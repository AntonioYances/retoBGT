package com.retoBTG.Subscription_Service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID
    private Long id;

    private String clientName;
    private String fundName;
    private boolean active;
    private String clientEmail;
    private String clientPhone;
}
