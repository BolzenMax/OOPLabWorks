package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.MathFunctionOption;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.NewCompositeFunctionRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFromArrayRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFromFunctionRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedApplyRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;

import java.util.List;

@RestController
@RequestMapping("/ui/tabulated")
public class TabulatedFunctionUiController {

    private final TabulatedFunctionUiService service;

    public TabulatedFunctionUiController(TabulatedFunctionUiService service) {
        this.service = service;
    }

    @GetMapping("/functions")
    public ResponseEntity<List<MathFunctionOption>> getFunctions() {
        return ResponseEntity.ok(service.getFunctionOptions());
    }

    @PostMapping("/from-arrays")
    public ResponseEntity<TabulatedFunctionResponse> createFromArrays(@RequestBody TabulatedFromArrayRequest request) {
        return ResponseEntity.ok(service.createFromArrays(request));
    }

    @PostMapping("/from-function")
    public ResponseEntity<TabulatedFunctionResponse> createFromFunction(@RequestBody TabulatedFromFunctionRequest request) {
        return ResponseEntity.ok(service.createFromFunction(request));
    }

    @PostMapping("/functions/composite")
    public ResponseEntity<List<MathFunctionOption>> createComposite(@RequestBody NewCompositeFunctionRequest request) {
        service.createComposite(request);
        return ResponseEntity.ok(service.getFunctionOptions());
    }

    @PostMapping("/apply")
    public ResponseEntity<Double> apply(@RequestBody TabulatedApplyRequest request) {
        return ResponseEntity.ok(service.apply(request));
    }
}