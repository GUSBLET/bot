package com.source;

import com.source.bot.*;
import com.source.bot.Action;
import com.source.bot.LoadAction;
import com.source.bot.RegistretionAction;
import com.source.bot.StartAction;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Service
public class ExporterBot extends TelegramLongPollingBot {

    private Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private Map<String, Action> actions = Map.of(
            "/start", new StartAction(
                    List.of(
                            "/start",
                            "/registration - регистрация нового пользователя",
                            "/load - загрузка файла"
                    )
            ),
            "/registration", new RegistretionAction("/registration", new Connection(new RestTemplate())),
            "/load", new LoadAction("/load", new Connection(new RestTemplate()))
    );

    @Override
    public String getBotUsername() {

        return "exporter_file_bot";
    }

    @Override
    public String getBotToken() {
        return "6655078079:AAEWNAL2GrkPnYBDH2oeOizbersTk19Nzig";
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {

            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().hasDocument()) {


                if (bindingBy.containsKey(chatId)) {
                    var result = actions.get(bindingBy.get(chatId)).callBack(update);
                    if (result.isStatus()) {
                        bindingBy.remove(chatId);
                    }
                    send(result.getMessage());
                }
            } else {
                var key = update.getMessage().getText();

                if (actions.containsKey(key)) {
                    var result = actions.get(key).handle(update);
                    bindingBy.put(chatId, key);
                    send(result.getMessage());
                } else if (bindingBy.containsKey(chatId)) {
                    var result = actions.get(bindingBy.get(chatId)).callBack(update);
                    if (result.isStatus()) {
                        bindingBy.remove(chatId);
                    }
                    send(result.getMessage());
                }
            }


        }
    }

    private void changeModeByChatId(String chatId) {
        switch (bindingBy.get(chatId)) {
            case "/load":
                bindingBy.remove(chatId);
                bindingBy.put(chatId, "loadView");
                break;
            default:

                break;
        }
    }


    private void send(BotApiMethod sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
