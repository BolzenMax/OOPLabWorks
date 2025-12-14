package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ssau.tk.labwork.ooplabworks.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.labwork.ooplabworks.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.labwork.ooplabworks.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.ErrorResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.FileProcessingException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidFactorySelectionException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidPointCountException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.NonNumericValueException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.TableSizeMismatchException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.UnknownFunctionException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.UnknownOperationException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.ValueOutOfRangeException;

@RestControllerAdvice
public class UiExceptionHandler {

    @ExceptionHandler(InvalidPointCountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPointCount(InvalidPointCountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_points_count", ex.getMessage()));
    }

    @ExceptionHandler(NonNumericValueException.class)
    public ResponseEntity<ErrorResponse> handleNonNumeric(NonNumericValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_number", ex.getMessage()));
    }

    @ExceptionHandler(ValueOutOfRangeException.class)
    public ResponseEntity<ErrorResponse> handleOutOfRange(ValueOutOfRangeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("value_out_of_range", ex.getMessage()));
    }

    @ExceptionHandler(TableSizeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTableSize(TableSizeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("table_size_mismatch", ex.getMessage()));
    }

    @ExceptionHandler({InvalidFactorySelectionException.class, UnknownOperationException.class})
    public ResponseEntity<ErrorResponse> handleInvalidState(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_request", ex.getMessage()));
    }

    @ExceptionHandler({ArrayIsNotSortedException.class, DifferentLengthOfArraysException.class})
    public ResponseEntity<ErrorResponse> handleValidation(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("function_validation", ex.getMessage()));
    }

    @ExceptionHandler(InconsistentFunctionsException.class)
    public ResponseEntity<ErrorResponse> handleInconsistentFunctions(InconsistentFunctionsException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("inconsistent_functions", ex.getMessage()));
    }

    @ExceptionHandler(UnknownFunctionException.class)
    public ResponseEntity<ErrorResponse> handleUnknownFunction(UnknownFunctionException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("unknown_function", ex.getMessage()));
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ErrorResponse> handleFile(FileProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("file_error", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_payload", "Не удалось разобрать запрос. Проверьте формат данных."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("server_error", "Возникла непредвиденная ошибка. Попробуйте ещё раз."));
    }
}