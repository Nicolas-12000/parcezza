package com.parcezza.backend.service.payment;

import java.time.YearMonth;
import org.springframework.stereotype.Component;

@Component
public class LuhnCardValidator implements CardValidator {

    @Override
    public boolean isValid(String cardNumber, Integer expMonth, Integer expYear, String cvv) {
        if (cardNumber == null || expMonth == null || expYear == null || cvv == null) {
            return false;
        }

        String normalized = cardNumber.replaceAll("[^0-9]", "");
        if (normalized.length() < 13 || normalized.length() > 19) {
            return false;
        }

        if (!cvv.matches("\\d{3,4}")) {
            return false;
        }

        if (expMonth < 1 || expMonth > 12) {
            return false;
        }

        YearMonth exp = YearMonth.of(expYear, expMonth);
        if (exp.isBefore(YearMonth.now())) {
            return false;
        }

        return passesLuhn(normalized);
    }

    private boolean passesLuhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }
}
