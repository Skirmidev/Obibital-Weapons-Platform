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
public class StatisticBean { //Time, Event, SourceUser, AffectedUser, Notes
    @CsvDate
    @CsvBindByName(column = "time")
    LocalDateTime time;
    @CsvBindByName(column = "event")
    String event;
    @CsvBindByName(column = "sourceUser")
    String sourceUser;
    @CsvBindByName(column = "affectedUser")
    String affectedUser;
    @CsvBindByName(column = "notes")
    String notes;
}