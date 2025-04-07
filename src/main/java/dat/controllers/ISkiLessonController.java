package dat.controllers;

import io.javalin.http.Context;

public interface ISkiLessonController
{
    void getLessonsByInstructor(Context context);
    void addLessonToInstructor(Context context);
    void filterLessonsByLevel(Context ctx);
    void getTotalLessonPricePerInstructor(Context ctx);
    void populate(Context ctx);
}
