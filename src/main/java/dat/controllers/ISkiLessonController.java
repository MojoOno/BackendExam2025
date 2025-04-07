package dat.controllers;

import io.javalin.http.Context;

public interface ISkiLessonController
{
    void addLessonToInstructor(Context ctx);
    void filterLessonsByLevel(Context ctx);
    void getTotalLessonPricePerInstructor(Context ctx);
    void populate(Context ctx);
    void getInstructionsByLevel(Context ctx);
    void getTotalInstructionDurationByLessonId(Context ctx);
}
