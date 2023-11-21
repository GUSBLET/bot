package com.source.entities;

import lombok.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotResult {
    private BotApiMethod message;

    private boolean status;
}
