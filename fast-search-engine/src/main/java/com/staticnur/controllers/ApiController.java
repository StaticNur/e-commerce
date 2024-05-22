package com.staticnur.controllers;

import com.staticnur.model.Address;
import com.staticnur.model.Response;
import com.staticnur.services.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final EsService esService;

    @Autowired
    public ApiController(EsService esService) {
        this.esService = esService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/download")
    public ResponseEntity<Response> downloadDataInEs() {
        esService.download();
        return ResponseEntity.ok(new Response("Данные успешно загружены из PostgreSQL в ElasticSearch"));
    }

    @GetMapping("/search")
    public List<Address> search(@RequestParam("query") String query) throws Exception {
        return esService.searchAddress(query);
    }

}
