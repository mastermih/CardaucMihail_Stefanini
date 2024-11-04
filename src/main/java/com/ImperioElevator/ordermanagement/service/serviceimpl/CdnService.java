package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CdnService {
    private static final Logger logger = LoggerFactory.getLogger(CdnService.class);


    public String sendImageToCDN(MultipartFile image, Long userId, String jwtToken) {
        try {
            String uploadUrl = "http://cdn-service:9090/cdn/upload";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Convert the image to a ByteArrayResource to avoid InputStream issues
            ByteArrayResource imageResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return userId + "-" + image.getOriginalFilename();
                }
            };


            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", imageResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                logger.error("Failed to upload to CDN. Status: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            logger.error("Error during file upload to CDN: ", e);
            return null;
        }
    }
}
