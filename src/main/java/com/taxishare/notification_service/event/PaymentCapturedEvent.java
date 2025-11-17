package com.taxishare.notification_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCapturedEvent {
    private String rideId;
    private String userId;
    private Double amount;
    private String currency;
}