package com.retoBTG.retoBTG.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class FundSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;
    private Long fundId;
    private String type;
    private Double amount;
    private Date transactionDate;
    private String clientEmail;
    private String clientPhone;
}
