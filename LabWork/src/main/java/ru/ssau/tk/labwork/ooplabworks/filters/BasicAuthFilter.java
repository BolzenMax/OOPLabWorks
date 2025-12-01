// src/main/java/ru/ssau/tk/labwork/ooplabworks/filters/BasicAuthFilter.java
package ru.ssau.tk.labwork.ooplabworks.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.service.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter(urlPatterns = "/api/*")
public class BasicAuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthFilter.class);
    private UserService userService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        log.info("BasicAuthFilter init");

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        if (url == null) {
            url = "jdbc:postgresql://localhost:5432/lab";
            username = "postgres";
            password = "postgres";
        }

        DataSource ds = DataSourceConfig.create(url, username, password);
        this.userService = new UserService(ds);


        config.getServletContext().setAttribute("dataSource", ds);

        log.info("BasicAuthFilter ready (DB: {})", url);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            log.warn("Missing/invalid Authorization header");
            deny(response, "Basic Auth required");
            return;
        }

        try {
            String base64 = authHeader.substring(6).trim();
            String credentials = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid Basic Auth format");
            }

            String login = parts[0];
            String password = parts[1];

            // Используем plaintext-сравнение (как в текущем UserService.authenticate())
            UserDTO user = userService.getByLogin(login);
            if (user == null || !user.isEnabled() || !user.getPassword().equals(password)) {
                log.warn("Auth failed for login={}", login);
                deny(response, "Invalid credentials");
                return;
            }

            log.debug("Authenticated: id={}, login={}, role={}", user.getId(), user.getLogin(), user.getRole());
            request.setAttribute("currentUser", user);
            chain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Auth error", e);
            deny(response, "Authorization failed");
        }
    }

    private void deny(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write("{\"error\":\"" + msg + "\"}");
    }

    @Override
    public void destroy() {
        log.info("BasicAuthFilter destroy");
    }
}