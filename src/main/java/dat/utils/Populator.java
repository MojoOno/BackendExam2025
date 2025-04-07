package dat.utils;

import dat.entities.Instructor;
import dat.entities.SkiLesson;
import dat.enums.Level;
import jakarta.persistence.EntityManager;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Populator
{
    private final Instructor jonSnow;
    private final Instructor aryaStark;
    private final Instructor tyrionLannister;
    private final SkiLesson skiLesson1;
    private final SkiLesson skiLesson2;
    private final SkiLesson skiLesson3;
    private final SkiLesson skiLesson4;
    private final SkiLesson skiLesson5;
    private final SkiLesson skiLesson6;

    public Populator()
    {
        jonSnow = new Instructor("Jon", "Snow", "jon.snow@thewall.com");
        jonSnow.setYearsOfExperience(5.0);
        jonSnow.setPhone(11112222);

        aryaStark = new Instructor("Arya", "Stark", "needle.master@winterfell.com");
        aryaStark.setYearsOfExperience(3.0);
        aryaStark.setPhone(22223333);

        tyrionLannister = new Instructor("Tyrion", "Lannister", "hand@casterlyrock.com");
        tyrionLannister.setYearsOfExperience(2.0);
        tyrionLannister.setPhone(33334444);

        skiLesson1 = new SkiLesson(null, "Beyond the Wall", 1500.0, Level.advanced,
                LocalDate.now(), LocalDate.now().plusDays(2), "-75.1234", "60.5678", jonSnow);

        skiLesson2 = new SkiLesson(null, "Night Watch Basics", 800.0, Level.beginner,
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4), "-75.1250", "60.5699", jonSnow);

        skiLesson3 = new SkiLesson(null, "Assassin Agility", 1200.0, Level.intermediate,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(4), "12.3456", "55.1234", aryaStark);

        skiLesson4 = new SkiLesson(null, "Silent Steps", 1000.0, Level.beginner,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(9), "12.3466", "55.1244", aryaStark);

        skiLesson5 = new SkiLesson(null, "Wine & Wisdom", 600.0, Level.beginner,
                LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(1).plusDays(1), "10.5678", "50.1234", tyrionLannister);

        skiLesson6 = new SkiLesson(null, "Advanced Slope Politics", 1300.0, Level.advanced,
                LocalDate.now().plusWeeks(2), LocalDate.now().plusWeeks(2).plusDays(1), "10.5688", "50.1244", tyrionLannister);

        jonSnow.addLesson(skiLesson1);
        jonSnow.addLesson(skiLesson2);
        aryaStark.addLesson(skiLesson3);
        aryaStark.addLesson(skiLesson4);
        tyrionLannister.addLesson(skiLesson5);
        tyrionLannister.addLesson(skiLesson6);
    }

    public Map<String, Object> getEntities()
    {
        Map<String, Object> entities = new HashMap<>();
        entities.put("jonSnow", jonSnow);
        entities.put("aryaStark", aryaStark);
        entities.put("tyrionLannister", tyrionLannister);
        entities.put("skiLesson1", skiLesson1);
        entities.put("skiLesson2", skiLesson2);
        entities.put("skiLesson3", skiLesson3);
        entities.put("skiLesson4", skiLesson4);
        entities.put("skiLesson5", skiLesson5);
        entities.put("skiLesson6", skiLesson6);
        return entities;
    }

    public void resetAndPersistEntities(EntityManager em)
    {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SkiLesson").executeUpdate();
        em.createQuery("DELETE FROM Instructor").executeUpdate();
        em.createQuery("DELETE FROM UserAccount").executeUpdate();

        em.persist(jonSnow);
        em.persist(aryaStark);
        em.persist(tyrionLannister);

        em.persist(skiLesson1);
        em.persist(skiLesson2);
        em.persist(skiLesson3);
        em.persist(skiLesson4);
        em.persist(skiLesson5);
        em.persist(skiLesson6);

        em.getTransaction().commit();
    }
}
