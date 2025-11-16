package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.models.Point;

public class PointMapper {

    private static final Logger log = LoggerFactory.getLogger(PointMapper.class);

    public static PointDTO toDTO(Point p) {
        if (p == null) {
            log.warn("toDTO: Point null");
            return null;
        }
        log.debug("Mapping Point -> PointDTO id={}", p.getId());

        return new PointDTO(
                p.getId(),
                p.getFunctionId(),
                p.getX(),
                p.getY()
        );
    }

    public static Point fromDTO(PointDTO dto) {
        if (dto == null) {
            log.warn("fromDTO: PointDTO null");
            return null;
        }
        log.debug("Mapping PointDTO -> Point id={}", dto.getId());

        return new Point(
                dto.getId(),
                dto.getFunctionId(),
                dto.getX(),
                dto.getY()
        );
    }
}
