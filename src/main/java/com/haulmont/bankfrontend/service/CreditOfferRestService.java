package com.haulmont.bankfrontend.service;


import com.haulmont.bankfrontend.dto.responses.CreditOfferResponse;
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

public class CreditOfferRestService {

    private final RestTemplate restTemplate;

    public CreditOfferRestService(RestTemplate restTemplate) {
        assert restTemplate != null;
        this.restTemplate = restTemplate;
    }

    public List<CreditOfferResponse> getCreditOffers() {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/offers")
                .build();
        CreditOfferResponse[] response = restTemplate.getForObject(url.toString(), CreditOfferResponse[].class);
        return Arrays.stream(Objects.requireNonNull(response))
                .collect(Collectors.toList());
    }

    public CreditOfferResponse getCreditOffers(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/offers/" + id)
                .build();

        return restTemplate.getForObject(url.toString(), CreditOfferResponse.class);
    }

    public void createCreditOffer(CreditOfferResponse creditOffer) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/offers")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<CreditOfferResponse> request = new HttpEntity<>(creditOffer, headers);
        try {
            restTemplate.postForLocation(url.toUri(), request);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot create credit offer");
        }
    }

    public void deleteCreditOffer(String id) {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/offers/{id}")
                .buildAndExpand(id);
        try {
            restTemplate.delete(url.toUri());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Cannot delete credit offer");
        }
    }

}
