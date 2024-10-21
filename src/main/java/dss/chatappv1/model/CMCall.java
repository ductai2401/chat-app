package dss.chatappv1.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CM_Call")
public class CMCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cm_call_id", precision = 10, scale = 0)
    private BigDecimal cmCallId;

    @Column(name = "ad_client_id", precision = 10, scale = 0, nullable = true)
    private BigDecimal adClientId;

    @Column(name = "ad_org_id", precision = 10, scale = 0, nullable = true)
    private BigDecimal adOrgId;

    @Column(name = "ad_user_id", precision = 10, scale = 0, nullable = true)
    private BigDecimal adUserId;

    @Column(name = "callid")
    private String callId;

    @Column(name = "callstatus")
    private String callStatus;

    @Column(name = "direction")
    private String direction;

    @Column(name = "callernumber")
    private String callerNumber;

    @Column(name = "destinationnumber")
    private String destinationNumber;

    @Column(name = "starttime")
    private String startTime;

    @Column(name = "answertime")
    private String answerTime;

    @Column(name="endtime")
    private Timestamp endTime;
    @Column(name = "hangupby")
    private String hangupBy;

    @Column(name = "recordingurl")
    private String recordingUrl;

    @Column(name = "transactionid")
    private String transactionId;

    @Column(name = "objectid", nullable = true)
    private int objectId;

    @Column(name = "username")
    private String userName;

    @Column(name = "eventtype")
    private String eventType;

    @Column(name = "customername")
    String customerName;

    @Column(name = "createdby", precision = 10, scale = 0)
    private BigDecimal createdBy;

    @Column(name = "updatedby", precision = 10, scale = 0)
    private BigDecimal updatedBy;

}
