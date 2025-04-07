package dat.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dat.entities.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstructorDTO
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private Double yearsOfExperience;
    @JsonBackReference
    private Set<SkiLessonDTO> lessons = new HashSet<>();

    public InstructorDTO(Instructor instructor)
    {
        this.id = instructor.getId();
        this.firstName = instructor.getFirstName();
        this.lastName = instructor.getLastName();
        this.email = instructor.getEmail();
        this.phone = instructor.getPhone();
        this.yearsOfExperience = instructor.getYearsOfExperience();
        this.lessons = instructor.getSkiLessons().stream().map(SkiLessonDTO::new).collect(java.util.stream.Collectors.toSet());
    }
}
