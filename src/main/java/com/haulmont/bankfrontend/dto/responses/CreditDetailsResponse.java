package com.haulmont.bankfrontend.dto.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditDetailsResponse {

    private String id;
    private BigDecimal creditLimit;
    private BigDecimal creditPercent;
    private String bankId;
    private List<CreditOfferResponse> creditOffers;
}
