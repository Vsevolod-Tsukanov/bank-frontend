package com.haulmont.bankfrontend.dto.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientResponse {
    private String id;
    @NotBlank
    private Long telephoneNumber;
    @Email
    private String email;
    private Long passportNumber;
    private String bankId;
    private List<CreditOfferResponse> creditOffers;
}
