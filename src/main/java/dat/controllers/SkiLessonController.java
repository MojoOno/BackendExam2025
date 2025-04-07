package dat.controllers;

import dat.config.HibernateConfig;
import dat.dao.SkiLessonDAO;
import dat.dto.*;
import dat.entities.Instructor;
import dat.entities.SkiLesson;
import dat.enums.Level;
import dat.exceptions.ApiException;
import dat.exceptions.DaoException;
import dat.service.SkiInstructionService;
import dat.utils.Populator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SkiLessonController implements IController, ISkiLessonController
{
    private final SkiLessonDAO dao;
    private final SkiInstructionService skiInstructionService = new SkiInstructionService();
    private static final Logger logger = LoggerFactory.getLogger(SkiLessonController.class);
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public SkiLessonController(EntityManagerFactory emf)
    {
        dao = new SkiLessonDAO(emf);
    }

    public SkiLessonController(SkiLessonDAO dao)
    {
        this.dao = dao;
    }

    // Get all lessons
    @Override
    public void getAll(Context ctx)
    {
        try
        {
            ctx.status(200).json(dao.getAllLessons());
        }
        catch (Exception ex)
        {
            logger.error("Error getting entities", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Get lesson by ID
    @Override
    public void getById(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i > 0, "id must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid id"));

            SkiLesson skiLesson = dao.getLessonById(id);

            if (skiLesson == null)
            {
                throw new BadRequestResponse("No skiLesson found with that ID");
            }

            SkiLessonDTO skiLessonDTO = new SkiLessonDTO(skiLesson);
            if (skiLesson.getInstructor() != null)
            {
                skiLessonDTO.setInstructor(new InstructorDTO(skiLesson.getInstructor()));
            }

            ctx.status(200).json(skiLessonDTO);
        }
        catch (Exception ex)
        {
            logger.error("Error getting entity", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Create a new lesson
    @Override
    public void create(Context ctx)
    {
        try
        {
            SkiLessonDTO incomingTrip = ctx.bodyAsClass(SkiLessonDTO.class);
            SkiLesson skiLesson = new SkiLesson(incomingTrip);
            SkiLesson createdSkiLesson = dao.createLesson(skiLesson);
            ctx.status(201).json(new SkiLessonDTO(createdSkiLesson));
        }
        catch (Exception ex)
        {
            logger.error("Error creating entity", ex);
            throw new ApiException(400, "Field ‘xxx’ is required");
        }
    }

    // Update an existing lesson
    @Override
    public void update(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i > 0, "id must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid id"));
            SkiLessonDTO incomingTrip = ctx.bodyAsClass(SkiLessonDTO.class);
            SkiLesson skiLessonToUpdate = dao.getLessonById(id);
            if (incomingTrip.getName() != null) skiLessonToUpdate.setName(incomingTrip.getName());
            if (incomingTrip.getPrice() != null) skiLessonToUpdate.setPrice(incomingTrip.getPrice());
            if (incomingTrip.getLevel() != null) skiLessonToUpdate.setLevel(incomingTrip.getLevel());
            if (incomingTrip.getStartTime() != null) skiLessonToUpdate.setStartTime(incomingTrip.getStartTime());
            if (incomingTrip.getEndTime() != null) skiLessonToUpdate.setEndTime(incomingTrip.getEndTime());
            if (incomingTrip.getLongitude() != null) skiLessonToUpdate.setLongitude(incomingTrip.getLongitude());
            if (incomingTrip.getLatitude() != null) skiLessonToUpdate.setLatitude(incomingTrip.getLatitude());

            SkiLesson updatedSkiLesson = dao.updateLesson(skiLessonToUpdate);
            SkiLessonDTO returnedTrip = new SkiLessonDTO(updatedSkiLesson);
            ctx.status(200).json(returnedTrip);
        }
        catch (DaoException ex)
        {
            logger.error("Error updating SkiLesson", ex);
            throw new ApiException(400, "Field ‘xxx’ is required");
        }
        catch (Exception ex)
        {
            logger.error("Error updating SkiLesson", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Delete a lesson by ID
    @Override
    public void delete(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i > 0, "id must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid id"));
            dao.deleteLesson(id);
            ctx.status(204);
        }
        catch (Exception ex)
        {
            logger.error("Error deleting SkiLesson", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Get all lessons for a given instructor
    @Override
    public void getLessonsByInstructor(@NotNull Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i > 0, "id must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid id"));
            Instructor instructor = dao.getById(Instructor.class, id);
            ctx.status(200).json(instructor.getSkiLessons());
        }
        catch (Exception ex)
        {
            logger.error("Error getting lessons", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Add a lesson to a instructor
    @Override
    public void addLessonToInstructor(@NotNull Context ctx)
    {
        try
        {
            logger.info("Adding skiLesson to instructor");

            long instructorId = ctx.pathParamAsClass("instructorId", Long.class)
                    .check(i -> i > 0, "instructorId must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid instructor id"));
            long lessonId = ctx.pathParamAsClass("lessonId", Long.class)
                    .check(i -> i > 0, "lessonId must be at least 0")
                    .getOrThrow((validator) -> new BadRequestResponse("Invalid skiLesson id"));

            logger.info("Instructor ID: {}, SkiLesson ID: {}", instructorId, lessonId);

            Instructor instructor = dao.getById(Instructor.class, instructorId);
            SkiLesson skiLesson = dao.getLessonById(lessonId);

            if (instructor == null || skiLesson == null)
            {
                throw new BadRequestResponse("Instructor or SkiLesson not found");
            }

            logger.info("Instructor details before adding skiLesson: {}", instructor);
            logger.info("SkiLesson details: {}", skiLesson);

            instructor.addLesson(skiLesson);
            skiLesson.setInstructor(instructor);

            dao.update(instructor);
            dao.update(skiLesson);

            logger.info("Instructor details after adding skiLesson: {}", instructor);
            ctx.status(200).json(instructor.getSkiLessons());
        }
        catch (Exception ex)
        {
            logger.error("Error adding lesson", ex);
            throw new ApiException(400, "Error adding lesson");
        }
    }

    // Filter lessons by level (via query param)
    @Override
    public void filterLessonsByLevel(Context ctx)
    {
        try
        {
            String levelParam = ctx.queryParam("level");
            if (levelParam == null)
            {
                throw new BadRequestResponse("Missing level parameter");
            }

            Level level;
            try
            {
                level = Level.valueOf(levelParam);
            }
            catch (IllegalArgumentException e)
            {
                throw new BadRequestResponse("Invalid level: " + levelParam);
            }

            List<SkiLesson> filteredCategory = dao.filterByLevel(level);
            List<SkiLessonNoInstructorDTO> skiLessonDTOS = filteredCategory.stream()
                    .map(SkiLessonNoInstructorDTO::new)
                    .collect(Collectors.toList());

            ctx.status(200).json(skiLessonDTOS);
        }
        catch (BadRequestResponse ex)
        {
            logger.error("Invalid level", ex);
            ctx.status(400).json(new ErrorMessage(ex.getMessage()));
        }
        catch (Exception ex)
        {
            logger.error("Error filtering lessons by level", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }


    // Return total lesson price per instructor
    @Override
    public void getTotalLessonPricePerInstructor(Context ctx)
    {
        try
        {
            List<InstructorSkiLessonTotalDTO> totals = dao.getTotalLessonPricePerInstructor();
            ctx.status(200).json(totals);
        }
        catch (Exception ex)
        {
            logger.error("Error calculating total lesson price by instructor", ex);
            throw new ApiException(404, "No content found for this request");
        }
    }

    // Populate the database with demo data
    @Override
    public void populate(Context ctx)
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            Populator populator = new Populator();
            populator.resetAndPersistEntities(em);
            ctx.status(200).result("Database populated successfully.");
        }
        catch (Exception e)
        {
            logger.error("Error populating database", e);
            ctx.status(500).result("Error populating database: " + e.getMessage());
        }
        finally
        {
            em.close();
        }
    }

    // fetching instructions by level from the ski instruction service
    public void getInstructionsByLevel(Context ctx) {
        try {
            String levelParam = ctx.pathParam("level");
            Level level = Level.valueOf(levelParam);

            List<SkiInstructionDTO> instructions = skiInstructionService.getInstructionsByLevel(level);

            ctx.status(200).json(instructions);
        } catch (IllegalArgumentException e) {
            throw new BadRequestResponse("Invalid level. Must be beginner, intermediate or advanced.");
        } catch (Exception e) {
            logger.error("Failed to get instructions by level", e);
            throw new ApiException(500, "Server error fetching instructions");
        }
    }

}
