package dss.chatappv1.bot;

import dss.chatappv1.model.Message;
import dss.chatappv1.model.Status;
import dss.chatappv1.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String TELEGRAM_API_URL = "https://api.telegram.org/botYOUR_BOT_TOKEN";

    private int lastUpdateId = 0;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;
    private final String botName;

    // Constructor for the bot
    public TelegramBot(String botName, String botToken, SimpMessagingTemplate messagingTemplate){
        super(botToken);
        this.botName = botName;
        this.messagingTemplate =  messagingTemplate;
    }

    public List<Update> getPaginatedUpdates(int limit) throws TelegramApiException {
        GetUpdates getUpdates = new GetUpdates();
        getUpdates.setLimit(limit);
        getUpdates.setOffset(lastUpdateId + 1);

        List<Update> updates = execute(getUpdates);

        if (!updates.isEmpty()) {
            lastUpdateId = updates.get(updates.size() - 1).getUpdateId();  // Cập nhật lastUpdateId
        }
        return updates;
    }

    // Method to handle incoming messages from Telegram
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();
            String fullName = update.getMessage().getFrom().getFirstName();

            // Kiểm tra xem có họ không
            if (update.getMessage().getFrom().getLastName() != null) {
                fullName += " " + update.getMessage().getFrom().getLastName();
            }

            // Kiểm tra xem tin nhắn có chứa text không
            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                log.info("Message received from chat ID {}, username: {}, full name: {} message: {}", chatId, userName, fullName, messageText);
                broadcastMessageToChatApp(chatId, fullName, messageText);
            }

            if (update.getMessage().hasDocument() || update.getMessage().hasPhoto()) {
                String fileId = update.getMessage().hasDocument() ?
                        update.getMessage().getDocument().getFileId() :
                        update.getMessage().getPhoto().get(0).getFileId();

                try {
                    // Mã hóa file thành Base64
                    String base64File = downloadAndEncodeFile(fileId);

                    // Gửi file qua WebSocket
                    broadcastFileToChatApp(chatId, fullName, base64File);
                } catch (IOException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void broadcastFileToChatApp(Long chatId, String fullName, String base64File) throws IOException {
        Message fileMessage = new Message();
        fileMessage.setChatId(chatId);
        fileMessage.setSenderName(fullName);
        fileMessage.setFileUrl(base64File);

        // Gửi qua WebSocket
        messagingTemplate.convertAndSend("/chatroom/telegram", fileMessage);

        log.info("Broadcast file to chat app: {}", fileMessage);
    }

    public String encodeFileToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];  // Kích thước buffer để đọc file
        int bytesRead;

        // Đọc dữ liệu từ InputStream và ghi vào ByteArrayOutputStream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] fileBytes = outputStream.toByteArray();

        // Chuyển đổi mảng byte thành chuỗi Base64
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    public String downloadAndEncodeFile(String fileId) throws IOException, TelegramApiException {
        // Tải file từ Telegram dưới dạng InputStream
        InputStream fileStream = downloadFileAsStream(fileId);

        // Mã hóa file thành chuỗi Base64
        return encodeFileToBase64(fileStream);
    }



    // Method to send messages to Telegram
    public void sendMessageToTelegram(Long chatId, String message) {
        SendMessage telegramMessage = new SendMessage();
        telegramMessage.setChatId(chatId);
        telegramMessage.setText(message);

        try {
            execute(telegramMessage);  // Send the message via Telegram API
            log.info("Sent message to Telegram chat ID {}: {}", chatId, message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to Telegram chat ID {}: {}", chatId, e.getMessage());
        }
    }

    // Broadcast message from Telegram to chat app using WebSocket
    public void broadcastMessageToChatApp(Long chatId, String user, String messageText) {
        String timestamp = getCurrentTime();

        // Create a Message object
        Message message = new Message();
        message.getMessage_id();
        message.setChatId(chatId);
        message.setMessage(messageText);
        message.setSenderName(user);
        message.setTime(timestamp);
        message.setStatus(Status.SENT);

        // Send the message to the WebSocket topic
        messagingTemplate.convertAndSend("/chatroom/telegram", message);

        log.info("Broadcast message to chat app: {}", message);
    }



    // Method to handle both file and text messages
    public void sendFileToTelegram(Message message) {
        Long chatId = message.getChatId();
        String fileUrl = message.getFileUrl();
        log.info(fileUrl);
        String fileType = message.getFileType();

        try {
            if (fileType.startsWith("image")) {
                sendPhotoToTelegram(chatId, fileUrl);
            } else if (fileType.startsWith("application")) {
                sendDocumentToTelegram(chatId, fileUrl);
            } else if (fileType.startsWith("audio")) {
                sendAudioToTelegram(chatId, fileUrl);  // Gọi hàm gửi file âm thanh
            } else if (fileType.startsWith("video")) {
                sendVideoToTelegram(chatId, fileUrl);  // Gọi hàm gửi video nếu cần
            } else {
                // Log unsupported file types and handle them appropriately
                log.warn("Unsupported file type: {}", fileType);
            }
        } catch (TelegramApiException e) {
            log.error("Error sending file to Telegram chat ID {}: {}", chatId, e.getMessage());
        }
    }

    private void sendAudioToTelegram(Long chatId, String fileUrl) throws TelegramApiException {
        File file = new File(fileUrl);
        InputFile audioFile = new InputFile(file);
        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(chatId.toString());
        sendAudio.setAudio(audioFile);
        execute(sendAudio);  // Send audio via Telegram API
        log.info("Sent audio to Telegram chat ID {}: {}", chatId, fileUrl);
    }

    // Method to send video to Telegram using URL (nếu cần)
    private void sendVideoToTelegram(Long chatId, String fileUrl) throws TelegramApiException {
        File file = new File(fileUrl);
        InputFile videoFile = new InputFile(file);
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(chatId);
        sendVideo.setVideo(videoFile);
        execute(sendVideo);  // Send video via Telegram API
        log.info("Sent video to Telegram chat ID {}: {}", chatId, fileUrl);
    }

    // Method to send photo to Telegram using URL
    private void sendPhotoToTelegram(Long chatId, String fileUrl) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);

        File file = new File(fileUrl);
        // Use InputFile to send the photo from URL
        InputFile photo = new InputFile(file);
        sendPhoto.setPhoto(photo);

        execute(sendPhoto);  // Send photo via Telegram API
        log.info("Sent photo to Telegram chat ID {}: {}", chatId, fileUrl);
    }

    // Method to send document to Telegram using URL
    private void sendDocumentToTelegram(Long chatId, String fileUrl) throws TelegramApiException {
        File file = new File(fileUrl);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        String fileName = getOriginalFileName(fileUrl);
        // Use InputFile to send the document from URL
        InputFile document = new InputFile(file, fileName);
        sendDocument.setDocument(document);

        execute(sendDocument);  // Send document via Telegram API
        log.info("Sent document to Telegram chat ID {}: {}", chatId, fileUrl);
    }


    public String getCurrentTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    private String getOriginalFileName(String fileUrl) {
        String fileNameWithUUID = Paths.get(fileUrl).getFileName().toString();
        return fileNameWithUUID.substring(fileNameWithUUID.indexOf("fileUrl_") + "fileUrl_".length());
    }

}