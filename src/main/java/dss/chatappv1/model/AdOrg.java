package dss.chatappv1.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdOrg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_org_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal adOrgId;

    @Column(name = "ad_client_id", precision = 10, scale = 0)
    private BigDecimal adClientId;

    @Column(name = "value", length = 40)
    private String value;
}
