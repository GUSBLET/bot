package com.source.ExporterBot.bot;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.Update;


import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;

@Component
public class ExporterBot extends AbilityBot {

    public ExporterBot(@Value("${bot.token}") String token, @Value("${bot.name}") String botName) {
        super(token, botName);
    }
    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("bam")
                .info("says hello world!")
                .input(0)
                .locality(ALL)
                .privacy(ADMIN)
                .action(ctx -> silent.send("Bye world!", ctx.chatId()))
                .post(ctx -> silent.send("Bye world!", ctx.chatId()))
                .build();
    }



    public AbilityExtension goodGuy() {
        return new MyAbilityExtension(this);
    }


    public AbilityExtension registration() {
        return new Registration(this);
    }


    @Override
    public long creatorId() {
        return 559791028;
    }


}
