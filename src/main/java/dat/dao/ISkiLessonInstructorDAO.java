package dat.dao;

import dat.dto.InstructorSkiLessonTotalDTO;
import dat.entities.Instructor;
import dat.entities.SkiLesson;
import dat.enums.Level;

import java.util.List;

public interface ISkiLessonInstructorDAO
{
    SkiLesson getLessonById(Long id);
    void addInstructorToSkiLesson(Instructor instructor, SkiLesson skiLesson);
    SkiLesson removeLesson(Instructor instructor, SkiLesson skiLesson);
    List<SkiLesson> getSkiLessonsByInstructor(Instructor instructor);
    List<SkiLesson> filterByLevel(Level level);
    List<InstructorSkiLessonTotalDTO> getTotalLessonPricePerInstructor();
}
