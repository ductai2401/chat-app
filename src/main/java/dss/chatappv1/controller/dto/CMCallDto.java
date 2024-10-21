package dss.chatappv1.controller.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMCallDto implements Serializable {

    private String callId;

    private String callStatus;

    private String direction;

    private String callerNumber;

    private String destinationNumber;

    private String startTime;

    private String answerTime;

    private String hangupBy;

    private String recordingUrl;

    private String transactionId;

    private int objectId;

    private String userName;

    private String eventType;
}
