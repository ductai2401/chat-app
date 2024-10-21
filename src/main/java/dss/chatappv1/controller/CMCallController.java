package dss.chatappv1.controller;

import dss.chatappv1.controller.dto.CMCallDto;
import dss.chatappv1.model.CMCall;
import dss.chatappv1.service.CMCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CMCallController {

    @Autowired
    CMCallService cmCallService;

    private static final String USERNAME = "CRM";
    private static final String PASSWORD = "1";

    @PostMapping("/v1/models/CM_CALL")
    public ResponseEntity<String> receiveCallData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                  @RequestBody Map<String, Object> payload) {
        try {
            if (!authorizationHeader.startsWith("Basic ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or incorrect Authorization header.");
            }

            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            String[] values = credentials.split(":", 2);
            String inputUsername = values[0];
            String inputPassword = values[1];

            // Kiểm tra username và password
            if (!USERNAME.equals(inputUsername) || !PASSWORD.equals(inputPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
                String callId = payload.get("call_id").toString();
                String callStatus = payload.get("call_status").toString();
                String direction = payload.get("direction").toString();
                String callerNumber = payload.get("caller_number").toString();
                String destinationNumber = payload.get("destination_number").toString();
                String startTime = payload.get("starttime").toString();
                String answerTime = payload.get("answertime").toString();
                Timestamp endTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payload.get("endtime").toString()).getTime());
                String hangupBy = payload.get("hangup_by").toString();
                String recordingUrl = payload.get("recording_url").toString();
                String transactionId = payload.get("transactionID").toString();
                int objectId = Integer.parseInt(payload.get("objectId").toString());
                String userName = payload.get("userName").toString();
                String eventType = payload.get("eventType").toString();

                CMCall cmCall = new CMCall();

                cmCall.setCallId(callId);
                cmCall.setCallStatus(callStatus);
                cmCall.setDirection(direction);
                cmCall.setCallerNumber(callerNumber);
                cmCall.setDestinationNumber(destinationNumber);
                cmCall.setStartTime(startTime);
                cmCall.setAnswerTime(answerTime);
                cmCall.setHangupBy(hangupBy);
                cmCall.setEndTime(endTime);
                cmCall.setRecordingUrl(recordingUrl);
                cmCall.setTransactionId(transactionId);
                cmCall.setObjectId(objectId);
                cmCall.setUserName(userName);
                cmCall.setEventType(eventType);

                CMCall callLog = cmCallService.saveCMCall(cmCall);
                System.out.println("Call Log: " + callLog);

                return ResponseEntity.ok("Data received and processed successfully");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in processing data: " + e.getMessage());
            }
        }

    @GetMapping("/v1/models/CM_CALL")
    public ResponseEntity<List<CMCall>> getAllCalls() {
        List<CMCall> calls = cmCallService.getAllCalls();
        return ResponseEntity.ok(calls);
    }
}
