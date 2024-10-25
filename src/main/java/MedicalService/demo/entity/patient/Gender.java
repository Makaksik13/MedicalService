package MedicalService.demo.entity.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE('M'), FEMALE('F');

    private final Character code;

}
