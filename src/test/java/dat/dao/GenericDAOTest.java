package dat.dao;

import dat.config.HibernateConfig;
import dat.entities.*;
import dat.exceptions.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenericDAOTest
{
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final GenericDAO genericDAO = new SkiLessonDAO(emf);
    private static Instructor h1, h2;
    private static SkiLesson r1, r2, r3, r4;


    @BeforeEach
    void setUp()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            r2 = new SkiLesson("A102");
            r3 = new SkiLesson("B101");
            r4 = new SkiLesson("B102");
            r1 = new SkiLesson("A101");
            h1 = new Instructor("Instructor A");
            h2 = new Instructor("Instructor B");
            h1.addLesson(r1);
            h1.addLesson(r2);
            h2.addLesson(r3);
            h2.addLesson(r4);
            em.getTransaction().begin();
                em.createQuery("DELETE FROM SkiLesson ").executeUpdate();
                em.createQuery("DELETE FROM Instructor ").executeUpdate();
                em.persist(h1);
                em.persist(h2);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            fail();
        }
    }

    @Test
    void getInstance()
    {
        assertNotNull(emf);
    }

    @Test
    void create()
    {
        // Arrange
        Instructor h3 = new Instructor("Instructor C");
        SkiLesson r5 = new SkiLesson("C101");
        r5.setInstructor(h3);
        h3.addLesson(r5);
        System.out.println("---- " + h3);


        // Act
        Instructor result = genericDAO.create(h3);
        System.out.println("---- " + result);
        System.out.println("---- " + result.getSkiLessons());
        System.out.println("---- " + r5);

        // Assert
        assertThat(result, samePropertyValuesAs(h3));
        assertNotNull(result);
        try (EntityManager em = emf.createEntityManager())
        {
            Instructor found = em.find(Instructor.class, result.getId());
            assertThat(found, samePropertyValuesAs(h3 ,"rooms"));
            Long amountInDb = em.createQuery("SELECT COUNT(t) FROM Instructor t", Long.class).getSingleResult();
            assertThat(amountInDb, is(3L));
        }

    }

    @Test
    void createMany()
    {
        // Arrange
        Instructor t3 = new Instructor("TestEntityC");
        Instructor t4 = new Instructor("TestEntityD");
        List<Instructor> testEntities = List.of(t3, t4);

        // Act
        List<Instructor> result = genericDAO.create(testEntities);

        // Assert
        assertThat(result.get(0), samePropertyValuesAs(t3, "rooms"));
        assertThat(result.get(1), samePropertyValuesAs(t4, "rooms"));
        assertNotNull(result);
        try (EntityManager em = emf.createEntityManager())
        {
            Long amountInDb = em.createQuery("SELECT COUNT(t) FROM Instructor t", Long.class).getSingleResult();
            assertThat(amountInDb, is(4L));
        }
    }

    @Test
    void read()
    {
        // Arrange
        Instructor expected = h1;

        // Act
        Instructor result = genericDAO.getById(Instructor.class, h1.getId());

        // Assert
        assertThat(result, samePropertyValuesAs(expected, "rooms"));
        //assertThat(result.getRooms(), containsInAnyOrder(expected.getRooms().toArray()));
    }

    @Test
    void read_notFound()
    {


        // Act
        DaoException exception = assertThrows(DaoException.class, () -> genericDAO.getById(Instructor.class, 1000L));
        //Instructor result = genericDAO.read(Instructor.class, 1000L);

        // Assert
        assertThat(exception.getMessage(), is("Error reading object from db"));
    }

    @Test
    void findAll()
    {
        // Arrange
        List<Instructor> expected = List.of(h1, h2);

        // Act
        List<Instructor> result = genericDAO.getAll(Instructor.class);

        // Assert
        assertNotNull(result);
        assertThat(result.size(), is(2));
        assertThat(result.get(0), samePropertyValuesAs(expected.get(0), "rooms"));
        assertThat(result.get(1), samePropertyValuesAs(expected.get(1), "rooms"));
    }

    @Test
    void update()
    {
        // Arrange
        h1.setFirstName("UpdatedName");

        // Act
        Instructor result = genericDAO.update(h1);

        // Assert
        assertThat(result, samePropertyValuesAs(h1, "rooms"));
        //assertThat(result.getRooms(), containsInAnyOrder(h1.getRooms()));

    }

    @Test
    void updateMany()
    {
        // Arrange
        h1.setFirstName("UpdatedName");
        h2.setFirstName("UpdatedName");
        List<Instructor> testEntities = List.of(h1, h2);

        // Act
        List<Instructor> result = genericDAO.update(testEntities);

        // Assert
        assertNotNull(result);
        assertThat(result.size(), is(2));
        assertThat(result.get(0), samePropertyValuesAs(h1, "rooms"));
        assertThat(result.get(1), samePropertyValuesAs(h2, "rooms"));
    }

    @Test
    void delete()
    {
        // Act
        genericDAO.delete(h1);

        // Assert
        try (EntityManager em = emf.createEntityManager())
        {
            Long amountInDb = em.createQuery("SELECT COUNT(t) FROM Instructor t", Long.class).getSingleResult();
            assertThat(amountInDb, is(1L));
            Instructor found = em.find(Instructor.class, h1.getId());
            assertNull(found);
        }
    }

    @Test
    void delete_byId()
    {
        // Act
        genericDAO.delete(Instructor.class, h2.getId());

        // Assert
        try (EntityManager em = emf.createEntityManager())
        {
            Long amountInDb = em.createQuery("SELECT COUNT(t) FROM Instructor t", Long.class).getSingleResult();
            assertThat(amountInDb, is(1L));
            Instructor found = em.find(Instructor.class, h2.getId());
            assertNull(found);
        }
    }
}