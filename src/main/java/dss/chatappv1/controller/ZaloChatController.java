//package dss.chatappv1.controller;
//
//import com.vng.zalo.sdk.APIException;
//import dss.chatappv1.bot.ZaloBot;
//import dss.chatappv1.model.CMChat;
//import dss.chatappv1.model.Message;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ZaloChatController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    @Autowired
//    private ZaloBot zaloBot;
//
//    @MessageMapping("/messageZalo")
//    @SendTo("chatroom/zalo")
//    public void sendMessageToZalo(@Payload CMChat cmChat) throws APIException{
//        String response = zaloBot.sendMessageToZalo(cmChat.getCmChatId().toString(), cmChat.getContentText());
//        messagingTemplate.convertAndSend("chatroom/zalo", response);
//    }
//
//    @PostMapping("/send-message")
//    public String sendZaloMessage() throws APIException {
//        String response = zaloBot.sendMessageToZalo("2329111446320638514", "hello");
//        return response;
//    }
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> receiveZaloMessage(@RequestBody String message) {
//        System.out.println("Received message: " + message);
//
//        JSONObject jsonObject = new JSONObject(message);
//
//        String userId = jsonObject.getJSONObject("sender").getString("id"); // Lấy userId
//        String userMessage = jsonObject.has("message") ? jsonObject.getJSONObject("message").optString("text") : null;
//
//        String responseMessage = "";
//        String fileUrl = "";
//
//        // Kiểm tra nếu có attachments (ảnh, file)
//        if (jsonObject.has("attachments")) {
//            JSONArray attachments = jsonObject.getJSONArray("attachments");
//            JSONObject attachment = attachments.getJSONObject(0);  // Lấy file đính kèm đầu tiên
//            String mediaType = attachment.getString("type");
//
//            // Xử lý dựa trên loại file đính kèm
//            if ("image".equalsIgnoreCase(mediaType)) {
//                fileUrl = attachment.getString("url"); // Lấy URL của ảnh đính kèm
//                responseMessage = "Cảm ơn bạn đã gửi hình ảnh!";
//            } else {
//                responseMessage = "Cảm ơn bạn đã gửi file!";
//            }
//        }
//
//        // Gửi tin nhắn
//        if (!fileUrl.isEmpty()) {
//            zaloBot.sendImageMessageToZalo(userId, responseMessage, fileUrl); // Gửi tin nhắn có ảnh, file
//        } else {
//            zaloBot.sendMessageToZalo(userId, responseMessage);  // Gửi tin nhắn chỉ chứa text
//        }
//
//        return ResponseEntity.ok("Message processed and response sent.");
//    }
//
//
//    private String handleTextMessage(String userMessage) {
//        String responseMessage;
//
//        // Kiểm tra nội dung tin nhắn để phản hồi
//        switch (userMessage.toLowerCase()) {
//            case "hi":
//                responseMessage = "Chào bạn! Bạn có cần hỗ trợ gì không?";
//                break;
//            case "help":
//                responseMessage = "Bạn có thể hỏi tôi về các dịch vụ mà chúng tôi cung cấp.";
//                break;
//            default:
//                responseMessage = "";
//        }
//
//        return responseMessage;
//    }
//
//
//}
