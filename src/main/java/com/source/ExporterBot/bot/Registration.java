package com.source.ExporterBot.bot;

import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.abilitybots.api.objects.Reply;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;



public class Registration implements AbilityExtension {

    private final AbilityBot extensionUser;

    public Registration(AbilityBot extensionUser) {
        this.extensionUser = extensionUser;
    }

    private final String correctPassword = "12345";
    private boolean registrationFlag = false;
    public Ability registration() {
        return Ability.builder()
                .name("registration")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(messageContext -> {
                        extensionUser.silent().send("Hello! Please enter your password:", messageContext.chatId());

                })
                .reply((baseAbilityBot, update) -> {


                        String userPassword = update.getMessage().getText();
                        if (userPassword.equals(correctPassword)) {
                            extensionUser.silent().send("Registration successful!", update.getMessage().getChatId());
                        } else {
                            extensionUser.silent().send("Incorrect password. Registration failed.", update.getMessage().getChatId());
                        }
                        registrationFlag = false; // Сбрасываем флаг после обработки сообщения

                }, Flag.TEXT)
                .build();
    }
}

