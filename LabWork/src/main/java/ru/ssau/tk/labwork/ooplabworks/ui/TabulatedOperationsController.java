package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.DerivativeRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedOperationRequest;

@RestController
@RequestMapping("/ui/operations")
public class TabulatedOperationsController {

    private final TabulatedOperationUiService operationUiService;

    public TabulatedOperationsController(TabulatedOperationUiService operationUiService) {
        this.operationUiService = operationUiService;
    }

    @PostMapping("/binary")
    public ResponseEntity<TabulatedFunctionResponse> applyBinary(@RequestBody TabulatedOperationRequest request) {
        return ResponseEntity.ok(operationUiService.applyOperation(request));
    }

    @PostMapping("/derivative")
    public ResponseEntity<TabulatedFunctionResponse> derive(@RequestBody DerivativeRequest request) {
        return ResponseEntity.ok(operationUiService.differentiate(request));
    }
}