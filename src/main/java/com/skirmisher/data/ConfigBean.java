package com.skirmisher.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class ConfigBean {
    @CsvBindByName(column = "element")
    String element;
    @CsvBindByName(column = "value")
    String value;
}