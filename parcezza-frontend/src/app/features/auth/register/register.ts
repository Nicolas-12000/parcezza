import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { SecurityValidators } from '../../../shared/validators/security.validators';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  submitted = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      fullName: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(100),
          SecurityValidators.safeTextOnly()
        ]
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.maxLength(100),
          SecurityValidators.noHtmlTags()
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
      ]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = null;

    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        this.router.navigate(['/catalog']);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}
