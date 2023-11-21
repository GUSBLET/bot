package com.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
		//
public class ExporterBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(ExporterBotApplication.class, args);


		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

		try {
			telegramBotsApi.registerBot(new ExporterBot());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

    }

}
