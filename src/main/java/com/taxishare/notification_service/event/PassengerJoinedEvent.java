package com.taxishare.notification_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerJoinedEvent {
    private String rideId;
    private String newPassengerId;
    private String newPassengerName;
    private List<String> existingPassengerIds;
    private String newEta;
}