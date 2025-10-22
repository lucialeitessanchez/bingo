package com.bingo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bingo.service.GameService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private GameService gameService;

    // Muestra la vista de control
    @GetMapping("/view")
    public String showTeacherView(Model model) {
        model.addAttribute("historyPhrases", gameService.getHistoryPhrases());
        model.addAttribute("playerCount", gameService.getAllPlayers().size());
        model.addAttribute("maxPlayers", 30);
        model.addAttribute("gameActive", gameService.isGameActive());
        model.addAttribute("winnerId", gameService.getWinningPlayerId());

        // La imagen se muestra condicionalmente en el HTML
        return "teacher_view"; // Renderiza teacher_view.html
    }

    // Inicia el juego
    @PostMapping("/start")
    public String startGame(RedirectAttributes redirectAttributes) {
        if (gameService.startGame()) {
            redirectAttributes.addFlashAttribute("message", "¡Juego iniciado!");
        } else {
            redirectAttributes.addFlashAttribute("error", "No hay jugadores o el juego ya está activo.");
        }
        return "redirect:/teacher/view";
    }

    // Genera la siguiente frase (simula la lectura del QR)
    @PostMapping("/next-phrase")
    public String generatePhrase(RedirectAttributes redirectAttributes) {
        if (!gameService.isGameActive()) {
            redirectAttributes.addFlashAttribute("error", "El juego no está activo.");
        } else if (gameService.getWinningPlayerId() != null) {
            redirectAttributes.addFlashAttribute("error", "El juego ha terminado.");
        } else {
            String newPhrase = gameService.generateNextPhrase();
            redirectAttributes.addFlashAttribute("newPhrase", "¡Nueva frase: " + newPhrase + "!");
        }
        return "redirect:/teacher/view";
    }

    // Acción simple para resetear el juego completamente (opcional)
    @PostMapping("/reset")
    public String resetGame() {
        // En una implementación real, esto podría requerir más lógica de limpieza.
        // Aquí, como el estado es en memoria, un restart/limpieza es simple.
        gameService.getAllPlayers().clear();
        gameService.getHistoryPhrases().clear();
        // Nota: El estado activo se manejaría al iniciar de nuevo.
        return "redirect:/teacher/view";
    }
}