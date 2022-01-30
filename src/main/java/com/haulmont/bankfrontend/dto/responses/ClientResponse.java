package com.haulmont.bankfrontend.dto.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientResponse {
    private String id;
    private Long telephoneNumber;
    private String email;
    private Long passportNumber;
    private String bankId;
    private List<CreditOfferResponse> creditOffers;
}
