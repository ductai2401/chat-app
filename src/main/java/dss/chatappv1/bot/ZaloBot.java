//package dss.chatappv1.bot;
//
//
//import com.google.gson.JsonObject;
//import com.vng.zalo.sdk.APIException;
//import com.vng.zalo.sdk.oa.ZaloOaClient;
//import dss.chatappv1.config.ZaloConfig;
//import dss.chatappv1.model.Message;
//import dss.chatappv1.service.MessageService;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.FileEntity;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ZaloBot extends ZaloOaClient {
//
//    private static final String ZALO_MESSAGE_API = "https://openapi.zalo.me/v3.0/oa/message/cs";
//    @Value("${zalo.oa.access.token}")
//    private String accessToken;
//
//    public String sendMessageToZalo(String userId, String message) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("access_token", accessToken);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("recipient", Map.of("user_id", userId));
//        body.put("message", Map.of("text", message));
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(ZALO_MESSAGE_API, request, String.class);
//        return response.getBody();
//    }
//
//    public String sendImageMessageToZalo(String userId, String messageText, String imageUrl) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("access_token", accessToken);
//
//        // Tạo nội dung tin nhắn chứa ảnh và text
//        Map<String, Object> body = new HashMap<>();
//        body.put("recipient", Map.of("user_id", userId));
//        body.put("message", Map.of(
//                "text", messageText,
//                "attachment", Map.of(
//                        "type", "template",
//                        "payload", Map.of(
//                                "template_type", "media",
//                                "elements", List.of(Map.of(
//                                        "media_type", "image",
//                                        "url", imageUrl
//                                ))
//                        )
//                )
//        ));
//
//        // Gửi tin nhắn
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(ZALO_MESSAGE_API, request, String.class);
//
//        return response.getBody();
//    }
//
//
//
//}