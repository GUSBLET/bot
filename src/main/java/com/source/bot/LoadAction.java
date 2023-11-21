package com.source.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.source.Connection;
import com.source.entities.BotResult;
import com.source.entities.PriceQuality;
import com.source.entities.ViewDTO;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LoadAction implements Action {


    private final String action;
    private final Connection connection;

    @Override
    public BotResult handle(Update update) {
        long userTelegramId = update.getMessage().getFrom().getId();
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        StringBuilder out = new StringBuilder();

        if (!connection.sendRequest(userTelegramId, "/user/find-user")) {
            out.append(" У вас нет прав.");
            return BotResult.builder()
                    .message(new SendMessage(chatId, out.toString()))
                    .status(true)
                    .build();
        }

        out.append("Отправте файл mview. Который должен содеражать следуйщий формат названия:")
                .append("\n")
                .append("CompanyName_ItemName_Type_AppendDate");

        return BotResult.builder()
                .message(new SendMessage(chatId, out.toString()))
                .status(true)
                .build();
    }

    @Override
    public BotResult callBack(Update update) {

        Message message = update.getMessage();
        StringBuilder out = new StringBuilder();

        String fileName = message.getDocument().getFileName();
        String chatId = message.getChatId().toString();
        String fileType = checkFileType(message.getDocument().getFileName());

        if (fileType == null || !fileNameChecking(fileName)) {
            out.append("Формат не верный.");
            return getBotResult(false, chatId, out.toString());
        } else if (fileType.equals("mview")) {
            ViewDTO dto = ViewDTO.builder()
                    .telegramId(Long.valueOf(chatId))
                    .mviewFile(getFile(message.getDocument()))
                    .build();

            dto = setFileParams(dto, fileName);
            if(dto == null){
                return getBotResult(true, chatId, "Не найдет такой формат качества");
            }

            if (sendFile(dto)) {
                out.append("Файл сохранен.");
            } else {
                out.append("Произошла ошибка.");
            }
        }

        return getBotResult(true, chatId, out.toString());
    }


    private boolean sendFile(ViewDTO dto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(dto);

            return connection.sendRequestView(json, "/view/add-view");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean сheckQualityExisting(String input) {
        try {
            PriceQuality priceQuality = PriceQuality.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String checkFileType(String fileName) {
        String[] fileParam = fileName.split("\\.");
        String result = null;
        if (fileParam[1].equals("mview")) {
            result = "mview";
        }
        return result;
    }

    private byte[] getFile(Document document) {
        String uploadedFileId = document.getFileId();
        GetFile uploadedFile = new GetFile();
        uploadedFile.setFileId(uploadedFileId);

        String token = "6655078079:AAEWNAL2GrkPnYBDH2oeOizbersTk19Nzig";

        JSONObject jsonObject = new JSONObject(connection.sendRequestGet("https://api.telegram.org/bot" + token + "/getFile?file_id=" + uploadedFileId));
        String filePath = String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));

        return connection.sendRequestGetFile("https://api.telegram.org/file/bot" + token + "/" + filePath);
    }

    private ViewDTO setFileParams(ViewDTO view, String fileName) {
        String[] fileParam = fileName.replace(".mview", "").split("_");

        if (!сheckQualityExisting(fileParam[2]))
            return null;

        view.setItemCompanyName(fileParam[0]);
        view.setItemName(fileParam[1].toUpperCase());
        view.setPriceQuality(PriceQuality.valueOf(fileParam[2]));
        view.setCreatingDate(convertToLocalDate(fileParam[3]));

        return view;
    }

    private LocalDate convertToLocalDate(String params) {

        int year = Integer.parseInt(params.substring(0, 2));
        int month = Integer.parseInt(params.substring(2, 4));
        int day = Integer.parseInt(params.substring(4, 6));

        int fullYear = 2000 + year;

        return LocalDate.of(fullYear, month, day);
    }


    private boolean fileNameChecking(String fileName) {
        String[] fileParam = fileName.replace(".html", "").split("_");
        return fileParam.length == 4;
    }
}


