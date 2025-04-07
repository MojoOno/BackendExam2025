package dat.dao;

import dat.dto.InstructorSkiLessonTotalDTO;
import dat.entities.Instructor;
import dat.entities.SkiLesson;
import dat.enums.Level;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkiLessonDAO extends GenericDAO implements ISkiLessonInstructorDAO
{

    public SkiLessonDAO(EntityManagerFactory emf)
    {
        super(emf);
    }

    // Get all lessons
    public List<SkiLesson> getAllLessons()
    {
        return super.getAll(SkiLesson.class);
    }

    // Get a single lesson by ID
    public SkiLesson getLessonById(Long id)
    {
        return super.getById(SkiLesson.class, id);
    }

    // Create a new skiLesson
    public SkiLesson createLesson(SkiLesson skiLesson)
    {
        return super.create(skiLesson);
    }

    // Update an existing skiLesson
    public SkiLesson updateLesson(SkiLesson skiLesson)
    {
        return super.update(skiLesson);
    }

    // Delete a lesson by ID
    public void deleteLesson(Long id)
    {
        super.delete(SkiLesson.class, id);
    }

    // Get all lessons for a specific instructor
    @Override
    public List<SkiLesson> getSkiLessonsByInstructor(Instructor instructor)
    {
        return instructor.getSkiLessons();
    }

    // Assign an instructor to a skiLesson
    @Override
    public void addInstructorToSkiLesson(Instructor instructor, SkiLesson skiLesson)
    {
        instructor.addLesson(skiLesson);
        update(instructor);
    }

    // Remove a skiLesson from an instructor and update both entities
    @Override
    public SkiLesson removeLesson(Instructor instructor, SkiLesson skiLesson)
    {
        instructor.removeLesson(skiLesson);
        skiLesson.setInstructor(null);
        update(instructor);
        return update(skiLesson);
    }

    // Filter lessons by a specific level
    public List<SkiLesson> filterByLevel(Level level)
    {
        List<SkiLesson> skiLessons = super.getAll(SkiLesson.class);
        return skiLessons.stream()
                .filter(item -> level.equals(item.getLevel()))
                .collect(Collectors.toList());
    }

    // Calculate the total lesson price per instructor and return as a list of DTOs
    public List<InstructorSkiLessonTotalDTO> getTotalLessonPricePerInstructor()
    {
        List<Instructor> instructors = getAll(Instructor.class);
        List<InstructorSkiLessonTotalDTO> result = new ArrayList<>();
        for (Instructor instructor : instructors)
        {
            double total = instructor.getSkiLessons().stream()
                    .mapToDouble(SkiLesson::getPrice)
                    .sum();
            result.add(new InstructorSkiLessonTotalDTO(instructor.getId(), total));
        }
        return result;
    }
}
