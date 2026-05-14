package com.parcezza.backend.service.payment;

public interface CardValidator {
    boolean isValid(String cardNumber, Integer expMonth, Integer expYear, String cvv);
}
