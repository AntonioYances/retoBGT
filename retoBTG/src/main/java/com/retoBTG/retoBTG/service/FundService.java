package com.retoBTG.retoBTG.service;

import com.retoBTG.retoBTG.config.NotificationClient;
import com.retoBTG.retoBTG.model.Fund;
import com.retoBTG.retoBTG.model.FundSubscription;
import com.retoBTG.retoBTG.repository.FundRepository;
import com.retoBTG.retoBTG.repository.FundSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FundService {

    private final FundRepository fundRepository;
    private final FundSubscriptionRepository fundSubscriptionRepository;
    private final NotificationClient notificationClient;

    public FundService(FundRepository fundRepository, FundSubscriptionRepository fundSubscriptionRepository, NotificationClient notificationClient) {
        this.fundRepository = fundRepository;
        this.fundSubscriptionRepository = fundSubscriptionRepository;
        this.notificationClient = notificationClient;
    }


    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    public String subscribeToFund(Long fundId, Double amount, String notificationType, String phoneNumber) {
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new IllegalArgumentException("Fund not found"));

        if (amount < fund.getMinInvestmentAmount()) {
            return "Insufficient funds to subscribe to " + fund.getName();
        }

        FundSubscription fundSubscription = new FundSubscription();
        fundSubscription.setFundId(fundId);
        fundSubscription.setType("subscription");
        fundSubscription.setAmount(amount);
        fundSubscription.setTransactionDate(new Date());
        fundSubscription.setClientEmail("antonioyances10@gmail.com");
        fundSubscription.setClientPhone("+13143092395");


        fundSubscriptionRepository.save(fundSubscription);

        String message = "You have successfully subscribed to the fund " + fund.getName();

        if (notificationType.equals("sms")) {
            notificationClient.sendSms(fundSubscription.getClientPhone(),
                    "Thank you for subscribing to " + fund.getName());
        } else if (notificationType.equals("email")) {
            notificationClient.sendEmail(fundSubscription.getClientEmail(),
                    "New Subscription",
                    "Thank you for subscribing to " + fund.getName());
        }

        return message;
    }

    public String unsubscribeFromFund(Long fundId, String notificationType, String phoneNumber) {
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new IllegalArgumentException("Fund not found"));

        FundSubscription fundSubscription = new FundSubscription();
        fundSubscription.setFundId(fundId);
        fundSubscription.setType("cancellation");
        fundSubscription.setAmount(fund.getMinInvestmentAmount());
        fundSubscription.setTransactionDate(new Date());

        fundSubscriptionRepository.save(fundSubscription);

        String message = "You have successfully unsubscribed from the fund " + fund.getName();

        if (notificationType.equals("sms")) {
            notificationClient.sendSms(fundSubscription.getClientPhone(),
                    "Thank you for subscribing to ");
        } else if (notificationType.equals("email")) {
            notificationClient.sendEmail(fundSubscription.getClientEmail(),
                    "New Subscription",
                    "Thank you for subscribing to ");
        }

        return message;
    }
}
