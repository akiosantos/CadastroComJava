package com.br.var.solutions.application.services.impl;

import com.br.var.solutions.application.services.use.MundialUseCase;

public class MundialUseCaseImpl implements MundialUseCase {

    public String calculoMundial(String time){
        return calcularMundial(time);
    }



    private String calcularMundial(String time) {

        if (time.equalsIgnoreCase("Corinthias")) {
            return "Parabéns, o seu time possui 2 mundiais de clubes conforme a FIFA";
        } else if (time.equalsIgnoreCase("São Paulo")) {
            return "Parabéns, o seu time possui 3 mundiais de clubes conforme a FIFA";
        } else if (time.equalsIgnoreCase("Santos")) {
            return "Parabéns, o seu time possui 2 mundiais de clubes conforme a FIFA";
        } else {
            return "poxa, que pena. Continue torcendo para o seu clube ganhar o mundial";
        }
    }
}
