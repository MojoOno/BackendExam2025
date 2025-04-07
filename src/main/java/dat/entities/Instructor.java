package dat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dat.dto.InstructorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Instructor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private Double yearsOfExperience;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "instructor", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonBackReference
    @ToString.Exclude
    private List<SkiLesson> skiLessons = new ArrayList<>();

    public Instructor(String firstName)
    {
        this.firstName = firstName;
    }

    public Instructor(String firstName, String lastName, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Instructor(InstructorDTO instructorDTO)
    {
        this.firstName = instructorDTO.getFirstName();
        this.lastName = instructorDTO.getLastName();
        this.email = instructorDTO.getEmail();
        this.phone = instructorDTO.getPhone();
        this.yearsOfExperience = instructorDTO.getYearsOfExperience();
        this.skiLessons = instructorDTO.getLessons().stream().map(SkiLesson::new).toList();
    }

    public void addLesson(SkiLesson skiLesson)
    {
        if (skiLesson != null)
        {
            skiLessons.add(skiLesson);
            skiLesson.setInstructor(this);
        }
    }

    public void removeLesson(SkiLesson skiLesson)
    {
        if (skiLesson != null)
        {
            skiLessons.remove(skiLesson);
            skiLesson.setInstructor(null);
        }
    }


}
