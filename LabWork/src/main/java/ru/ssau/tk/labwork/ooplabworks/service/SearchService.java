package ru.ssau.tk.labwork.ooplabworks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dao.FunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.PointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import java.util.*;

public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final UsersDao usersDao;
    private final FunctionsDao functionsDao;
    private final PointsDao pointsDao;

    public SearchService(UsersDao usersDao, FunctionsDao functionsDao, PointsDao pointsDao) {
        this.usersDao = usersDao;
        this.functionsDao = functionsDao;
        this.pointsDao = pointsDao;
    }




    // Иерархический поиск ( User → Functions → Points)
    public Map<String, Object> loadUserTree(int userId) {
        log.info("Loading full tree for user {}", userId);

        UserDTO user = usersDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);

        List<FunctionDTO> funcs = functionsDao.findByUserId(userId);
        result.put("functions", funcs);

        Map<Integer, List<PointDTO>> points = new HashMap<>();
        for (FunctionDTO f : funcs) {
            points.put(f.getId(), pointsDao.findByFunctionId(f.getId()));
        }
        result.put("points", points);

        return result;
    }

    // Одиночный поиск
    public Optional<UserDTO> findUserByLogin(String login) {
        log.info("findUserByLogin {}", login);
        return usersDao.findByLogin(login);
    }

    public List<FunctionDTO> findFunctionsByName(String name) {
        log.info("findFunctionsByName {}", name);
        return functionsDao.findByName(name);
    }

    // Множественный поиск + сортировка
    public List<UserDTO> findUsersSortedByLogin() {
        log.info("findUsersSortedByLogin");
        return usersDao.findAllSortedBy("login");
    }

    public List<FunctionDTO> searchFunctionsMulti(Collection<String> names) {
        log.info("searchFunctionsMulti: {}", names);
        return functionsDao.findByNames(names);
    }
}
