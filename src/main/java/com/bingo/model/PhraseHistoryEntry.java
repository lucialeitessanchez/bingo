package com.bingo.model;

public class PhraseHistoryEntry {
    private String phrase;
    private String toy; // El juguete específico de la frase
    private String color; // El color específico de la frase (opcional, pero útil)

    public PhraseHistoryEntry(String phrase, String toy, String color) {
        this.phrase = phrase;
        this.toy = toy;
        this.color = color;
    }

    // Getters
    public String getPhrase() {
        return phrase;
    }

    public String getToy() {
        return toy;
    }

    public String getColor() {
        return color;
    }

    // Setters (si es necesario modificar después, aunque para historial no suele
    // serlo)
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public void setToy(String toy) {
        this.toy = toy;
    }

    public void setColor(String color) {
        this.color = color;
    }
}