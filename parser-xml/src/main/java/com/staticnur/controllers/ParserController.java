package com.staticnur.controllers;

import com.staticnur.models.CustomFieldError;
import com.staticnur.models.RegionsDTO;
import com.staticnur.models.Response;
import com.staticnur.services.FileService;
import com.staticnur.utils.GeneratorResponseMessage;
import com.staticnur.utils.ValidateFormat;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parse")
public class ParserController {
    private final ValidateFormat validateFormat;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final FileService fileService;
    @Autowired
    public ParserController(ValidateFormat validateFormat, FileService fileService, GeneratorResponseMessage generatorResponseMessage) {
        this.validateFormat = validateFormat;
        this.fileService = fileService;
        this.generatorResponseMessage = generatorResponseMessage;
    }

    @PostMapping
    public ResponseEntity<?> parse(@RequestBody @Valid RegionsDTO regions, BindingResult bindingResult) {
        validateFormat.validate(regions, bindingResult);
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        } else {
            try {
                fileService.readArchiveFile(regions.getRegions());
                return ResponseEntity.ok(new Response("Данные успешно распарсены и сохранены в базу данных."));
            }catch (Exception e){
                return ResponseEntity.internalServerError().body(new Response(e.toString()));
            }
        }
    }
}
