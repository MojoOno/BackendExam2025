package dat.dto;

import dat.entities.SkiLesson;
import dat.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkiLessonNoInstructorDTO {
    private Long id;
    private String name;
    private Double price;
    private Level level;
    private LocalDate startTime;
    private LocalDate endTime;
    private String longitude;
    private String latitude;

    public SkiLessonNoInstructorDTO(SkiLesson skiLesson) {
        this.id = skiLesson.getId();
        this.name = skiLesson.getName();
        this.price = skiLesson.getPrice();
        this.level = skiLesson.getLevel();
        this.startTime = skiLesson.getStartTime();
        this.endTime = skiLesson.getEndTime();
        this.longitude = skiLesson.getLongitude();
        this.latitude = skiLesson.getLatitude();
    }
}

