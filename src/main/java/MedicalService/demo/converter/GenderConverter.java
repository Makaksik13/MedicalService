package MedicalService.demo.converter;

import MedicalService.demo.entity.patient.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(Character character) {
        if (character == null) {
            return null;
        }

        return Stream.of(Gender.values())
                .filter(c -> c.getCode().equals(character))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
