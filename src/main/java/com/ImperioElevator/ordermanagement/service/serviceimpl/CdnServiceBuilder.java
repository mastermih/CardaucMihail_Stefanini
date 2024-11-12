package com.ImperioElevator.ordermanagement.service.serviceimpl;

import org.springframework.web.client.RestTemplate;

public class CdnServiceBuilder {
     String uploadUrl = "http://cdn-service:9090/cdn/upload";
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

