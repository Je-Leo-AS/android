package com.example.termo;

public class Termo {

        private String termo; // TERMO em sí, sem acentos
        private String palavra; // palavra acentuando

        public Termo(String palavra, String termo) {
            this.palavra = palavra;
            this.termo = termo;
        }

        public String getTermo() {
            return termo;
        }

        public void setTermo(String termo) {
            this.termo = termo;
        }

        public String getPalavra() {
            return palavra;
        }

        public void setPalavra(String palavra) {
            this.palavra = palavra;
        }

        @Override
        public String toString() {
            return palavra;
        }
    }

}
