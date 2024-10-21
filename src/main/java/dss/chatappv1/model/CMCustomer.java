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
@Table(name = "cm_customer")
public class CMCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cm_customer_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal cmCustomerId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "name")
    private String name;
}
