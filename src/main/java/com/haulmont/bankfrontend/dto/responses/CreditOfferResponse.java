package com.haulmont.bankfrontend.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditOfferResponse {

    private String id;
    private String clientId;
    private String creditId;
    private String paymentScheduleId;
    private BigDecimal sumOfCredit;
    private Integer monthsOfCredit;
}
