package MedicalService.demo.model.entity.disease;

import MedicalService.demo.model.entity.icd.Icd;
import MedicalService.demo.model.entity.patient.Patient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "starting", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate starting;

    @Column(name = "ending")
    @Temporal(TemporalType.DATE)
    private LocalDate ending;

    @ManyToOne
    @JoinColumn(name = "icd_code", referencedColumnName = "code", nullable = false)
    private Icd icd;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @Column(name = "description", length = 2048, nullable = false)
    private String description;
}
