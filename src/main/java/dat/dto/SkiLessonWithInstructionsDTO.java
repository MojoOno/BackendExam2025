package dat.dto;

import dat.entities.SkiLesson;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SkiLessonWithInstructionsDTO extends SkiLessonDTO {
    private List<SkiInstructionDTO> instructions;

    public SkiLessonWithInstructionsDTO(SkiLesson lesson, List<SkiInstructionDTO> instructions) {
        super(lesson);
        this.instructions = instructions;
    }
}