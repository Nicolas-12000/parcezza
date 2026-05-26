import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { SecurityValidators } from '../../../shared/validators/security.validators';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  errorMessage: string | null = null;
  isLoading = signal(false);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.maxLength(100),
          SecurityValidators.noHtmlTags(),
          SecurityValidators.noWhitespaceAround()
        ]
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(64),
          SecurityValidators.noHtmlTags()
        ]
      ],
      rememberMe: [false]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = null;

    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    const { email, password, rememberMe } = this.loginForm.value;
    this.authService.login({ email, password }, !!rememberMe).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.cartService.loadCart();
        this.router.navigate(['/catalog']);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage = err.error?.message || 'Invalid email or password. Please try again.';
      }
    });
  }
}
