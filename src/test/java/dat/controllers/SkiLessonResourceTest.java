package dat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dto.SkiLessonDTO;
import dat.entities.SkiLesson;
import dat.enums.Level;
import dat.routes.Routes;
import dat.utils.Populator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SkiLessonResourceTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final Logger logger = LoggerFactory.getLogger(SkiLessonResourceTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SkiLesson lesson1, lesson2;
    private String token;

    @BeforeAll
    void setUpAll() {
        SkiLessonController skiLessonController = new SkiLessonController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(securityController, skiLessonController);

        ApplicationConfig.getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7078);

        RestAssured.baseURI = "http://localhost:7078/api";
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            Populator populator = new Populator();
            populator.resetAndPersistEntities(em);
        }

        try (EntityManager em = emf.createEntityManager()) {
            List<SkiLesson> skiLessons = em.createQuery("SELECT i FROM SkiLesson i", SkiLesson.class).getResultList();
            Assertions.assertFalse(skiLessons.isEmpty(), "Lessons should not be empty after population");
            lesson1 = skiLessons.get(0);
            lesson2 = skiLessons.get(1);
        }

        token = registerAndLoginTestUser();
    }

    private String registerAndLoginTestUser() {
        String username = "testuser";
        String password = "secret";

        String registerJson = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        given()
                .contentType(ContentType.JSON)
                .body(registerJson)
                .post("/auth/register")
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(422)));

        String loginJson = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void getAll() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/skilessons")
                .then()
                .statusCode(200)
                .body("size()", equalTo(6));
    }

    @Test
    void getById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/skilessons/" + lesson2.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(lesson2.getId().intValue()));
    }

    @Test
    void create() {
        SkiLessonDTO newLesson = new SkiLessonDTO();
        newLesson.setName("Test SkiLesson");
        newLesson.setPrice(1234.56);
        newLesson.setLevel(Level.beginner);
        newLesson.setLongitude("Test location");

        try {
            String json = objectMapper.writeValueAsString(newLesson);

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(json)
                    .post("/skilessons")
                    .then()
                    .statusCode(201)
                    .body("name", equalTo("Test SkiLesson"));

        } catch (Exception e) {
            logger.error("Error creating lesson", e);
            Assertions.fail();
        }
    }

    @Test
    void update() {
        SkiLessonDTO update = new SkiLessonDTO();
        update.setName("Updated Name");

        try {
            String json = objectMapper.writeValueAsString(update);

            given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(json)
                    .put("/skilessons/" + lesson1.getId())
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Updated Name"));

        } catch (Exception e) {
            logger.error("Error updating lesson", e);
            Assertions.fail();
        }
    }

    @Test
    void delete() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/skilessons/" + lesson1.getId())
                .then()
                .statusCode(204);
    }

    @Test
    void filterLessonsByLevel() {
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("level", "beginner")
                .when()
                .get("/skilessons/level")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void addLessonToInstructor() {
        // Remove instructor from lesson2 and reassign it via endpoint
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            SkiLesson skiLesson = em.find(SkiLesson.class, lesson2.getId());
            skiLesson.setInstructor(null);
            em.merge(skiLesson);
            em.getTransaction().commit();
        }

        Long instructorId = lesson1.getInstructor().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/skilessons/" + lesson2.getId() + "/instructors/" + instructorId)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Lesson should now be added to instructor's lesson list
    }

    @Test
    void getTotalLessonPricePerInstructor() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/skilessons/totalprice")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0]", hasKey("instructorId"))
                .body("[0]", hasKey("totalPrice"));
    }

    @Test
    void populateDatabase() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/skilessons/populate")
                .then()
                .statusCode(200)
                .body(containsString("Database populated successfully."));
    }

}
