package dss.chatappv1.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad_client")
public class AdClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_user_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adUserId;

    @Column(name = "ad_client_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adClientId;

    @Column(name = "ad_org_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adOrgId;

    @Column(name = "isactive", length = 1, nullable = false)
    private String isActive;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "createdby", precision = 10, scale = 0, nullable = false)
    private BigDecimal createdBy;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Column(name = "updatedby", precision = 10, scale = 0, nullable = false)
    private BigDecimal updatedBy;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "password", length = 1024)
    private String password;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "emailuser", length = 60)
    private String emailUser;

    @Column(name = "emailuserpw", length = 255)
    private String emailUserPw;

    @Column(name = "notificationtype", length = 1, nullable = false)
    private String notificationType;

    @Column(name = "ldapuser", length = 60)
    private String ldapUser;

    @Column(name = "value", length = 40)
    private String value;

    @Column(name = "ad_user_uu", length = 36)
    private String adUserUU;

    @Column(name = "salt", length = 16)
    private String salt;

    @Column(name = "islocked", length = 1)
    private String isLocked;

    @Column(name = "dateaccountlocl")
    private LocalDateTime dateAccountLocl;

    @Column(name = "failedlogincount")
    private Integer failedLoginCount;

    @Column(name = "datepasswordcl")
    private LocalDateTime datePasswordCl;

    @Column(name = "datelastlogin")
    private LocalDateTime dateLastLogin;

    @Column(name = "isexpired", length = 1)
    private String isExpired;

    @Column(name = "securityquestion", length = 1024)
    private String securityQuestion;

    @Column(name = "answer", length = 1024)
    private String answer;

    @Column(name = "sessionid", length = 60)
    private String sessionId;

    @Column(name = "ad_image_id", precision = 10, scale = 0)
    private BigDecimal adImageId;

    @Column(name = "isnoexpire", length = 1)
    private String isNoExpire;

    @Column(name = "isuseradmin", length = 1)
    private String isUserAdmin;

    @Column(name = "isusersystem",length = 1)
    private String isUserSystem;

    @Column(name = "hr_employee_id", precision = 10, scale = 0)
    private BigDecimal hrEmployeeId;

    @Column(name = "ischangeprice", length = 11)
    private String isChangePrice;

    @Column(name = "ismanylogin", length = 1)
    private String isManyLogin;



}
