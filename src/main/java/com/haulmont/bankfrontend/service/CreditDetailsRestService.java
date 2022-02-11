package com.haulmont.bankfrontend.service;


import com.haulmont.bankfrontend.dto.responses.CreditDetailsResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreditDetailsRestService {

    private final RestTemplate restTemplate;

    public CreditDetailsRestService(RestTemplate restTemplate) {
        assert restTemplate != null;
        this.restTemplate = restTemplate;
    }

    public List<CreditDetailsResponse> getCreditDetail() {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/creditDetails")
                .build();
        CreditDetailsResponse[] response = restTemplate.getForObject(url.toString(), CreditDetailsResponse[].class);
        return Arrays.stream(Objects.requireNonNull(response))
                .collect(Collectors.toList());
    }

    public CreditDetailsResponse getCreditDetail(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/creditDetails/" + id)
                .build();

        return restTemplate.getForObject(url.toString(), CreditDetailsResponse.class);
    }

    public void createCreditDetail(CreditDetailsResponse credit) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/creditDetails")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<CreditDetailsResponse> request = new HttpEntity<>(credit, headers);
        try {
            restTemplate.postForLocation(url.toUri(), request);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot create credit detail");
        }
    }

    public void deleteCreditDetails(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/creditDetails/{id}")
                .buildAndExpand(id);
        try {
            restTemplate.delete(url.toUri());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot delete credit detail");
        }
    }

}
