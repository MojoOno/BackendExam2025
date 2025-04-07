package dat.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.dto.SkiLessonDTO;
import dat.enums.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SkiLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    @Enumerated(EnumType.STRING)
    private Level level;
    private LocalDate startTime;
    private LocalDate endTime;
    private String longitude;
    private String latitude;

    @ManyToOne
    @JsonManagedReference
    private Instructor instructor;


    public SkiLesson(String name)
    {
        this.name = name;
    }

    public SkiLesson(SkiLessonDTO skiLessonDTO)
    {
        this.name = skiLessonDTO.getName();
        this.price = skiLessonDTO.getPrice();
        this.level = skiLessonDTO.getLevel();
        this.startTime = skiLessonDTO.getStartTime();
        this.endTime = skiLessonDTO.getEndTime();
        this.longitude = skiLessonDTO.getLongitude();
        this.latitude = skiLessonDTO.getLatitude();
    }

}
