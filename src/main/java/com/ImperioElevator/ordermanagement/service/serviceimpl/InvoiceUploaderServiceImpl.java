package com.ImperioElevator.ordermanagement.service.serviceimpl;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.function.BiFunction;

@Service
//ToDo the functions have no logger in the use cases
public class InvoiceUploaderServiceImpl {

    public String uploadInvoice(ByteArrayResource invoiceFile, String jwtToken) {
        return uploadToCDNFunction.apply(invoiceFile, jwtToken);
    }

    public final BiFunction<ByteArrayResource, String, String> uploadToCDNFunction = (invoiceFile, jwtToken) -> {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken); // Replace this with the actual token source
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            String uploadUrl = "http://cdn-service:9090/cdn/invoices/upload/invoice";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("Invoice", invoiceFile);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Return the invoice path (assumed to be in the response body)
            } else {
                throw new RuntimeException("Failed to upload invoice. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during invoice upload", e);
        }
    };

}