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

    // DFS — поиск в глубину (User -> Function -> Points)

    public List<Object> depthFirstSearch(int rootUserId) {
        log.info("DFS start from user {}", rootUserId);

        List<Object> result = new ArrayList<>();
        UserDTO user = usersDao.findById(rootUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        dfsUser(user, result);
        return result;
    }

    private void dfsUser(UserDTO user, List<Object> result) {
        result.add(user);
        log.debug("DFS: visit User {}", user.getId());

        List<FunctionDTO> functions = functionsDao.findByUserId(user.getId());

        for (FunctionDTO f : functions) {
            result.add(f);
            log.debug("DFS: visit Function {}", f.getId());

            List<PointDTO> points = pointsDao.findByFunctionId(f.getId());
            result.addAll(points);

            for (PointDTO p : points) {
                log.debug("DFS: visit Point {}", p.getId());
            }
        }
    }

    // BFS — поиск в ширину
    public List<Object> breadthFirstSearch() {
        log.info("BFS start");

        List<Object> result = new ArrayList<>();

        Queue<Object> queue = new LinkedList<>();

        // 1 уровень — Users
        List<UserDTO> users = usersDao.findAll();
        queue.addAll(users);

        while (!queue.isEmpty()) {
            Object obj = queue.poll();
            result.add(obj);

            if (obj instanceof UserDTO user) {
                log.debug("BFS: user {}", user.getId());
                queue.addAll(functionsDao.findByUserId(user.getId()));
            } else if (obj instanceof FunctionDTO function) {
                log.debug("BFS: function {}", function.getId());
                queue.addAll(pointsDao.findByFunctionId(function.getId()));
            } else if (obj instanceof PointDTO point) {
                log.debug("BFS: point {}", point.getId());
            }
        }

        return result;
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
