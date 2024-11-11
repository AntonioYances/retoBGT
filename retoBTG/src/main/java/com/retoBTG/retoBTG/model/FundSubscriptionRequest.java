package com.retoBTG.retoBTG.model;

import lombok.Data;

@Data
public class FundSubscriptionRequest {

    private Long fundId;
    private Double amount;
    private String notificationType;
    private String contact;
    private String clientEmail;
    private String clientPhone;

}
