package com.jan.coin.parqetparser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParseEmailException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final String message;
    
    @Override
    public String getMessage() {
        return this.message;
    }
}
