package com.bingo.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bingo.model.PhraseHistoryEntry;
import com.bingo.service.GameService;
import com.bingo.service.QrCodeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private GameService gameService;

    @Autowired // ¡NUEVO!
    private QrCodeService qrCodeService;
    

    // Muestra la vista de control
    // Muestra la vista de control
    @GetMapping("/view")
    public String showTeacherView(Model model) {
        model.addAttribute("historyPhrases", gameService.getHistoryPhrases());
        model.addAttribute("playerCount", gameService.getAllPlayers().size());
        model.addAttribute("maxPlayers", 30);
        model.addAttribute("gameActive", gameService.isGameActive());
        model.addAttribute("winnerId", gameService.getWinningPlayerId());

        return "teacher_view";
    }

    private String getLocalIpAddress() throws SocketException {
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
        NetworkInterface iface = interfaces.nextElement();
        if (iface.isLoopback() || !iface.isUp()) continue;

        Enumeration<InetAddress> addresses = iface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress addr = addresses.nextElement();
            if (addr instanceof Inet4Address) {
                return addr.getHostAddress();
            }
        }
    }
    return "127.0.0.1";
}


    /**
     * Genera dinámicamente el código QR con la URL de unión al juego.
     */
    @GetMapping("/qr-code.png")
    public ResponseEntity<byte[]> getQrCode(HttpServletRequest request) {
        try {
            // Detecta la IP de la máquina donde se está ejecutando el servidor
            String ipAddress = getLocalIpAddress();
            int port = request.getServerPort();

            // Construye la URL base automáticamente
            String baseUrl = "http://" + ipAddress + ":" + port;
            String joinUrl = baseUrl + "/player/join";

            // Genera el QR
            byte[] qrCodeBytes = qrCodeService.generateQrCode(joinUrl, 300, 300);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    // Genera la siguiente frase (simula la lectura del QR)
    @PostMapping("/next-phrase")
    public String generatePhrase(RedirectAttributes redirectAttributes) {
        if (!gameService.isGameActive()) {
            redirectAttributes.addFlashAttribute("error", "El juego no está activo.");
        } else if (gameService.getWinningPlayerId() != null) {
            redirectAttributes.addFlashAttribute("error", "El juego ha terminado.");
        } else {
            PhraseHistoryEntry newEntry = gameService.generateNextPhrase();
            if (newEntry != null) {
                String phrase = newEntry.getPhrase(); // ejemplo: "brown bike"
                String[] parts = phrase.split(" ", 2);
                if (parts.length == 2) {
                    String color = parts[0]; // "brown"
                    String object = parts[1]; // "bike"
                    redirectAttributes.addFlashAttribute("newCardColor", color);
                    redirectAttributes.addFlashAttribute("newCardObject", object);
                    redirectAttributes.addFlashAttribute("newCardText", phrase);
                } else {
                    redirectAttributes.addFlashAttribute("newCardText", phrase);
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "No quedan frases por cantar.");
            }
        }
        return "redirect:/teacher/view";
    }

    /**
     * Acción para resetear el juego completamente (opcional)
     */
    @PostMapping("/reset")
    public String resetGame() {
        // Llama al nuevo método del servicio que gestiona el reinicio total
        gameService.resetGameState();

        return "redirect:/teacher/view"; // Redirige a la vista de la docente
    }
}