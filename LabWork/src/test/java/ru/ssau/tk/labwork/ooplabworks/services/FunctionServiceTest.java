package ru.ssau.tk.labwork.ooplabworks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.repositories.FunctionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunctionServiceTest {

    @Mock
    private FunctionRepository functionRepository;

    @Mock
    private PointService pointService;

    @InjectMocks
    private FunctionService functionService;

    private Function testFunction;

    @BeforeEach
    void setUp() {
        testFunction = new Function();
        testFunction.setId(1L);
        testFunction.setUserId(10L);
        testFunction.setName("linear");
        testFunction.setSignature("f(x) = ax + b");
    }

    @Test
    void testCreateFunction_Success() {
        when(functionRepository.save(any(Function.class))).thenReturn(testFunction);

        Function result = functionService.createFunction(testFunction);

        assertNotNull(result);
        assertEquals("linear", result.getName());
        assertEquals(10L, result.getUserId());
        verify(functionRepository, times(1)).save(testFunction);
    }

    @Test
    void testGetFunctionById_Found() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));

        Optional<Function> result = functionService.getFunctionById(1L);

        assertTrue(result.isPresent());
        assertEquals("linear", result.get().getName());
        verify(functionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllFunctions() {
        Function function2 = new Function(20L, "quadratic", "f(x) = ax² + bx + c");
        function2.setId(2L);
        List<Function> functions = Arrays.asList(testFunction, function2);

        when(functionRepository.findAll()).thenReturn(functions);

        List<Function> result = functionService.getAllFunctions();

        assertEquals(2, result.size());
        assertEquals("linear", result.get(0).getName());
        assertEquals("quadratic", result.get(1).getName());
        verify(functionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateFunction() {
        testFunction.setName("updatedLinear");
        when(functionRepository.save(testFunction)).thenReturn(testFunction);

        Function result = functionService.updateFunction(testFunction);

        assertEquals("updatedLinear", result.getName());
        verify(functionRepository, times(1)).save(testFunction);
    }

    @Test
    void testDeleteFunction() {
        functionService.deleteFunction(1L);

        verify(functionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCountFunctionsByUserId() {
        when(functionRepository.countByUserId(10L)).thenReturn(5L);

        Long count = functionService.countFunctionsByUserId(10L);

        assertEquals(5L, count);
        verify(functionRepository, times(1)).countByUserId(10L);
    }
}
