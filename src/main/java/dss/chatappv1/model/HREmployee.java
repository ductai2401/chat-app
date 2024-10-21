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
@Table(name = "hr_employee")
public class HREmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hr_employee_id", precision = 10, scale = 0, nullable = false)
    private BigDecimal hrEmployeeId;

    @Column(name = "name")
    private String name;
    @Column(name = "extension")
    private String extension;
}