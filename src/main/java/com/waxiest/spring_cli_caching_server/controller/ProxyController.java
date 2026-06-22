package com.waxiest.spring_cli_caching_server.controller;

import com.waxiest.spring_cli_caching_server.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/**")
    public Mono<Void> proxy(ServerHttpRequest request, ServerHttpResponse response,
                            @RequestBody(required = false) Flux<DataBuffer> body) {
        log.info("incoming request at {}", request.getURI());
        return proxyService.forward(request, response, body != null ? body : Flux.empty());
    }
}
