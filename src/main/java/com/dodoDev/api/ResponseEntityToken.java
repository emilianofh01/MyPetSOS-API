package com.dodoDev.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Table(name = "UserToken")
public class ResponseEntityToken {
    private String TextError;
    private HttpStatus response;
    private int status;

    public ResponseEntityToken(String textError, HttpStatus error) {
        TextError = textError;
        this.status = error.value();
        this.response = error;
    }

    static public ResponseEntity<Object> TokenError(String errorText, HttpStatus code) {
        return new ResponseEntity<>(new ResponseEntityToken(errorText, code), code);
    }

}
