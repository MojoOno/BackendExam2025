package dat.config;

import dat.controllers.ISecurityController;
import dat.controllers.SecurityController;
import dat.dto.ErrorMessage;
import dat.exceptions.ApiException;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.config.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApplicationConfig
{
    private static ApplicationConfig instance;
    private static Javalin app;
    private static JavalinConfig javalinConfig;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final ISecurityController securityController = new SecurityController();

    private ApplicationConfig()
    {
    }

    public static ApplicationConfig getInstance()
    {
        if (instance == null)
        {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    // Initialize and configure the Javalin server
    public ApplicationConfig initiateServer()
    {
        app = Javalin.create(config ->
        {
            javalinConfig = config;
            config.showJavalinBanner = false;
            config.http.defaultContentType = "application/json";
            config.router.contextPath = "/api";
            config.bundledPlugins.enableRouteOverview("/routes"); // Show route overview at /api/routes
            config.bundledPlugins.enableDevLogging(); // Enable dev logging for incoming requests
        });
        logger.info("Server initiated");
        return instance;
    }

    // Register application routes using the apiBuilder
    public ApplicationConfig setRoute(EndpointGroup routes)
    {
        javalinConfig.router.apiBuilder(routes);
        logger.info("Routes set");
        return instance;
    }

    // Apply security role checks before each matched route
    public ApplicationConfig checkSecurityRoles()
    {
        app.beforeMatched(securityController::accessHandler); // Authenticate and authorize
        return instance;
    }

    // Custom exception handler for ApiException (used across the app)
    public ApplicationConfig setApiExceptionHandling()
    {
        // May be overridden by the generic exception handler below
        app.exception(ApiException.class, (e, ctx) ->
        {
            logger.error("ApiException: {}", e.getMessage());
            int statusCode = e.getCode();
            ctx.status(statusCode).json(new ErrorMessage(statusCode, e.getMessage()));
        });
        return instance;
    }

    // Global fallback exception handler for unexpected errors
    public ApplicationConfig handleException()
    {
        app.exception(Exception.class, (e, ctx) ->
        {
            logger.error("Exception occurred", e); // Logs the full stack trace

            // Return basic debug info in JSON (remove or limit in production)
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "message", e.getMessage(),
                    "stackTrace", e.getStackTrace()[0].toString()
            ));
        });

        logger.info("ExceptionHandler initiated");
        return instance;
    }

    // Start the server on the specified port
    public void startServer(int port)
    {
        app.start(port);
        logger.info("Server started on port: {}", port);
    }

    // Stop the server (for shutdown or test teardown)
    public ApplicationConfig stopServer()
    {
        app.stop();
        logger.info("Server stopped");
        return instance;
    }
}
