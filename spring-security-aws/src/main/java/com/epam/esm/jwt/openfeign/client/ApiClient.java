package com.epam.esm.jwt.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}")
public interface ApiClient {

    @GetMapping
    Map<String,String> verifyGoogleToken (@RequestParam("id_token") String token);
}
