package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.models.Function;

public class FunctionMapper {

    private static final Logger log = LoggerFactory.getLogger(FunctionMapper.class);

    public static FunctionDTO toDTO(Function f) {
        if (f == null) {
            log.warn("toDTO: Function null");
            return null;
        }
        log.debug("Mapping Function -> FunctionDTO id={}", f.getId());

        return new FunctionDTO(
                f.getId(),
                f.getUserId(),
                f.getName(),
                f.getSignature()
        );
    }

    public static Function fromDTO(FunctionDTO dto) {
        if (dto == null) {
            log.warn("fromDTO: FunctionDTO null");
            return null;
        }
        log.debug("Mapping FunctionDTO -> Function id={}", dto.getId());

        return new Function(
                dto.getId(),
                dto.getUserId(),
                dto.getName(),
                dto.getSignature()
        );
    }
}
