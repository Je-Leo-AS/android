package com.example.academia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Exercicio {
    private String fNome;
    private LocalDate fData;
    private String fLocal;
    private String fComentarios;
    private boolean fFavorito;
    private float fAval;

    public Exercicio() {
        this.fNome = "";
        this.fData = LocalDate.now();
        this.fLocal = "";
        fComentarios = "";
        fFavorito = false;
        fAval = 1;
    }
    public Exercicio(String fNome, LocalDate fData, String fLocal) {
        this.fNome = fNome;
        this.fData = fData;
        this.fLocal = fLocal;
        fComentarios = "";
        fFavorito = false;
        fAval = 1;
    }

    public Exercicio(String fNome, int fDia, int fMes, int fAno, String fLocal, String fComentarios) {
        this.fNome = fNome;
        this.fData = LocalDate.of(fAno, fMes, fDia);
        this.fLocal = fLocal;
        this.fComentarios = fComentarios;
        fFavorito = false;
        fAval = 1;
    }

    public Exercicio(String fNome, LocalDate fData, String fLocal, String fComentarios) {
        this.fNome = fNome;
        this.fData = fData;
        this.fLocal = fLocal;
        this.fComentarios = fComentarios;
        fFavorito = false;
        fAval = 1;
    }

    public String getfNome() {
        return fNome;
    }

    public void setfNome(String fNome) {
        this.fNome = fNome;
    }

    public LocalDate getfData() {
        return fData;
    }

    public String getfDataStr() {
        return fData.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }

    public void setfData(LocalDate fData) {
        this.fData = fData;
    }

    public String getfLocal() {
        return fLocal;
    }

    public void setfLocal(String fLocal) {
        this.fLocal = fLocal;
    }

    public String getfComentarios() {
        return fComentarios;
    }

    public void setfComentarios(String fComentarios) {
        this.fComentarios = fComentarios;
    }

    public boolean isfFavorito() {
        return fFavorito;
    }

    public void setfFavorito(boolean fFavorito) {
        this.fFavorito = fFavorito;
    }

    public float getfAval() {
        return fAval;
    }

    public void setfAval(float fAval) {
        this.fAval = fAval;
    }

    @Override
    public int compareTo(Exercicio filme) {
        return this.fNome.compareToIgnoreCase(filme.getfNome());
    }
}
