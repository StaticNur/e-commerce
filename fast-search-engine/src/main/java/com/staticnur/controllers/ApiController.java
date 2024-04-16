package com.staticnur.controllers;

import com.staticnur.model.Address;
import com.staticnur.services.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public String downloadDataInEs(){
        esService.download();
        return "Данные успешно загружены из PostgreSQL в ElasticSearch";
    }

    /*@PutMapping("/articles")
    public String addArticle(@RequestParam("title") String title, @RequestParam("text") String text) throws Exception {
        String id = UUID.randomUUID().toString();
        esService.updateAddress(id, title, text);
        return id;
    }*/

    @GetMapping("/search")
    public List<Address> search(@RequestParam("query") String query) throws Exception {
        return esService.searchAddress(query);
    }

}
