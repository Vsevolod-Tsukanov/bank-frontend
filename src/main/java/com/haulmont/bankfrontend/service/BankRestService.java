package com.haulmont.bankfrontend.service;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
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


public class BankRestService {

    private final RestTemplate restTemplate;

    public BankRestService(RestTemplate restTemplate) {
        assert restTemplate != null;
        this.restTemplate = restTemplate;
    }


    public List<BankResponse> getBanks() {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/banks")
                .build();
        BankResponse[] response = restTemplate.getForObject(url.toString(), BankResponse[].class);
        return Arrays.stream(Objects.requireNonNull(response))
                .collect(Collectors.toList());
    }

    public BankResponse getBank(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/banks/" + id)
                .build();

        return restTemplate.getForObject(url.toString(), BankResponse.class);
    }

    public void createBank(BankResponse bank) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/banks")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<BankResponse> request = new HttpEntity<>(bank, headers);
        try {
            restTemplate.postForLocation(url.toUri(), request);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot create bank");
        }
    }

    public void deleteBank(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/banks/{id}")
                .buildAndExpand(id);
        try {
            restTemplate.delete(url.toUri());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot delete bank");
        }
    }
}
