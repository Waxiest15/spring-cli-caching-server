package com.waxiest.spring_cli_caching_server.service;

import com.waxiest.spring_cli_caching_server.properties.ProxyTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ProxyService {

    private final WebClient webClient;
    private final ProxyTarget target;

    public ProxyService(WebClient webClient, ProxyTarget target) {
        this.webClient = webClient;
        this.target = target;
    }

    private final Map<String, Mono<ResponseEntity<byte[]>>> cache = new ConcurrentHashMap<>();

    public Mono<Void> forward(ServerHttpRequest request, ServerHttpResponse response, Flux<DataBuffer> body) {
        return fetchUpstream(request, body)
                .flatMap(upstream -> {
                    response.setStatusCode(upstream.getStatusCode());
                    response.getHeaders().addAll(upstream.getHeaders());
                    byte[] cachedBody = upstream.getBody();
                    DataBuffer buffer = response.bufferFactory().wrap(cachedBody);
                    return response.writeWith(Mono.just(buffer));
                });
    }

    public Mono<ResponseEntity<byte[]>> fetchUpstream(ServerHttpRequest request, Flux<DataBuffer> body) {
        String key = request.getURI().toString();

        return cache.computeIfAbsent(key, k -> {
            URI uri = URI.create(target.get() + request.getURI().getRawPath()
                    + (request.getURI().getRawQuery() != null ? "?" + request.getURI().getRawQuery() : ""));

            log.info("cache miss — fetching from upstream: {}", uri);

            return webClient
                    .method(request.getMethod())
                    .uri(uri)
                    .headers(headers -> {
                        headers.addAll(request.getHeaders());
                        headers.remove(HttpHeaders.HOST);
                        headers.remove(HttpHeaders.ACCEPT_ENCODING);
                    })
                    .body(BodyInserters.fromDataBuffers(body))
                    .retrieve()
                    .toEntity(byte[].class)
                    .cache();
        });
    }

    // expose for your CacheService to inspect
    public Map<String, Mono<ResponseEntity<byte[]>>> getCacheContents() {
        return cache;
    }
}
