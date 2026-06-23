package com.waxiest.spring_cli_caching_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

    private final ProxyService service;

    public CacheService(ProxyService service) {
        this.service = service;
    }

    public void getCacheContent() {
        log.info("cache content: {}", service.getCacheContents().toString());
    }
}
