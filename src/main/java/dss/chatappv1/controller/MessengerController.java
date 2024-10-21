package dss.chatappv1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class MessengerController {
    @Value("messenger.page_access_token")
    private String PAGE_ACCESS_TOKEN;
    @Value("${messenger.verify}")
    private String verify_Token;

    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam(name = "hub.mode") String mode,
                                                @RequestParam(name = "hub.verify_token") String verifyToken,
                                                @RequestParam(name = "hub.challenge") String challenge) {
        if (verify_Token.equals(verifyToken) && "subscribe".equals(mode)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> Webhook(@RequestBody Map<String, Object> body) {
        System.out.println("\uD83D\uDD35 Received webhook:");
        System.out.println(body);

        if ("page".equals(body.get("object"))) {
            // Xử lý thông báo từ page
            return ResponseEntity.ok("EVENT_RECEIVED");
        } else {
            // Không phải thông báo từ page subscription
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

    // Phương thức gửi tin nhắn
    private void sendMessage(String recipientId, String messageText) {
        String url = "https://graph.facebook.com/v12.0/me/messages?access_token=" + PAGE_ACCESS_TOKEN;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String message = String.format("{\"recipient\":{\"id\":\"%s\"},\"message\":{\"text\":\"%s\"}}", recipientId, messageText);

        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
