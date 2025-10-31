package com.bingo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bingo.model.Player;
import com.bingo.service.GameService;

@Controller
@RequestMapping("/player")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/join")
    public String joinForm() {
        // Muestra el formulario donde se ingresa el nombre
        return "join_form"; // join_form.html
    }

    // Simula la lectura de QR / Unirse al juego
    @PostMapping("/join")
    public String joinGame(@RequestParam String playerName, RedirectAttributes redirectAttributes) {
        try {
            Player player = gameService.addPlayer(playerName); // ahora pasamos el nombre
            return "redirect:/player/" + player.getId();
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/join";
        }
    }

    // Muestra el tablero del jugador
    @GetMapping("/{playerId}")
    public String showPlayerBoard(@PathVariable String playerId, Model model) {
        Player player = gameService.getPlayer(playerId);

        if (player == null) {
            model.addAttribute("error", "Jugador no encontrado.");
            return "error"; // Puedes crear una página de error
        }

        model.addAttribute("player", player);
        model.addAttribute("gameActive", gameService.isGameActive());
        model.addAttribute("winnerId", gameService.getWinningPlayerId());

        return "player_board"; // Renderiza player_board.html
    }

    // Acción de marcar una frase
    @PostMapping("/{playerId}/mark")
    public String markPhrase(@PathVariable String playerId,
            @RequestParam("phrase") String phrase,
            RedirectAttributes redirectAttributes) {

        if (!gameService.isGameActive() && gameService.getWinningPlayerId() != null) {
            redirectAttributes.addFlashAttribute("error",
                    "The game is over. Winner: " + gameService.getWinningPlayerId());
            return "redirect:/player/" + playerId;
        }
        if (!gameService.isGameActive()) {
            redirectAttributes.addFlashAttribute("error", "The game has not yet started.");
            return "redirect:/player/" + playerId;
        }

        boolean isBingo = gameService.processPlayerMarking(playerId, phrase);

        if (isBingo) {
            redirectAttributes.addFlashAttribute("message", "¡BINGO! You are win.");
        } else {
            // Esto recarga el tablero para ver la frase tachada y actualizar el estado
            redirectAttributes.addFlashAttribute("message", "Crossed-out phrase");
        }

        return "redirect:/player/" + playerId;
    }
}