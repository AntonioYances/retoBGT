package com.retoBTG.retoBTG.controller;


import com.retoBTG.retoBTG.model.Fund;
import com.retoBTG.retoBTG.model.FundSubscriptionRequest;
import com.retoBTG.retoBTG.model.FundUnsubscriptionRequest;
import com.retoBTG.retoBTG.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private final FundService fundService;

    @Autowired
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    // Obtener todos los fondos disponibles
    @GetMapping
    public List<Fund> getAllFunds() {
        return fundService.getAllFunds();
    }

    // Suscribirse a un fondo
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToFund(@RequestBody FundSubscriptionRequest request) {
        String response = fundService.subscribeToFund(
                request.getFundId(),
                request.getAmount(),
                request.getNotificationType(),
                request.getContact()
        );
        return ResponseEntity.ok(response);

    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromFund(@RequestBody FundUnsubscriptionRequest request) {
        // Lógica de negocio para la desuscripción
        String response = fundService.unsubscribeFromFund(
                request.getFundId(),
                request.getNotificationType(),
                request.getContact()
        );
        return ResponseEntity.ok(response);
    }
}
