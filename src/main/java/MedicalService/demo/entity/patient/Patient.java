package MedicalService.demo.entity.patient;

import MedicalService.demo.entity.disease.Disease;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "surname", length = 128, nullable = false)
    private String surname;

    @Column(name = "patronymic", length = 128, nullable = false)
    @ColumnDefault("0")
    private String patronymic;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @Column(name = "policy_number", length = 16, nullable = false, unique = true)
    private String policyNumber;

    @OneToMany(mappedBy = "patientId")
    private List<Disease> diseases;
}
