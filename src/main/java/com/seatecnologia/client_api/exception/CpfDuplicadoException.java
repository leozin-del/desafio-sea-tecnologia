package com.seatecnologia.client_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CpfDuplicadoException extends RuntimeException {

    public CpfDuplicadoException() {
        super("Já existe um cliente cadastrado com o CPF informado.");
    }
}
