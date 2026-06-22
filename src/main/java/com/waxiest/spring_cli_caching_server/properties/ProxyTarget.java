package com.waxiest.spring_cli_caching_server.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ProxyTarget {

    private final AtomicReference<String> url;

    public ProxyTarget(@Value("${proxy.target}") String initialTarget) {
        this.url = new AtomicReference<>(initialTarget);
    }

    public String get() {
        return url.get();
    }

    public void set(String newTarget) {
        url.set(newTarget);
    }
}
