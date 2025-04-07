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
public class SkiLessonDTO
{
    private Long id;
    private String name;
    private Double price;
    private Level level;
    private LocalDate startTime;
    private LocalDate endTime;
    private String longitude;
    private String latitude;
    private InstructorDTO instructor;

    public SkiLessonDTO(SkiLesson skiLesson)
    {
        this.id = skiLesson.getId();
        this.name = skiLesson.getName();
        this.price = skiLesson.getPrice();
        this.level = skiLesson.getLevel();
        this.startTime = skiLesson.getStartTime();
        this.endTime = skiLesson.getEndTime();
        this.longitude = skiLesson.getLongitude();
        this.latitude = skiLesson.getLatitude();
    }

    public void setInstructor(InstructorDTO instructor)
    {
        this.instructor = instructor;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    public void setStartTime(LocalDate startTime)
    {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDate endTime)
    {
        this.endTime = endTime;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }
}
