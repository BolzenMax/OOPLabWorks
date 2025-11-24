package ru.ssau.tk.labwork.ooplabworks.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.repositories.FunctionRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.PointRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.UserRepository;

import java.util.*;

@Slf4j
@Service
public class SearchSortService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    // пользователи

    public List<User> findUsersByLogin(String login) {
        log.debug("Поиск пользователЯ по логину: {}", login);
        return userRepository.findByLoginContaining(login);
    }

    public List<User> findUsersByRole(String role) {
        log.debug("Поиск пользователЕЙ по роли: {}", role);
        return userRepository.findByRole(role);
    }

    public List<User> findAllUsersSortedByLogin(boolean ascending) {
        log.debug("Поиск всех пользователЕЙ с сортировкой по логину (в алфавитном порядке: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "login") : Sort.by(Sort.Direction.DESC, "login");
        return userRepository.findAll(sort);
    }

    public List<User> findAllUsersSortedById(boolean ascending) {
        log.debug("Поиск всех пользователЕЙ с сортировкой по ID (по возрастанию: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "id") : Sort.by(Sort.Direction.DESC, "id");
        return userRepository.findAll(sort);
    }

    // функции

    public List<Function> findFunctionsByName(String name) {
        log.debug("Поиск функциИ по имени: {}", name);
        return functionRepository.findByNameContaining(name);
    }

    public List<Function> findFunctionsByUserId(Long userId) {
        log.debug("Поиск функциЙ по ID пользователя: {}", userId);
        return functionRepository.findByUserId(userId);
    }

    public List<Function> findFunctionsByUserIdAndName(Long userId, String name) { // КККОМБО
        log.debug("Поиск функциЙ по пользователю с ID {} и имени: {}", userId, name);
        return functionRepository.findByUserIdAndNameContaining(userId, name);
    }

    public List<Function> findAllFunctionsSortedByName(boolean ascending) {
        log.debug("Поиск всех функциЙ с сортировкой по имени (в алфавитном порядке: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "name") : Sort.by(Sort.Direction.DESC, "name");
        return functionRepository.findAll(sort);
    }

    public List<Function> findAllFunctionsSortedByUserId(boolean ascending) {
        log.debug("Поиск всех функциЙ с сортировкой по ID пользователя (по возрастанию: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "userId") : Sort.by(Sort.Direction.DESC, "userId");
        return functionRepository.findAll(sort);
    }

    // точки

    public List<Point> findPointsByFunctionId(Long functionId) {
        log.debug("Поиск точЕК по ID функции: {}", functionId);
        return pointRepository.findByFunctionId(functionId);
    }

    public List<Point> findPointsByFunctionIdAndXRange(Long functionId, Double minX, Double maxX) {
        log.debug("Поиск точЕК по функции {} где X в интервале [{}, {}]", functionId, minX, maxX);
        return pointRepository.findByFunctionIdAndXBetween(functionId, minX, maxX);
    }

    public List<Point> findPointsByFunctionIdOrderedByXAsc(Long functionId) {
        log.debug("Поиск точЕК функции {} с сортировкой по X (возрастание)", functionId);
        return pointRepository.findByFunctionIdOrderByXAsc(functionId);
    }

    public List<Point> findPointsByFunctionIdOrderedByXDesc(Long functionId) {
        log.debug("Поиск точЕК функции {} с сортировкой по X (убывание)", functionId);
        return pointRepository.findByFunctionIdOrderByXDesc(functionId);
    }

    public List<Point> findAllPointsSortedByFunctionId(boolean ascending) {
        log.debug("Поиск всех точЕК с сортировкой по ID функции (по возрастанию: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "functionId") : Sort.by(Sort.Direction.DESC, "functionId");
        return pointRepository.findAll(sort);
    }

    public List<Point> findAllPointsSortedByX(boolean ascending) {
        log.debug("Поиск всех точЕК с сортировкой по X (по возрастанию: {})", ascending);
        Sort sort = ascending ? Sort.by(Sort.Direction.ASC, "x") : Sort.by(Sort.Direction.DESC, "x");
        return pointRepository.findAll(sort);
    }

    // иерархия

    public UserHierarchy getUserHierarchy(Long userId) {
        log.info("Данные о пользователе с ID: {}", userId);

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return null;
        }

        List<Function> functions = functionRepository.findByUserId(userId);
        Map<Long, List<Point>> functionPoints = new HashMap<>();

        for (Function function : functions) {
            List<Point> points = pointRepository.findByFunctionId(function.getId());
            functionPoints.put(function.getId(), points);
        }

        return new UserHierarchy(user.get(), functions, functionPoints);
    }

    public static class UserHierarchy { // вспомогательный класс
        private final User user;
        private final List<Function> functions;
        private final Map<Long, List<Point>> functionPoints;

        public UserHierarchy(User user, List<Function> functions, Map<Long, List<Point>> functionPoints) {
            this.user = user;
            this.functions = functions;
            this.functionPoints = functionPoints;
        }

        public User getUser() {
            return user;
        }

        public List<Function> getFunctions() {
            return functions;
        }

        public Map<Long, List<Point>> getFunctionPoints() {
            return functionPoints;
        }

        public List<Point> getPointsForFunction(Long functionId) {
            return functionPoints.getOrDefault(functionId, new ArrayList<>());
        }
    }
}