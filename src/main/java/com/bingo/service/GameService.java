package com.bingo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bingo.model.Player;

@Service
public class GameService {

    // --- Datos del Juego ---
    private static final List<String> JUGUETES = Arrays.asList(
            "doll", "ball", "scooter", "guitar", "puppet", "tablet", "bike", "teddy", "train set");
    private static final List<String> COLORES = Arrays.asList(
            "red", "yellow", "green", "blue", "brown", "orange", "purple", "pink");

    // Todas las posibles frases
    private static final List<String> ALL_PHRASES;
    static {
        ALL_PHRASES = JUGUETES.stream()
                .flatMap(j -> COLORES.stream().map(c -> c + " " + j))
                .collect(Collectors.toList());
        Collections.shuffle(ALL_PHRASES); // Barajamos inicialmente todas las frases posibles
    }

    // --- Estado del Juego ---
    private final Map<String, Player> playerBoards = new ConcurrentHashMap<>();
    private final List<String> historyPhrases = new ArrayList<>();
    private boolean gameActive = false;
    private String winningPlayerId = null;
    private final int MAX_PLAYERS = 30;

    // --- Gestión de Frases ---

    /**
     * Genera una frase aleatoria única.
     * 
     * @return La frase generada.
     */
    public String generateNextPhrase() {
        if (!gameActive)
            return "Juego no activo";

        // Selecciona una frase de las posibles que aún no se hayan cantado.
        List<String> remainingPhrases = ALL_PHRASES.stream()
                .filter(p -> !historyPhrases.contains(p))
                .collect(Collectors.toList());

        if (remainingPhrases.isEmpty()) {
            return "No quedan frases por cantar";
        }

        Random random = new Random();
        String newPhrase = remainingPhrases.get(random.nextInt(remainingPhrases.size()));

        historyPhrases.add(newPhrase);
        return newPhrase;
    }

    /**
     * Genera un tablero con 9 frases únicas.
     */
    private List<String> createRandomBoard() {
        Random random = new Random();
        List<String> shuffledPhrases = new ArrayList<>(ALL_PHRASES);
        Collections.shuffle(shuffledPhrases, random);
        return shuffledPhrases.subList(0, 9); // Las primeras 9
    }

    // --- Gestión de Jugadores ---

    /**
     * Simula la lectura del QR: agrega un nuevo jugador y genera su tablero.
     */
    public Player addPlayer() {
        if (playerBoards.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("Máximo de 30 jugadores alcanzado.");
        }
        String playerId = "Jugador_" + (playerBoards.size() + 1);
        Player newPlayer = new Player(playerId, createRandomBoard());
        playerBoards.put(playerId, newPlayer);
        return newPlayer;
    }

    public Player getPlayer(String id) {
        return playerBoards.get(id);
    }

    public Map<String, Player> getAllPlayers() {
        return playerBoards;
    }

    // --- Control del Juego ---

    public boolean startGame() {
        if (playerBoards.size() > 0 && !gameActive) {
            gameActive = true;
            winningPlayerId = null; // Reinicia el ganador
            historyPhrases.clear(); // Reinicia el historial
            return true;
        }
        return false;
    }

    public void endGame(String winnerId) {
        gameActive = false;
        winningPlayerId = winnerId;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public String getWinningPlayerId() {
        return winningPlayerId;
    }

    public List<String> getHistoryPhrases() {
        return historyPhrases;
    }

    /**
     * Marca una frase en el tablero del jugador y verifica si hay BINGO.
     * 
     * @return true si el jugador cantó BINGO.
     */
    public boolean processPlayerMarking(String playerId, String phrase) {
        Player player = playerBoards.get(playerId);
        if (player == null || !gameActive)
            return false;

        player.markPhrase(phrase);

        if (player.checkBingo()) {
            endGame(playerId);
            return true;
        }
        return false;
    }
}