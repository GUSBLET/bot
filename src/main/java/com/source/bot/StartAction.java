package com.source.bot;

import com.source.entities.BotResult;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@RequiredArgsConstructor
public class StartAction implements Action{

    private final List<String> actions;
    @Override
    public BotResult handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        StringBuilder out = new StringBuilder();
        out.append("Выберите команду:").append("\n");
        for (String action : actions) {
            out.append(action).append("\n");
        }
        return getBotResult(true, chatId, out.toString());
    }

    @Override
    public BotResult callBack(Update update) {
        return handle(update);
    }
}
