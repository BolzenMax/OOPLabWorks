package ru.ssau.tk.labwork.ooplabworks.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.labwork.ooplabworks.jpa.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    List<User> findByRole(String role);

    @Query("SELECT u FROM User u WHERE u.login LIKE %:keyword%")
    List<User> findByLoginContaining(@Param("keyword") String keyword);
}