package com.taxishare.notification_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideAcceptedEvent {
    private String rideId;
    private String passengerId;
    private String driverId;
    private String driverName;
    private String eta;
}