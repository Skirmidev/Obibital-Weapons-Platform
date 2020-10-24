package com.skirmisher.data;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BannedStickerBean {
    @CsvBindByName(column = "packName")
    String packId;

    // BannedStickerBean(String pack){
    //     packId = pack;
    // }
}