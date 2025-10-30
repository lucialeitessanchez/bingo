package com.bingo.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String id;
    private final String name;
    private final List<String> board; // 9 frases
    private final List<Boolean> markedStatus; // Estado de tachado (true/false)

    public Player(String id, String name, List<String> phrases) {
        this.id = id;
        this.name = name;
        this.board = phrases;
        this.markedStatus = new ArrayList<>();
        // Inicializa los 9 estados como no marcados (false)
        for (int i = 0; i < 9; i++) {
            this.markedStatus.add(false);
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getBoard() {
        return board;
    }

    public List<Boolean> getMarkedStatus() {
        return markedStatus;
    }

    /**
     * Tacha una frase si está en el tablero.
     * 
     * @param phrase La frase a marcar.
     * @return true si se marcó, false si no se encontró.
     */
    public boolean markPhrase(String phrase) {
        int index = board.indexOf(phrase);
        if (index != -1 && !markedStatus.get(index)) {
            markedStatus.set(index, true);
            return true;
        }
        return false;
    }

    /**
     * Verifica si se ha cantado BINGO (las 9 frases tachadas).
     */
    public boolean checkBingo() {
        return markedStatus.stream().allMatch(status -> status);
    }

    public String getName() {
        return name;
    }
}