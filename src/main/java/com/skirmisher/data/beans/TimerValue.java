package com.skirmisher.data.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerValue {
    int id;
    String action;
    String args;
}