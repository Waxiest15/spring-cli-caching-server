package com.waxiest.spring_cli_caching_server.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${proxy.target}")
    private String target;

    @Bean
    public WebClient proxyWebClient() {
        HttpClient httpClient = HttpClient.create()
                .compress(true)
                .responseTimeout(Duration.ofSeconds(30))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000);

        return WebClient.builder()
                .baseUrl(target)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // Increase if upstream responses can have large headers
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}
