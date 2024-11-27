package com.ImperioElevator.ordermanagement.service.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CdnServiceBuilder {
    @Value("${cdn.server.url}")
    String uploadUrl;

     RestTemplate restTemplate;

    public CdnServiceBuilder withUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
        return this;
    }

    public CdnServiceBuilder withRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return this;
    }

    public CdnService build() {
        return new CdnService(this);
    }
}

