package com.skirmisher.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class BannedStickerBean {
    @CsvBindByName(column = "packName")
    String packId;
}