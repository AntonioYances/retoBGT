package com.retoBTG.retoBTG.model;

import lombok.Data;

@Data
public class FundUnsubscriptionRequest {

    private Long fundId;
    private String notificationType;
    private String contact;


}
