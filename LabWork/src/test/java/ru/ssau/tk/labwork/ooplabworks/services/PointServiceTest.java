package ru.ssau.tk.labwork.ooplabworks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.repositories.PointRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    private Point testPoint;

    @BeforeEach
    void setUp() {
        testPoint = new Point();
        testPoint.setId(1L);
        testPoint.setFunctionId(10L);
        testPoint.setX(2.5);
        testPoint.setY(6.25);
    }

    @Test
    void testCreatePoint_Success() {
        when(pointRepository.save(any(Point.class))).thenReturn(testPoint);

        Point result = pointService.createPoint(testPoint);

        assertNotNull(result);
        assertEquals(10L, result.getFunctionId());
        assertEquals(2.5, result.getX());
        verify(pointRepository, times(1)).save(testPoint);
    }

    @Test
    void testCreatePoints_Batch() {
        Point point1 = new Point(10L, 1.0, 1.0);
        Point point2 = new Point(10L, 2.0, 4.0);
        List<Point> points = Arrays.asList(point1, point2);

        when(pointRepository.saveAll(anyList())).thenReturn(points);

        List<Point> result = pointService.createPoints(points);

        assertEquals(2, result.size());
        verify(pointRepository, times(1)).saveAll(points);
    }

    @Test
    void testGetPointById_Found() {
        when(pointRepository.findById(1L)).thenReturn(Optional.of(testPoint));

        Optional<Point> result = pointService.getPointById(1L);

        assertTrue(result.isPresent());
        assertEquals(2.5, result.get().getX());
        verify(pointRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllPoints() {
        Point point2 = new Point(20L, 3.0, 9.0);
        point2.setId(2L);
        List<Point> points = Arrays.asList(testPoint, point2);

        when(pointRepository.findAll()).thenReturn(points);

        List<Point> result = pointService.getAllPoints();

        assertEquals(2, result.size());
        verify(pointRepository, times(1)).findAll();
    }

    @Test
    void testUpdatePoint() {
        testPoint.setX(10.0);
        testPoint.setY(100.0);
        when(pointRepository.save(testPoint)).thenReturn(testPoint);

        Point result = pointService.updatePoint(testPoint);

        assertEquals(10.0, result.getX());
        assertEquals(100.0, result.getY());
        verify(pointRepository, times(1)).save(testPoint);
    }

    @Test
    void testDeletePoint() {
        pointService.deletePoint(1L);

        verify(pointRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePointsByFunctionId() {
        pointService.deletePointsByFunctionId(10L);

        verify(pointRepository, times(1)).deleteByFunctionId(10L);
    }
}
