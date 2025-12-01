package ru.ssau.tk.labwork.ooplabworks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.repositories.UserRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.FunctionRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.PointRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchSortServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FunctionRepository functionRepository;

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private SearchSortService searchSortService;

    private User user1;
    private User user2;
    private User adminUser;
    private Function function1;
    private Function function2;
    private Point point1;
    private Point point2;
    private Point point3;

    @BeforeEach
    void setUp() {
        // пользователи

        user1 = new User();
        user1.setId(1L);
        user1.setLogin("john_doe");
        user1.setRole("CIVIL");
        user1.setEnabled(true);

        user2 = new User();
        user2.setId(2L);
        user2.setLogin("jane_doe");
        user2.setRole("CIVIL");
        user2.setEnabled(true);

        adminUser = new User();
        adminUser.setId(3L);
        adminUser.setLogin("admin");
        adminUser.setRole("ADMIN");
        adminUser.setEnabled(true);

        // функции

        function1 = new Function();
        function1.setId(1L);
        function1.setUserId(1L); // john_doe
        function1.setName("linear_function");
        function1.setSignature("f(x) = ax + b");

        function2 = new Function();
        function2.setId(2L);
        function2.setUserId(2L); // jane_doe
        function2.setName("quadratic_function");
        function2.setSignature("f(x) = ax² + bx + c");

        // точки

        point1 = new Point();
        point1.setId(1L);
        point1.setFunctionId(1L);
        point1.setX(1.0);
        point1.setY(2.0);

        point2 = new Point();
        point2.setId(2L);
        point2.setFunctionId(1L);
        point2.setX(2.0);
        point2.setY(4.0);

        point3 = new Point();
        point3.setId(3L);
        point3.setFunctionId(2L);
        point3.setX(3.0);
        point3.setY(9.0);
    }

    // пользователи

    @Test
    void testFindUsersByLogin_Found() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByLoginContaining("doe")).thenReturn(users);

        List<User> result = searchSortService.findUsersByLogin("doe");

        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).getLogin());
        assertEquals("jane_doe", result.get(1).getLogin());
        verify(userRepository, times(1)).findByLoginContaining("doe");
    }

    @Test
    void testFindUsersByLogin_NotFound() {
        when(userRepository.findByLoginContaining("nonexistent")).thenReturn(Arrays.asList());

        List<User> result = searchSortService.findUsersByLogin("nonexistent");

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByLoginContaining("nonexistent");
    }

    @Test
    void testFindUsersByRole_Found() {
        List<User> civilUsers = Arrays.asList(user1, user2);
        when(userRepository.findByRole("CIVIL")).thenReturn(civilUsers);

        List<User> adminUsers = Arrays.asList(adminUser);
        when(userRepository.findByRole("ADMIN")).thenReturn(adminUsers);

        List<User> civilResult = searchSortService.findUsersByRole("CIVIL");
        List<User> adminResult = searchSortService.findUsersByRole("ADMIN");

        assertEquals(2, civilResult.size());
        assertEquals(1, adminResult.size());
        assertEquals("admin", adminResult.get(0).getLogin());
        verify(userRepository, times(1)).findByRole("CIVIL");
        verify(userRepository, times(1)).findByRole("ADMIN");
    }

    // функции

    @Test
    void testFindFunctionsByName_Found() {
        List<Function> functions = Arrays.asList(function1, function2);
        when(functionRepository.findByNameContaining("function")).thenReturn(functions);

        List<Function> result = searchSortService.findFunctionsByName("function");

        assertEquals(2, result.size());
        assertEquals("linear_function", result.get(0).getName());
        assertEquals("quadratic_function", result.get(1).getName());
        verify(functionRepository, times(1)).findByNameContaining("function");
    }

    @Test
    void testFindFunctionsByName_NotFound() {
        when(functionRepository.findByNameContaining("nonexistent")).thenReturn(Arrays.asList());

        List<Function> result = searchSortService.findFunctionsByName("nonexistent");

        assertTrue(result.isEmpty());
        verify(functionRepository, times(1)).findByNameContaining("nonexistent");
    }

    @Test
    void testFindFunctionsByUserId_Found() {
        List<Function> functions = Arrays.asList(function1);
        when(functionRepository.findByUserId(1L)).thenReturn(functions);

        List<Function> result = searchSortService.findFunctionsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("linear_function", result.get(0).getName());
        assertEquals(1L, result.get(0).getUserId());
        verify(functionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testFindFunctionsByUserIdAndName_Combo_Found() {
        List<Function> functions = Arrays.asList(function1);
        when(functionRepository.findByUserIdAndNameContaining(1L, "linear")).thenReturn(functions);

        List<Function> result = searchSortService.findFunctionsByUserIdAndName(1L, "linear");

        assertEquals(1, result.size());
        assertEquals("linear_function", result.get(0).getName());
        verify(functionRepository, times(1)).findByUserIdAndNameContaining(1L, "linear");
    }

    @Test
    void testFindFunctionsByUserIdAndName_Combo_NotFound() {
        when(functionRepository.findByUserIdAndNameContaining(1L, "quadratic")).thenReturn(Arrays.asList());

        List<Function> result = searchSortService.findFunctionsByUserIdAndName(1L, "quadratic");

        assertTrue(result.isEmpty());
        verify(functionRepository, times(1)).findByUserIdAndNameContaining(1L, "quadratic");
    }

    // точки

    @Test
    void testFindPointsByFunctionId_Found() {
        List<Point> points = Arrays.asList(point1, point2);
        when(pointRepository.findByFunctionId(1L)).thenReturn(points);

        List<Point> result = searchSortService.findPointsByFunctionId(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getFunctionId());
        assertEquals(1L, result.get(1).getFunctionId());
        verify(pointRepository, times(1)).findByFunctionId(1L);
    }

    @Test
    void testFindPointsByFunctionIdAndXRange_Found() {
        List<Point> points = Arrays.asList(point2);
        when(pointRepository.findByFunctionIdAndXBetween(1L, 1.5, 2.5)).thenReturn(points);

        List<Point> result = searchSortService.findPointsByFunctionIdAndXRange(1L, 1.5, 2.5);

        assertEquals(1, result.size());
        assertEquals(2.0, result.get(0).getX());
        verify(pointRepository, times(1)).findByFunctionIdAndXBetween(1L, 1.5, 2.5);
    }

    @Test
    void testFindPointsByFunctionIdAndXRange_NotFound() {
        when(pointRepository.findByFunctionIdAndXBetween(1L, 10.0, 20.0)).thenReturn(Arrays.asList());

        List<Point> result = searchSortService.findPointsByFunctionIdAndXRange(1L, 10.0, 20.0);

        assertTrue(result.isEmpty());
        verify(pointRepository, times(1)).findByFunctionIdAndXBetween(1L, 10.0, 20.0);
    }

    @Test
    void testFindPointsByFunctionIdOrderedByXAsc() {
        List<Point> points = Arrays.asList(point1, point2);
        when(pointRepository.findByFunctionIdOrderByXAsc(1L)).thenReturn(points);

        List<Point> result = searchSortService.findPointsByFunctionIdOrderedByXAsc(1L);

        assertEquals(2, result.size());
        assertEquals(1.0, result.get(0).getX());
        assertEquals(2.0, result.get(1).getX());
        verify(pointRepository, times(1)).findByFunctionIdOrderByXAsc(1L);
    }

    @Test
    void testFindPointsByFunctionIdOrderedByXDesc() {
        List<Point> points = Arrays.asList(point2, point1);
        when(pointRepository.findByFunctionIdOrderByXDesc(1L)).thenReturn(points);

        List<Point> result = searchSortService.findPointsByFunctionIdOrderedByXDesc(1L);

        assertEquals(2, result.size());
        assertEquals(2.0, result.get(0).getX());
        assertEquals(1.0, result.get(1).getX());
        verify(pointRepository, times(1)).findByFunctionIdOrderByXDesc(1L);
    }

    // иерархия

    @Test
    void testGetUserHierarchy_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        List<Function> functions = Arrays.asList(function1);
        when(functionRepository.findByUserId(1L)).thenReturn(functions);

        List<Point> points = Arrays.asList(point1, point2);
        when(pointRepository.findByFunctionId(1L)).thenReturn(points);

        SearchSortService.UserHierarchy result = searchSortService.getUserHierarchy(1L);

        assertNotNull(result);
        assertEquals("john_doe", result.getUser().getLogin());
        assertEquals(1, result.getFunctions().size());
        assertEquals("linear_function", result.getFunctions().get(0).getName());

        Map<Long, List<Point>> functionPoints = result.getFunctionPoints();
        assertEquals(1, functionPoints.size());
        assertEquals(2, functionPoints.get(1L).size());

        verify(userRepository, times(1)).findById(1L);
        verify(functionRepository, times(1)).findByUserId(1L);
        verify(pointRepository, times(1)).findByFunctionId(1L);
    }

    @Test
    void testGetUserHierarchy_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        SearchSortService.UserHierarchy result = searchSortService.getUserHierarchy(999L);

        assertNull(result);
        verify(userRepository, times(1)).findById(999L);
        verify(functionRepository, never()).findByUserId(any());
        verify(pointRepository, never()).findByFunctionId(any());
    }

    @Test
    void testGetUserHierarchy_UserWithNoFunctions() {
        User userWithoutFunctions = new User();
        userWithoutFunctions.setId(4L);
        userWithoutFunctions.setLogin("empty_user");

        when(userRepository.findById(4L)).thenReturn(Optional.of(userWithoutFunctions));
        when(functionRepository.findByUserId(4L)).thenReturn(Arrays.asList());

        SearchSortService.UserHierarchy result = searchSortService.getUserHierarchy(4L);

        assertNotNull(result);
        assertEquals("empty_user", result.getUser().getLogin());
        assertTrue(result.getFunctions().isEmpty());
        assertTrue(result.getFunctionPoints().isEmpty());

        verify(pointRepository, never()).findByFunctionId(any());
    }

    @Test
    void testGetUserHierarchy_FunctionWithNoPoints() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        Function functionWithoutPoints = new Function();
        functionWithoutPoints.setId(3L);
        functionWithoutPoints.setUserId(2L);
        functionWithoutPoints.setName("empty_function");

        List<Function> functions = Arrays.asList(functionWithoutPoints);
        when(functionRepository.findByUserId(2L)).thenReturn(functions);

        when(pointRepository.findByFunctionId(3L)).thenReturn(Arrays.asList());

        SearchSortService.UserHierarchy result = searchSortService.getUserHierarchy(2L);

        assertNotNull(result);
        assertEquals(1, result.getFunctions().size());
        assertEquals("empty_function", result.getFunctions().get(0).getName());

        Map<Long, List<Point>> functionPoints = result.getFunctionPoints();
        assertEquals(1, functionPoints.size());
        assertTrue(functionPoints.get(3L).isEmpty());
    }

    @Test
    void testUserHierarchy_GetPointsForFunction_Found() {
        List<Point> points = Arrays.asList(point1, point2);
        Map<Long, List<Point>> functionPoints = Map.of(1L, points);

        SearchSortService.UserHierarchy hierarchy =
                new SearchSortService.UserHierarchy(user1, Arrays.asList(function1), functionPoints);

        List<Point> result = hierarchy.getPointsForFunction(1L);

        assertEquals(2, result.size());
        assertEquals(1.0, result.get(0).getX());
        assertEquals(2.0, result.get(1).getX());
    }

    @Test
    void testUserHierarchy_GetPointsForFunction_NotFound() {
        SearchSortService.UserHierarchy hierarchy =
                new SearchSortService.UserHierarchy(user1, Arrays.asList(), Map.of());

        List<Point> result = hierarchy.getPointsForFunction(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUserHierarchy_Getters() {
        List<Function> functions = Arrays.asList(function1);
        Map<Long, List<Point>> functionPoints = Map.of(1L, Arrays.asList(point1));

        SearchSortService.UserHierarchy hierarchy =
                new SearchSortService.UserHierarchy(user1, functions, functionPoints);

        assertEquals(user1, hierarchy.getUser());
        assertEquals(functions, hierarchy.getFunctions());
        assertEquals(functionPoints, hierarchy.getFunctionPoints());
    }

    // крайности

    @Test
    void testFindUsersByLogin_EmptyString() {
        when(userRepository.findByLoginContaining("")).thenReturn(Arrays.asList(user1, user2, adminUser));

        List<User> result = searchSortService.findUsersByLogin("");

        assertEquals(3, result.size());
        verify(userRepository, times(1)).findByLoginContaining("");
    }

    @Test
    void testFindFunctionsByName_EmptyString() {
        List<Function> allFunctions = Arrays.asList(function1, function2);
        when(functionRepository.findByNameContaining("")).thenReturn(allFunctions);

        List<Function> result = searchSortService.findFunctionsByName("");

        assertEquals(2, result.size());
        verify(functionRepository, times(1)).findByNameContaining("");
    }

    @Test
    void testFindPointsByFunctionIdAndXRange_NullBounds() {
        List<Point> allPoints = Arrays.asList(point1, point2, point3);
        when(pointRepository.findByFunctionIdAndXBetween(eq(1L), isNull(), isNull()))
                .thenReturn(Arrays.asList(point1, point2));

        List<Point> result = searchSortService.findPointsByFunctionIdAndXRange(1L, null, null);

        assertEquals(2, result.size());
        verify(pointRepository, times(1)).findByFunctionIdAndXBetween(eq(1L), isNull(), isNull());
    }

    @Test
    void testFindAllUsersSorted_EmptyDatabase() {
        when(userRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList());

        List<User> result = searchSortService.findAllUsersSortedByLogin(true);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll(any(Sort.class));
    }
}