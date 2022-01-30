package com.haulmont.bankfrontend.dto.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankResponse {
    private String id;
    private List<ClientResponse> clients;
    private List<CreditDetailsResponse> credits;
}
