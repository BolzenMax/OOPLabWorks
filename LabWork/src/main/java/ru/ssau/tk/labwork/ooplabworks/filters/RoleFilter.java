package ru.ssau.tk.labwork.ooplabworks.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.service.FunctionService;

import java.io.IOException;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = "/api/*")
public class RoleFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RoleFilter.class);
    private FunctionService functionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializing RoleFilter");

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        if (url == null) {
            url = "jdbc:postgresql://localhost:5432/lab";
            username = "postgres";
            password = "postgres";
        }

        var ds = DataSourceConfig.create(url, username, password);
        this.functionService = new FunctionService(ds);
        log.info("RoleFilter initialized with FunctionService (DB: {})", url);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        UserDTO user = (UserDTO) request.getAttribute("currentUser");
        if (user == null) {
            log.warn("Access attempt without authenticated user");
            deny(response, "Authentication required (set by BasicAuthFilter)", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = request.getServletPath();
        String method = request.getMethod();
        log.debug("Checking access: user={}, role={}, method={}, path={}", user.getLogin(), user.getRole(), method, path);


        if (path.startsWith("/api/users")) {
            if (!"ADMIN".equals(user.getRole())) {
                log.warn("Non-ADMIN user '{}' attempted to access /api/users", user.getLogin());
                deny(response, "Only ADMIN can manage users", HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if (path.startsWith("/api/functions")) {
            if ("GET".equals(method)) {
                String userIdParam = request.getParameter("userId");
                if (userIdParam != null && !userIdParam.isEmpty()) {
                    try {
                        int targetUserId = Integer.parseInt(userIdParam);
                        if (targetUserId != user.getId() && !"ADMIN".equals(user.getRole())) {
                            log.warn("User '{}' (CIVIL) tried to access functions of user {}", user.getLogin(), targetUserId);
                            deny(response, "You can view only your own functions", HttpServletResponse.SC_FORBIDDEN);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Invalid userId parameter: '{}'", userIdParam);
                        deny(response, "Invalid userId", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }
            } else {
                if (!"ADMIN".equals(user.getRole())) {
                    int targetUserId = extractTargetUserId(request, path, method);
                    if (targetUserId == -1) {
                        deny(response, "Could not determine function owner", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                    if (targetUserId != user.getId()) {
                        log.warn("User '{}' (CIVIL) tried to modify function belonging to user {}", user.getLogin(), targetUserId);
                        deny(response, "You can manage only your own functions", HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
            }
        }

        if (path.startsWith("/api/points")) {
            if ("POST".equals(method) && !"ADMIN".equals(user.getRole())) {
                int functionId = extractFunctionIdFromBody(request);
                if (functionId <= 0) {
                    deny(response, "Missing or invalid functionId", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                FunctionDTO func = functionService.getById(functionId);
                if (func == null) {
                    deny(response, "Function not found", HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                if (func.getUserId() != user.getId()) {
                    log.warn("User '{}' (CIVIL) tried to add point to foreign function {}", user.getLogin(), functionId);
                    deny(response, "You can add points only to your own functions", HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
        }

        log.debug("Access granted to {} ({}) for {} {}", user.getLogin(), user.getRole(), method, path);
        chain.doFilter(request, response);
    }

    private int extractTargetUserId(HttpServletRequest request, String path, String method) {
        try {
            if ("POST".equals(method)) {
                String body = getBody(request);
                // Пример ожидаемого тела: {"userId":2, ...} или {"functionId":1} → тогда ищем function
                if (body.contains("\"userId\":")) {
                    int start = body.indexOf("\"userId\":") + 9;
                    int end = findNextDelimiter(body, start);
                    String val = body.substring(start, end).trim();
                    return Integer.parseInt(val);
                } else if (body.contains("\"functionId\":")) {
                    int start = body.indexOf("\"functionId\":") + 13;
                    int end = findNextDelimiter(body, start);
                    String val = body.substring(start, end).trim();
                    int funcId = Integer.parseInt(val);
                    FunctionDTO f = functionService.getById(funcId);
                    return f != null ? f.getUserId() : -1;
                }
            } else if ("PUT".equals(method) || "DELETE".equals(method)) {
                int id = extractIdFromPath(path);
                FunctionDTO f = functionService.getById(id);
                return f != null ? f.getUserId() : -1;
            }
        } catch (Exception e) {
            log.warn("Failed to extract target user ID", e);
        }
        return -1;
    }

    private int extractFunctionIdFromBody(HttpServletRequest request) {
        try {
            String body = getBody(request);
            if (body.contains("\"functionId\":")) {
                int start = body.indexOf("\"functionId\":") + 13;
                int end = findNextDelimiter(body, start);
                String val = body.substring(start, end).trim();
                return Integer.parseInt(val);
            }
        } catch (Exception e) {
            log.warn("Failed to parse functionId from body", e);
        }
        return -1;
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Integer.parseInt(parts[i]);
            } catch (NumberFormatException ignored) {}
        }
        throw new IllegalArgumentException("No ID found in path: " + path);
    }

    private int findNextDelimiter(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ',' || c == '}' || c == ']') return i;
        }
        return s.length();
    }

    private String getBody(HttpServletRequest req) throws IOException {

        return req.getReader().lines().collect(Collectors.joining());
    }

    private void deny(HttpServletResponse resp, String msg, int status) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(String.format("{\"error\":\"%s\"}", msg));
    }

    @Override
    public void destroy() {
        log.info("RoleFilter destroyed");
    }
}