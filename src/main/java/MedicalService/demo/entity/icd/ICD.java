package MedicalService.demo.entity.icd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "icd")
public class ICD {

    @Id
    private String code;

    @Column(name = "name", length = 256, nullable = false)
    private String name;
}
