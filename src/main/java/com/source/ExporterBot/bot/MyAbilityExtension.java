package com.source.ExporterBot.bot;

import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;



public class MyAbilityExtension implements AbilityExtension {
    private final AbilityBot extensionUser;

    public MyAbilityExtension(AbilityBot extensionUser) { this.extensionUser = extensionUser; }

    public Ability nice() {
        return Ability.builder()
                .name("nice")
                .privacy(PUBLIC)
                .locality(ALL)
                .action(ctx -> {
                    extensionUser.silent().send("Hello! Please enter your name:", ctx.chatId());
                    // После отправки этого сообщения вы можете ждать ответа от пользователя
                    // и обрабатывать его в следующем обновлении (Update).
                }).build();
    }



}
