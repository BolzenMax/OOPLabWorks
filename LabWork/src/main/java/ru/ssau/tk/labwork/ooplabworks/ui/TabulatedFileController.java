package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.io.FunctionsIO;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionPayload;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.FileProcessingException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ui/tabulated")
public class TabulatedFileController {

    private final UiFactoryService factoryService;
    private final TabulatedFunctionUiMapper mapper;

    public TabulatedFileController(UiFactoryService factoryService, TabulatedFunctionUiMapper mapper) {
        this.factoryService = factoryService;
        this.mapper = mapper;
    }

    @PostMapping(value = "/serialize/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> serializeJson(@RequestBody TabulatedFunctionPayload payload) {
        try {
            String json = mapper.payloadToJson(payload);
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tabulated_function.json")
                    .contentLength(data.length)
                    .body(new ByteArrayResource(data));
        } catch (IOException ex) {
            throw new FileProcessingException("Не удалось сохранить функцию в json", ex);
        }
    }

    @PostMapping(value = "/serialize/xml", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> serializeXml(@RequestBody TabulatedFunctionPayload payload) {
        String xml = buildXml(payload);
        byte[] data = xml.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tabulated_function.xml")
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping(value = "/serialize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> serialize(@RequestBody TabulatedFunctionPayload payload) {
        TabulatedFunction function = mapper.toFunction(payload, factoryService.currentFactory());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            FunctionsIO.serialize(bufferedOutputStream, function);
            bufferedOutputStream.flush();
            byte[] data = outputStream.toByteArray();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tabulated_function.bin")
                    .contentLength(data.length)
                    .body(new ByteArrayResource(data));
        } catch (IOException ex) {
            throw new FileProcessingException("Не удалось сериализовать функцию", ex);
        }
    }

    @PostMapping(value = "/deserialize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TabulatedFunctionResponse> deserialize(@RequestPart("file") MultipartFile file) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(file.getBytes()))) {
            TabulatedFunction function = FunctionsIO.deserialize(inputStream);
            return ResponseEntity.ok(mapper.toResponse(function));
        } catch (IOException | ClassNotFoundException ex) {
            throw new FileProcessingException("Не удалось прочитать файл функции", ex);
        }
    }

    @PostMapping(value = "/deserialize/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TabulatedFunctionPayload> deserializeJson(@RequestPart("file") MultipartFile file) {
        try {
            String json = new String(file.getBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok(mapper.jsonToPayload(json));
        } catch (IOException ex) {
            throw new FileProcessingException("Не удалось прочитать json", ex);
        }
    }

    @PostMapping(value = "/deserialize/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TabulatedFunctionPayload> deserializeXml(@RequestPart("file") MultipartFile file) {
        try {
            String xml = new String(file.getBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok(mapper.xmlToPayload(xml));
        } catch (IOException ex) {
            throw new FileProcessingException("Не удалось прочитать xml", ex);
        }
    }

    private String buildXml(TabulatedFunctionPayload payload) {
        String points = payload.getPoints().stream()
                .map(p -> String.format("    <point><x>%s</x><y>%s</y></point>", p.getX(), p.getY()))
                .collect(Collectors.joining("\n"));
        return "<tabulatedFunction>\n" + points + "\n</tabulatedFunction>";
    }
}