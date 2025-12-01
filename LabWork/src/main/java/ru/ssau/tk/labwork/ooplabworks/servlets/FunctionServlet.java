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
import ru.ssau.tk.labwork.ooplabworks.service.FunctionService;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.service.UserService;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "FunctionServlet", urlPatterns = "/api/functions/*")
public class FunctionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(FunctionServlet.class);

    private DataSource ds;
    private FunctionService functionService;
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

        this.functionService = new FunctionService(ds);
        log.info("UserServlet initialized with DB: {}", ds);
    }

    // ------------------------ GET ------------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        // /api/functions?userId=...
        String userId = req.getParameter("userId");
        String name = req.getParameter("name");

        try {

            // GET by userId
            if (userId != null) {
                int uid = Integer.parseInt(userId);
                log.info("GET /api/functions?userId={}", uid);

                List<FunctionDTO> list = functionService.getByUserId(uid);
                writer.write(gson.toJson(list));
                return;
            }

            // GET by name
            if (name != null) {
                log.info("GET /api/functions?name={}", name);

                List<FunctionDTO> list = functionService.getByName(name);
                writer.write(gson.toJson(list));
                return;
            }

            // GET by id: /api/functions/4
            String path = req.getPathInfo();
            if (path == null || path.equals("/") || path.length() <= 1) {
                log.warn("GET invalid request: no ID");
                resp.setStatus(400);
                writer.write(error("Function ID required"));
                return;
            }

            int id = Integer.parseInt(path.substring(1));
            log.info("GET /api/functions/{}", id);

            FunctionDTO f = functionService.getById(id);
            if (f == null) {
                log.warn("Function not found id={}", id);
                resp.setStatus(404);
                writer.write(error("Function not found"));
                return;
            }

            writer.write(gson.toJson(f));

        } catch (Exception e) {
            log.error("GET /api/functions failed", e);
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
            int userId = json.get("userId").getAsInt();
            String name = json.get("name").getAsString();
            String signature = json.get("signature").getAsString();

            log.info("POST /api/functions userId={}, name={}", userId, name);

            FunctionDTO created = functionService.create(userId, name, signature);

            resp.setStatus(201);
            writer.write(gson.toJson(created));

        } catch (Exception e) {
            log.error("POST /api/functions failed", e);
            resp.setStatus(400);
            writer.write(error("Invalid input"));
        }
    }

    // ------------------------ PUT ------------------------
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("PUT invalid request: no ID");
            resp.setStatus(400);
            writer.write(error("Function ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        JsonObject body = readBody(req);

        try {
            log.info("PUT /api/functions/{}", id);

            String name = body.has("name") ? body.get("name").getAsString() : null;
            String signature = body.has("signature") ? body.get("signature").getAsString() : null;

            FunctionDTO updated = functionService.update(id, name, signature);

            if (updated == null) {
                log.warn("Function not found for update id={}", id);
                resp.setStatus(404);
                writer.write(error("Function not found"));
                return;
            }

            writer.write(gson.toJson(updated));

        } catch (Exception e) {
            log.error("PUT /api/functions failed id=" + id, e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    // ------------------------ DELETE ------------------------
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("DELETE invalid request: no ID");
            resp.setStatus(400);
            writer.write(error("Function ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            log.info("DELETE /api/functions/{}", id);

            boolean deleted = functionService.delete(id);

            if (!deleted) {
                log.warn("Function not found for delete id={}", id);
                resp.setStatus(404);
                writer.write(error("Function not found"));
                return;
            }

            resp.setStatus(204);

        } catch (Exception e) {
            log.error("DELETE /api/functions failed id=" + id, e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    // ------------------------ Utility ------------------------
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
