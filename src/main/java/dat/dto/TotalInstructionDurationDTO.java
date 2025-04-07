package dat.dto;

import dat.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalInstructionDurationDTO {
    private Long lessonId;
    private Level level;
    private int totalDurationMinutes;
}
