package com.skirmisher.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class AdminBean {
    @CsvBindByName(column = "userId")
    Long userId;
}