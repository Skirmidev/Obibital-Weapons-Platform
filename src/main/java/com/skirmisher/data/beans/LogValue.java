package com.skirmisher.data.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogValue {
    String event;
    String sourceUser;
    String affectedUser;
    String notes;
    LocalDateTime time;
}