package dat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstructorSkiLessonTotalDTO
{
    private Long instructorId;
    private Double totalPrice;
}

