package com.waxiest.spring_cli_caching_server.comamnds;

import com.waxiest.spring_cli_caching_server.service.CacheService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class Cache {

    private final CacheService service;

    public Cache(CacheService service) {
        this.service = service;
    }

    @ShellMethod(key = "cache", value = "retrieve current cache content")
    public void getCache(){
        service.getCacheContent();
    }

}
