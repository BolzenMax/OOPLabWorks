package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.FactorySelectionRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.FactoryStateResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidFactorySelectionException;

@RestController
@RequestMapping("/ui/state")
public class UiStateController {

    private final UiFactoryService factoryService;

    public UiStateController(UiFactoryService factoryService) {
        this.factoryService = factoryService;
    }

    @GetMapping("/factory")
    public ResponseEntity<FactoryStateResponse> getFactory() {
        return ResponseEntity.ok(asResponse(factoryService.getCurrentType()));
    }

    @PostMapping("/factory")
    public ResponseEntity<FactoryStateResponse> updateFactory(@RequestBody FactorySelectionRequest request) {
        TabulatedFactoryType type;
        try {
            type = TabulatedFactoryType.valueOf(request.getType());
        } catch (Exception ex) {
            throw new InvalidFactorySelectionException("Неизвестный тип фабрики");
        }
        return ResponseEntity.ok(asResponse(factoryService.updateType(type)));
    }

    private FactoryStateResponse asResponse(TabulatedFactoryType type) {
        String displayName = type == TabulatedFactoryType.ARRAY ? "Массив" : "Связный список";
        return new FactoryStateResponse(type.name(), displayName);
    }
}