package com.hard.controllers;

import com.hard.services.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entities")
public class EntityController {
    @Autowired
    private EntityService entityService;

    @GetMapping(value = "")
    public String get() {
        String entity = entityService.getEntity();
        System.out.println(entity);

        return "entities/main";
    }
}
