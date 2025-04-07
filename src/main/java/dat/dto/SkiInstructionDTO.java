package dat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkiInstructionDTO {
    private String title;
    private String description;
    private String level;
    private int durationMinutes;
    private String createdAt;
    private String updatedAt;
}