package dss.chatappv1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "ad_user")
public class AdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_user_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adUserId;

    @Column(name = "ad_client_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adClientId;

    @Column(name = "ad_org_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adOrgId;

    @Column(name = "isactive", length = 1)
    private String isActive;

    @Column(name = "created", nullable = false)
    private Timestamp created;

    @Column(name = "createdby", precision = 10, scale = 0, nullable = false)
    private BigDecimal createdBy;

    @Column(name = "updated", nullable = false)
    private Timestamp updated;

    @Column(name = "updatedby", precision = 10, scale = 0, nullable = false)
    private BigDecimal updatedBy;

    @Column(name = "name", length = 60)
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

    @Column(name = "notificationtype", length = 1)
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

    @Column(name = "dateaccountlocked")
    private Timestamp dateAccountLocked;

    @Column(name = "failedlogincount", precision = 10, scale = 0)
    private Integer failedLoginCount;

    @Column(name = "datepasswordchanged")
    private Timestamp datePasswordChanged;

    @Column(name = "datelastlogin")
    private Timestamp dateLastLogin;

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

    @Column(name = "isusersystem", length = 1)
    private String isUserSystem;

    @Column(name = "hr_employee_id", precision = 10, scale = 0)
    private BigDecimal hrEmployeeId;

    @Column(name = "ischangeprice", length = 1)
    private String isChangePrice;

    @Column(name = "ismanylogin", length = 1)
    private String isManyLogin;

    @Column(name = "ad_department_id", precision = 10, scale = 0)
    private BigDecimal adDepartmentId;

    @Column(name = "isallorg", length = 1)
    private String isAllOrg;

    @Column(name = "isshowacct", length = 1)
    private String isShowAcct;

    @Column(name = "isshowprice", length = 1)
    private String isShowPrice;

    @Column(name = "isdragdropmenu", length = 1)
    private String isDragDropMenu;

    @Column(name = "iscanexport", length = 1)
    private String isCanExport;

    @Column(name = "isconfigacct", length = 1)
    private String isConfigAcct;

    @Column(name = "token", length = 1000)
    private String token;

    @Column(name = "usertype", length = 1)
    private String userType;

    @Column(name = "duration", precision = 10,scale = 0)
    private BigDecimal duration;

    @Column(name = "managerType")
    private String managerType;

    @Column(name = "isdeleted", precision = 1)
    private String isDeleted;

    @Column(name = "extension",precision = 10)
    private String extension;

}
