package ru.ssau.tk.labwork.ooplabworks.config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceConfig {

    public static DataSource create(String url, String username, String password) {

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);

        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30000);

        cfg.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(cfg);
    }
    public static DataSource create() {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        if (url == null) {

            url = "jdbc:postgresql://localhost:5432/lab";
            username = "postgres";
            password = "123"; // ← или "postgres", если так в dev
        }

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30000);


        return new HikariDataSource(cfg);
    }
}