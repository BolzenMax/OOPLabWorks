package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.labwork.ooplabworks.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.DerivativeRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionPayload;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedOperationRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.UnknownOperationException;

@Service
public class TabulatedOperationUiService {

    private final UiFactoryService factoryService;
    private final TabulatedFunctionUiMapper mapper;

    public TabulatedOperationUiService(UiFactoryService factoryService, TabulatedFunctionUiMapper mapper) {
        this.factoryService = factoryService;
        this.mapper = mapper;
    }

    public TabulatedFunctionResponse applyOperation(TabulatedOperationRequest request) {
        if (request == null || request.getOperation() == null) {
            throw new UnknownOperationException("Укажите выполняемую операцию");
        }
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factoryService.currentFactory());
        String operation = request.getOperation().toLowerCase();

        switch (operation) {
            case "add":
                return mapper.toResponse(service.add(
                        mapper.toFunction(request.getFirst(), factoryService.currentFactory()),
                        mapper.toFunction(request.getSecond(), factoryService.currentFactory())), request.getResultName());
            case "subtract":
                return mapper.toResponse(service.subtract(
                        mapper.toFunction(request.getFirst(), factoryService.currentFactory()),
                        mapper.toFunction(request.getSecond(), factoryService.currentFactory())), request.getResultName());
            case "multiply":
                return mapper.toResponse(service.multiplication(
                        mapper.toFunction(request.getFirst(), factoryService.currentFactory()),
                        mapper.toFunction(request.getSecond(), factoryService.currentFactory())), request.getResultName());
            case "divide":
                return mapper.toResponse(service.division(
                        mapper.toFunction(request.getFirst(), factoryService.currentFactory()),
                        mapper.toFunction(request.getSecond(), factoryService.currentFactory())), request.getResultName());
            default:
                throw new UnknownOperationException("Неизвестная операция: " + request.getOperation());
        }
    }

    public TabulatedFunctionResponse differentiate(DerivativeRequest request) {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factoryService.currentFactory());
        TabulatedFunctionPayload payload = request == null ? null : request.getFunction();
        String name = request == null ? null : request.getResultName();
        return mapper.toResponse(operator.derive(mapper.toFunction(payload, factoryService.currentFactory())), name);
    }
}