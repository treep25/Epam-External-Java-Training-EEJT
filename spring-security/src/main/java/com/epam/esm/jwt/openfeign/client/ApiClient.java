package com.epam.esm.jwt.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "google-verify", url = "https://oauth2.googleapis.com/tokeninfo")
public interface ApiClient {

    @GetMapping
    Map<String,String> verifyGoogleToken (@RequestParam("id_token") String token);
}
