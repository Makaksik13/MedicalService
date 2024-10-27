package MedicalService.demo.repository;

import MedicalService.demo.entity.icd.Icd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcdRepository extends JpaRepository<Icd, String> {
}
