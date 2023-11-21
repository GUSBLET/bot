package com.source.bot;

import com.source.Connection;
import com.source.entities.BotResult;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@RequiredArgsConstructor
public class RegistretionAction implements Action {

    private final String action;
    private final Connection connection;
    private String correctPassword = "34faza5643";

    @Override
    public BotResult handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        StringBuilder out = new StringBuilder();
        out.append("Введите пароль:").append("\n");

        return getBotResult(true, chatId, out.toString());
    }

    @Override
    public BotResult callBack(Update update) {
        long userTelegramId = update.getMessage().getFrom().getId();
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        StringBuilder out = new StringBuilder();

        if (connection.sendRequest(userTelegramId, "/user/find-user")) {
            out.append("Вы уже зарегестрированы.");
            return getBotResult(true, chatId, out.toString());
        }

        if (!update.getMessage().getText().equals(correctPassword)) {
            out.append("Пароль не верный. Введите команду" + action + "и повторите попытку.");
            return getBotResult(false, chatId, out.toString());
        }

        if (connection.sendRequest(userTelegramId, "/user/add-new-user")) {
            out.append("Добро пожаловать в семью. Вы зарегистрированы.");
        } else
            out.append("Произошла ошибка при регистрации.");

        return getBotResult(true, chatId, out.toString());
    }
}
