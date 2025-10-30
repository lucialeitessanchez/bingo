package com.bingo.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bingo.model.PhraseHistoryEntry;
import com.bingo.model.Player;

@Service
public class GameService {

    // --- Datos del Juego ---
    private static final List<String> JUGUETES = Arrays.asList(
            "doll", "ball", "scooter", "guitar", "puppet", "tablet", "bike", "teddy", "train-set");
    private static final List<String> COLORES = Arrays.asList(
            "red", "yellow", "green", "blue", "brown", "orange", "purple", "pink");

    // Todas las posibles combinaciones de Juguete-Color
    private static final List<AbstractMap.SimpleEntry<String, String>> ALL_COMBINATIONS; // Almacenará pares (color,
                                                                                         // juguete)
    static {
        ALL_COMBINATIONS = new ArrayList<>();
        for (String juguete : JUGUETES) {
            for (String color : COLORES) {
                ALL_COMBINATIONS.add(new AbstractMap.SimpleEntry<>(color, juguete));
            }
        }
        Collections.shuffle(ALL_COMBINATIONS); // Barajamos inicialmente todas las combinaciones
    }

    // --- Estado del Juego ---
    private final Map<String, Player> playerBoards = new ConcurrentHashMap<>();
    // Modificado para usar PhraseHistoryEntry
    private final List<PhraseHistoryEntry> historyPhrases = new ArrayList<>(); // ¡CAMBIADO!
    private boolean gameActive = false;
    private String winningPlayerId = null;
    private final int MAX_PLAYERS = 30;

    // --- Gestión de Frases ---

    /**
     * Genera una frase aleatoria única.
     * 
     * @return La frase generada.
     */
    public PhraseHistoryEntry generateNextPhrase() { // ¡CAMBIADO el tipo de retorno!
        if (!gameActive)
            return null; // Retornar null o lanzar excepción si el juego no está activo

        // Selecciona una combinación de juguete-color que aún no se haya cantado
        List<AbstractMap.SimpleEntry<String, String>> remainingCombinations = ALL_COMBINATIONS.stream()
                .filter(combo -> !historyPhrases.stream()
                        .map(PhraseHistoryEntry::getPhrase)
                        .collect(Collectors.toList())
                        .contains(combo.getKey() + " " + combo.getValue()))
                .collect(Collectors.toList());

        if (remainingCombinations.isEmpty()) {
            // Podrías lanzar una excepción o retornar un mensaje especial si no quedan
            // frases
            return null; // O un PhraseHistoryEntry especial para indicar que no hay más frases
        }

        Random random = new Random();
        AbstractMap.SimpleEntry<String, String> selectedCombo = remainingCombinations
                .get(random.nextInt(remainingCombinations.size()));

        String newPhraseText = selectedCombo.getKey() + " " + selectedCombo.getValue();
        PhraseHistoryEntry newEntry = new PhraseHistoryEntry(newPhraseText, selectedCombo.getValue(),
                selectedCombo.getKey());

        historyPhrases.add(newEntry);
        return newEntry;
    }

    /**
     * Genera un tablero con 9 frases únicas.
     */
    private List<String> createRandomBoard() {
        Random random = new Random();
        List<AbstractMap.SimpleEntry<String, String>> shuffledCombinations = new ArrayList<>(ALL_COMBINATIONS);
        Collections.shuffle(shuffledCombinations, random);

        return shuffledCombinations.stream()
                .limit(9) // Toma solo 9
                .map(combo -> combo.getKey() + " " + combo.getValue()) // Convierte a frase
                .collect(Collectors.toList());
    }

    // --- Gestión de Jugadores ---

    /**
     * Simula la lectura del QR: agrega un nuevo jugador y genera su tablero.
     */
    public Player addPlayer(String playerName) {
        if (playerBoards.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("Máximo de 30 jugadores alcanzado.");
        }

        // Usamos el nombre ingresado por el jugador como ID
        // Si quieres mantener un ID único separado del nombre, puedes combinarlo con un
        // UUID
        String playerId = playerName + "_" + (playerBoards.size() + 1);

        Player newPlayer = new Player(playerId, playerName, createRandomBoard());
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

    // Modificado para retornar la lista de PhraseHistoryEntry
    public List<PhraseHistoryEntry> getHistoryPhrases() { // ¡CAMBIADO el tipo de retorno!
        return historyPhrases;
    }

    /**
     * Reinicia el estado completo del juego a su valor inicial,
     * borrando jugadores, historial y restableciendo las banderas.
     */
    public void resetGameState() {
        this.playerBoards.clear(); // Borra todos los jugadores
        this.historyPhrases.clear(); // Borra el historial de frases
        this.gameActive = false; // ¡ESTO ES CLAVE! El juego vuelve a estado inactivo (Esperando)
        this.winningPlayerId = null; // Borra el ganador

        // Opcional: Si el juego debe tener las combinaciones barajadas de nuevo:
        // Collections.shuffle(ALL_COMBINATIONS);
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