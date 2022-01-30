package com.haulmont.bankfrontend.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentScheduleResponse {

    private UUID id;
    private Date dateOfFirstPayment;
    private Date dateOfLastPayment;
    private BigDecimal sumOfMonthlyPayment;
    private BigDecimal sumOfPayment;
    private BigDecimal sumOfPrincipal;
    private BigDecimal sumOfPercent;
    private CreditOfferResponse creditOffer;
}
