package com.staticnur.controllers;

import com.staticnur.models.CustomFieldError;
import com.staticnur.models.RegionsDTO;
import com.staticnur.models.Response;
import com.staticnur.repositories.AddressDao;
import com.staticnur.services.FileService;
import com.staticnur.utils.GeneratorResponseMessage;
import com.staticnur.utils.ValidateFormat;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addresses")
public class ParserController {
    private final ValidateFormat validateFormat;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final FileService fileService;
    private final AddressDao addressDao;
    @Autowired
    public ParserController(ValidateFormat validateFormat, FileService fileService,
                            GeneratorResponseMessage generatorResponseMessage, AddressDao addressDao) {
        this.validateFormat = validateFormat;
        this.fileService = fileService;
        this.generatorResponseMessage = generatorResponseMessage;
        this.addressDao = addressDao;
    }

    @PostMapping("/parse")
    public ResponseEntity<?> parseAndSave(@RequestBody @Valid RegionsDTO regions, BindingResult bindingResult) {
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
    @GetMapping
    public ResponseEntity<?> getAllAddress(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10000") int size) {
        try {
            return ResponseEntity.ok(addressDao.findAllAddress(page, size));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response(e.toString()));
        }
    }
    @DeleteMapping
    public ResponseEntity<?> delete() {
        try {
            addressDao.delete();
            return ResponseEntity.ok(new Response("Данные успешно удалены из базы данных."));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new Response(e.toString()));
        }
    }
}
