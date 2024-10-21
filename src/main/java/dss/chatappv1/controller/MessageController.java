package dss.chatappv1.controller;

import dss.chatappv1.bot.TelegramBot;
import dss.chatappv1.model.Message;
import dss.chatappv1.model.Status;
import dss.chatappv1.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@CrossOrigin(origins ="*")
public class MessageController {

    private static final String UPLOADED_FOLDER = "C:\\Users\\ADMIN\\Downloads\\";

    @Autowired
    TelegramBot telegramBot;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public void chatPublic(@Payload Message message) {
        message.getMessage_id();
        String timestamp = getCurrentTime();
        message.setTime(timestamp);
        message.setStatus(Status.SENT);
        Message newMessage = messageService.createMessage(message);

        // Send message to chat app
        simpMessagingTemplate.convertAndSend("/chatroom/public", newMessage);
        log.info("Response:"+ new ResponseEntity<>(newMessage, HttpStatus.OK));
    }

    @PostMapping("/api/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("senderName") String senderName,
            @RequestParam("receiverName") String receiverName,
            @RequestParam(value = "message", required = false) String messageText,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Message message = new Message();
        Map<String, String> response = new HashMap<>();
        String fileUrl = null;
        String fileType = null;

        // Xử lý việc upload file
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "fileUrl_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOADED_FOLDER, fileName); // đường dẫn

                if (!Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, file.getBytes());

                if (Files.exists(path)) {
                    fileUrl = UPLOADED_FOLDER + fileName; // Tạo URL
                    fileType = file.getContentType();
                    response.put("fileUrl", fileUrl);
                    response.put("fileType", fileType);
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "File upload failed: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        message.setSenderName(senderName);
        message.setReceiverName(receiverName);
        message.setMessage(messageText);
        message.setFileUrl(fileUrl);
        message.setFileType(fileType);
        message.setTime(getCurrentTime());
        message.setStatus(Status.SENT);

        // Gọi phương thức receiveMessageTelegram để gửi tin nhắn
        chatPublic(message);

        // Trả về phản hồi sau khi hoàn thành
        response.put("status", "Message sent successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOADED_FOLDER).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine the file's content type
                String contentType = Files.probeContentType(file);

                // Fallback to octet-stream if content type is not determined
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/chat/history")
    public ResponseEntity<List<Message>> getChatHistory(
            @RequestParam(value = "chatId", required = false) Long chatId,
            @RequestParam(value = "senderName", required = false) String senderName,
            @RequestParam(value = "receiverName", required = false) String receiverName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size ) {

        System.out.println("Sender: " + senderName + ", Receiver: " + receiverName);
        Page<Message> chatHistory;

        Pageable pageable = PageRequest.of(page, size);
        if (chatId != null) {
            chatHistory = messageService.getMessagesByChatId(chatId, pageable);
        } else if (senderName != null && receiverName != null) {
            chatHistory = messageService.getMessagesBetweenUsers(senderName, receiverName, pageable );
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(chatHistory.getContent(), HttpStatus.OK);
    }

    @GetMapping("/api/chat/public")
    public ResponseEntity<List<Message>> getChatHistory(
            @RequestParam(value = "receiverName") String receiverName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size ) {

        Page<Message> chatHistory;

        Pageable pageable = PageRequest.of(page, size);

            chatHistory = messageService.findMessagesByReceiverName(receiverName,pageable );

        return new ResponseEntity<>(chatHistory.getContent(), HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<Message> updateMessageStatus(@RequestBody Message update) {
        Message updatedMessage = messageService.updateMessageStatus(update);

        if (updatedMessage == null) {
            return ResponseEntity.notFound().build();  // Trả về 404 nếu không tìm thấy tin nhắn
        }

        return ResponseEntity.ok(updatedMessage);
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        message.getMessage_id();
        String timestamp = getCurrentTime();
        message.setTime(timestamp);
        message.setStatus(Status.SENT);
        Message newMessage = messageService.createMessage(message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", newMessage);
        System.out.println(newMessage);
        return newMessage;
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(dtf);
    }
}