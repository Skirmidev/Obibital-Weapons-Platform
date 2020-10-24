package com.skirmisher.data.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBean {
    @CsvBindByName(column = "userId")
    Long userId;
}