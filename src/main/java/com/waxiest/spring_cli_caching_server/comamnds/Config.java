package com.waxiest.spring_cli_caching_server.comamnds;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;

@ShellComponent
public class Config {

    @Value("${port}")
    private String port;

    @Value("${proxy.target}")
    private String target;

    @ShellMethod(
            key = "config",
            value = "Get current server config (port and target)")
    public Map<String, String> getConfig() {

        return Map.of(
                "port", port,
                "target", target
        );
    }
}
