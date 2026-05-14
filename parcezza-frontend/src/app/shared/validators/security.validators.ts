import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class SecurityValidators {
  /**
   * Validates that the input does not contain common XSS or injection patterns
   * like <script>, javascript:, etc.
   */
  static noHtmlTags(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) {
        return null;
      }
      // Simple regex to catch basic HTML tags or script injection attempts
      const hasTags = /<[^>]*>|javascript:|onerror=|onload=/i.test(value);
      return hasTags ? { hasHtmlTags: true } : null;
    };
  }

  /**
   * Validates that the input only contains alphanumeric characters and basic punctuation.
   * Useful for names, simple text fields.
   */
  static safeTextOnly(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) {
        return null;
      }
      // Allows letters, numbers, spaces, and basic punctuation (-.,')
      const isSafe = /^[\w\s\-\.,']+$/.test(value);
      return isSafe ? null : { unsafeText: true };
    };
  }

  /**
   * Validates there is no leading or trailing whitespace.
   */
  static noWhitespaceAround(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (typeof value === 'string') {
        if (value.startsWith(' ') || value.endsWith(' ')) {
          return { whitespaceAround: true };
        }
      }
      return null;
    };
  }
}
