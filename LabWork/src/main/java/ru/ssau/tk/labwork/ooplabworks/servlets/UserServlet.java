package ru.ssau.tk.labwork.ooplabworks.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.service.UserService;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/api/users/*")
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private DataSource ds;
    private UserService userService;
    private final Gson gson = new Gson();
    @Override
    public void init() throws ServletException {
        log.info("Initializing UserServlet…");

        ServletContext context = getServletContext();

        DataSource ds = (DataSource) context.getAttribute("dataSource");

        if (ds == null) {
            log.error("dataSource not found in ServletContext! Falling back to env vars.");
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");

            if (url == null) {
                url = "jdbc:postgresql://localhost:5432/lab";
                user = "postgres";
                pass = "postgres";
            }

            ds = DataSourceConfig.create(url, user, pass);

        }

        this.userService = new UserService(ds);
        log.info("UserServlet initialized with DB: {}", ds);
    }

    // ------------------------ GET ------------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            // GET /api/users?login=admin
            String loginParam = req.getParameter("login");
            if (loginParam != null) {
                log.info("GET /api/users?login={}", loginParam);
                UserDTO user = userService.getByLogin(loginParam);

                if (user == null) {
                    log.warn("User not found login={}", loginParam);
                    resp.setStatus(404);
                    writer.write(error("User not found"));
                    return;
                }

                writer.write(gson.toJson(user));
                return;
            }

            // GET /api/users?sortedBy=login
            String sortedParam = req.getParameter("sortedBy");
            if (sortedParam != null) {
                log.info("GET /api/users?sortedBy={}", sortedParam);

                try {
                    List<UserDTO> list = userService.getAllSortedBy(sortedParam);
                    writer.write(gson.toJson(list));
                    return;
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid sort field: {}", sortedParam);
                    resp.setStatus(400);
                    writer.write(error("Invalid sort field"));
                    return;
                }
            }

            // GET /api/users/{id}
            String path = req.getPathInfo();
            if (path == null || path.equals("/") || path.length() <= 1) {
                log.info("GET /api/users – return all users");
                List<UserDTO> users = userService.getAll();
                writer.write(gson.toJson(users));
                return;
            }

            int id = Integer.parseInt(path.substring(1));
            log.info("GET /api/users/{}", id);

            UserDTO user = userService.getById(id);
            if (user == null) {
                log.warn("User not found id={}", id);
                resp.setStatus(404);
                writer.write(error("User not found"));
                return;
            }

            writer.write(gson.toJson(user));

        } catch (Exception e) {
            log.error("GET /api/users failed", e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    // ------------------------ POST ------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonObject json = readBody(req);

        try {
            String login = json.get("login").getAsString();
            String role = json.get("role").getAsString();
            String password = json.get("password").getAsString();
            boolean enabled = json.get("enabled").getAsBoolean();

            log.info("POST /api/users login={}", login);

            UserDTO created = userService.create(login, role, password, enabled);

            resp.setStatus(201);
            writer.write(gson.toJson(created));

        } catch (Exception e) {
            log.error("POST /api/users failed", e);
            resp.setStatus(400);
            writer.write(error("Invalid user data"));
        }
    }

    // ------------------------ PUT ------------------------
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonObject json = readBody(req);

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("PUT invalid request: no ID");
            resp.setStatus(400);
            writer.write(error("User ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            log.info("PUT /api/users/{}", id);

            String password = json.has("password") ? json.get("password").getAsString() : null;
            String role = json.has("role") ? json.get("role").getAsString() : null;
            Boolean enabled = json.has("enabled") ? json.get("enabled").getAsBoolean() : null;

            UserDTO updated = userService.update(id, password, role, enabled);

            if (updated == null) {
                log.warn("User not found for update id={}", id);
                resp.setStatus(404);
                writer.write(error("User not found"));
                return;
            }

            writer.write(gson.toJson(updated));

        } catch (Exception e) {
            log.error("PUT /api/users failed id=" + id, e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("DELETE invalid request: no ID");
            resp.setStatus(400);
            writer.write(error("User ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            log.info("DELETE /api/users/{}", id);

            boolean deleted = userService.delete(id);

            if (!deleted) {
                log.warn("User not found for delete id={}", id);
                resp.setStatus(404);
                writer.write(error("User not found"));
                return;
            }

            resp.setStatus(204);

        } catch (Exception e) {
            log.error("DELETE /api/users failed id=" + id, e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    private JsonObject readBody(HttpServletRequest req) throws IOException {
        BufferedReader r = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) sb.append(line);
        return gson.fromJson(sb.toString(), JsonObject.class);
    }

    private String error(String msg) {
        return "{\"error\": \"" + msg + "\"}";
    }
}
