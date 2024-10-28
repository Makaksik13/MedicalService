package MedicalService.demo.model.entity.icd;

import com.opencsv.bean.CsvBindByPosition;
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
public class Icd {

    @Id
    @Column(name = "code", length = 5)
    @CsvBindByPosition(position = 2)
    private String code;

    @Column(name = "name", length = 256, nullable = false)
    @CsvBindByPosition(position = 3)
    private String name;

}
