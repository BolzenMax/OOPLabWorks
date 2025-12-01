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
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.service.FunctionService;
import ru.ssau.tk.labwork.ooplabworks.service.PointService;
import ru.ssau.tk.labwork.ooplabworks.service.UserService;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PointServlet", urlPatterns = "/api/points/*")
public class PointServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PointServlet.class);

    private DataSource ds;
    private PointService pointService;
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

        this.pointService = new PointService(ds);
        log.info("UserServlet initialized with DB: {}", ds);
    }

    // ------------------------ GET ------------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            String fIdParam = req.getParameter("functionId");
            if (fIdParam != null) {
                int functionId = Integer.parseInt(fIdParam);

                log.info("GET /api/points?functionId={}", functionId);

                List<PointDTO> list = pointService.getByFunctionId(functionId);
                writer.write(gson.toJson(list));
                return;
            }

            // GET /api/points/{id}
            String path = req.getPathInfo();
            if (path == null || path.equals("/") || path.length() <= 1) {
                log.warn("GET invalid: no ID provided");
                resp.setStatus(400);
                writer.write(error("Point ID required"));
                return;
            }

            int id = Integer.parseInt(path.substring(1));

            log.info("GET /api/points/{}", id);

            PointDTO p = pointService.getById(id);
            if (p == null) {
                log.warn("Point not found id={}", id);
                resp.setStatus(404);
                writer.write(error("Point not found"));
                return;
            }

            writer.write(gson.toJson(p));

        } catch (Exception e) {
            log.error("GET /api/points failed", e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    // ------------------------ POST ------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonObject json = read(req);

        try {
            int functionId = json.get("functionId").getAsInt();
            double x = json.get("x").getAsDouble();
            double y = json.get("y").getAsDouble();

            log.info("POST /api/points functionId={}, x={}, y={}", functionId, x, y);

            PointDTO created = pointService.create(functionId, x, y);

            resp.setStatus(201);
            writer.write(gson.toJson(created));

        } catch (Exception e) {
            log.error("POST /api/points failed", e);
            resp.setStatus(400);
            writer.write(error("Invalid point data"));
        }
    }

    // ------------------------ PUT ------------------------
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonObject json = read(req);

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("PUT invalid: no ID provided");
            resp.setStatus(400);
            writer.write(error("Point ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            double x = json.get("x").getAsDouble();
            double y = json.get("y").getAsDouble();

            log.info("PUT /api/points/{} x={}, y={}", id, x, y);

            PointDTO updated = pointService.update(id, x, y);

            if (updated == null) {
                log.warn("Point not found for update id={}", id);
                resp.setStatus(404);
                writer.write(error("Point not found"));
                return;
            }

            writer.write(gson.toJson(updated));

        } catch (Exception e) {
            log.error("PUT /api/points failed id=" + id, e);
            resp.setStatus(400);
            writer.write(error("Invalid point data"));
        }
    }

    // ------------------------ DELETE ------------------------
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.length() <= 1) {
            log.warn("DELETE invalid: no ID provided");
            resp.setStatus(400);
            writer.write(error("Point ID required"));
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            log.info("DELETE /api/points/{}", id);

            boolean deleted = pointService.delete(id);

            if (!deleted) {
                log.warn("Point not found for delete id={}", id);
                resp.setStatus(404);
                writer.write(error("Point not found"));
                return;
            }

            resp.setStatus(204);

        } catch (Exception e) {
            log.error("DELETE /api/points failed id=" + id, e);
            resp.setStatus(500);
            writer.write(error("Internal server error"));
        }
    }

    private JsonObject read(HttpServletRequest req) throws IOException {
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
