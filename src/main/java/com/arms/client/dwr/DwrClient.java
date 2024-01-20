package com.arms.client.dwr;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dwrClient", url = "http://backend-core:31313")
public interface DwrClient {

    @PostMapping("/send-message/server")
    void sendMessage(@RequestParam("message") String message);
}
