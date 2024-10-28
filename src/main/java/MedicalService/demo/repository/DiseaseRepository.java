package MedicalService.demo.repository;

import MedicalService.demo.model.entity.disease.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Optional<Disease> findByIdAndPatientId(long diseaseId, long patientId);
}
