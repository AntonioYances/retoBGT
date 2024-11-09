package com.retoBTG.Subscription_Service.repository;

import com.retoBTG.Subscription_Service.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
