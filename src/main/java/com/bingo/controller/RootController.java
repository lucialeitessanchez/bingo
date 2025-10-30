package com.bingo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    /**
     * Mapea la URL raíz (http://localhost:8080/) y redirige a la vista de la
     * docente.
     */
    @GetMapping("/")
    public String redirectToTeacherView() {
        // Redirige al navegador a la URL del TeacherController
        return "redirect:/teacher/view";
    }
}