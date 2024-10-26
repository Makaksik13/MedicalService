package MedicalService.demo.repository;

import MedicalService.demo.entity.icd.ICD;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICDRepository extends CrudRepository<ICD, String> {
}
