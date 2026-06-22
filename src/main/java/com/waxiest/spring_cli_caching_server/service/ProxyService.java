package com.waxiest.spring_cli_caching_server.service;

import com.waxiest.spring_cli_caching_server.properties.ProxyTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class ProxyService {

    private final WebClient webClient;
    private final ProxyTarget target;

    public ProxyService(WebClient webClient, ProxyTarget target) {
        this.webClient = webClient;
        this.target = target;
    }

    public Mono<Void> forward(ServerHttpRequest request, ServerHttpResponse response, Flux<DataBuffer> body) {
        URI uri = URI.create(target.get() + request.getURI().getRawPath()
                + (request.getURI().getRawQuery() != null ? "?" + request.getURI().getRawQuery() : ""));

        log.info("forwarding to {}", uri);

        return webClient
                .method(request.getMethod())
                .uri(uri)
                .headers(headers -> {
                    headers.addAll(request.getHeaders());
                    headers.remove(HttpHeaders.HOST);
                    headers.remove(HttpHeaders.ACCEPT_ENCODING);
                })
                .body(BodyInserters.fromDataBuffers(body))
                .exchangeToMono(upstream -> {
                    response.setStatusCode(upstream.statusCode());
                    response.getHeaders().addAll(upstream.headers().asHttpHeaders());
                    return response.writeWith(upstream.bodyToFlux(DataBuffer.class));
                });
    }
}