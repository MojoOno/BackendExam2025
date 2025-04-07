package dat.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.SkiLessonController;
import dat.controllers.SecurityController;
import dat.enums.Roles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{
    private final SecurityController securityController;
    private final SkiLessonController skiLessonController;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public Routes(SecurityController securityController, SkiLessonController skiLessonController)
    {
        this.skiLessonController = skiLessonController;
        this.securityController = securityController;
    }

    public EndpointGroup getRoutes()
    {
        return () ->
        {
            path("skilessons", SkiLessonRoutes());
            path("auth", authRoutes());
            path("protected", protectedRoutes());
        };
    }

    private EndpointGroup SkiLessonRoutes()
    {
        return () ->
        {
            get(skiLessonController::getAll, Roles.ANYONE);
            get("/level", skiLessonController::filterLessonsByLevel, Roles.ANYONE);
            get("/totalprice", skiLessonController::getTotalLessonPricePerInstructor, Roles.ANYONE);
            get("/{id}", skiLessonController::getById, Roles.ANYONE);
            get("/instructions/{level}", skiLessonController::getInstructionsByLevel, Roles.ANYONE);
            post(skiLessonController::create, Roles.ADMIN);
            put("/{id}", skiLessonController::update, Roles.ADMIN);
            delete("/{id}", skiLessonController::delete, Roles.ADMIN);
            put("/{lessonId}/instructors/{instructorId}", skiLessonController::addLessonToInstructor, Roles.ANYONE);
            post("/populate", skiLessonController::populate, Roles.ANYONE);
        };
    }

    private EndpointGroup authRoutes()
    {
        return () ->
        {
            get("/test", ctx -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from Open")), Roles.ANYONE);
            get("/healthcheck", securityController::healthCheck, Roles.ANYONE);
            post("/login", securityController::login, Roles.ANYONE);
            post("/register", securityController::register, Roles.ANYONE);
            get("/verify", securityController::verify, Roles.ANYONE);
            get("/tokenlifespan", securityController::timeToLive, Roles.ANYONE);
        };
    }

    private EndpointGroup protectedRoutes()
    {
        return () ->
        {
            get("/user_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from USER Protected")), Roles.USER);
            get("/admin_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from ADMIN Protected")), Roles.ADMIN);
        };
    }

}
