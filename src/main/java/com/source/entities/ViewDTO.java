package com.source.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDTO {
    private Long telegramId;

    private String itemName;

    private String itemCompanyName;

    private LocalDate creatingDate;

    private PriceQuality priceQuality;

    private byte[] mviewFile;
}
