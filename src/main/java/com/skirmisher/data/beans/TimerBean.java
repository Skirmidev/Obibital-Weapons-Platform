package com.skirmisher.data.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerBean {
    @CsvBindByName(column = "timerId")
    int timerId;
    @CsvBindByName(column = "action")
    String action;
    @CsvBindByName(column = "args")
    String args;
    @CsvDate
    @CsvBindByName(column = "expiry")
    LocalDateTime expiry;
}