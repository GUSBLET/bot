package com.source.bot;

import com.source.entities.BotResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    BotResult handle(Update update);

    BotResult callBack(Update update);

    default BotResult getBotResult(boolean status, String chatId, String out){
        return BotResult.builder()
                .message(new SendMessage(chatId, out))
                .status(true)
                .build();
    }
}
