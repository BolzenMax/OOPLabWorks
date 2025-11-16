package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.models.User;

public class UserMapper {

    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);

    public static UserDTO toDTO(User user) {
        if (user == null) {
            log.warn("toDTO: source User = null");
            return null;
        }

        log.debug("Mapping User -> UserDTO id={}", user.getId());

        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getRole(),
                user.getPassword(),
                user.isEnabled()
        );
    }

    public static User fromDTO(UserDTO dto) {
        if (dto == null) {
            log.warn("fromDTO: source UserDTO = null");
            return null;
        }

        log.debug("Mapping UserDTO -> User id={}", dto.getId());

        return new User(
                dto.getId(),
                dto.getLogin(),
                dto.getRole(),
                dto.getPassword(),
                dto.isEnabled()
        );
    }
}