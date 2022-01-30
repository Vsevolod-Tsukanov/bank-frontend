package com.haulmont.bankfrontend.service;


import com.haulmont.bankfrontend.dto.responses.ClientResponse;
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

public class ClientRestService {


    private final RestTemplate restTemplate;

    public ClientRestService(RestTemplate restTemplate) {
        assert restTemplate != null;
        this.restTemplate = restTemplate;
    }

    public List<ClientResponse> getClients() {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/clients")
                .build();
        ClientResponse[] response = restTemplate.getForObject(url.toString(), ClientResponse[].class);
        return Arrays.stream(Objects.requireNonNull(response))
                .collect(Collectors.toList());
    }

    public ClientResponse getClient(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/clients/" + id)
                .build();

        return restTemplate.getForObject(url.toString(), ClientResponse.class);
    }

    public void createClient(ClientResponse client) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/clients")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<ClientResponse> request = new HttpEntity<>(client, headers);
        try {
            restTemplate.postForLocation(url.toUri(), request);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot create client");
        }
    }

    public void deleteClient(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/clients/{id}")
                .buildAndExpand(id);
        try {
            restTemplate.delete(url.toUri());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot delete client");
        }
    }

}
