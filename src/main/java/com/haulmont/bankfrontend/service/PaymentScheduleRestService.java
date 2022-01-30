package com.haulmont.bankfrontend.service;

import com.haulmont.bankfrontend.dto.responses.BankResponse;
import com.haulmont.bankfrontend.dto.responses.PaymentScheduleResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PaymentScheduleRestService {

    private final RestTemplate restTemplate;

    public PaymentScheduleRestService(RestTemplate restTemplate) {
        assert restTemplate != null;
        this.restTemplate = restTemplate;
    }

    public List<PaymentScheduleResponse> getPaymentSchedules() {
        UriComponents url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8090)
                .path("/schedules")
                .build();
        PaymentScheduleResponse[] response = restTemplate.getForObject(url.toString(), PaymentScheduleResponse[].class);
        return Arrays.stream(Objects.requireNonNull(response))
                .collect(Collectors.toList());
    }
}
