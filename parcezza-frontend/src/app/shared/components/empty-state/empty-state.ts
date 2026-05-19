import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  template: `
    <div class="empty-state animate-fade-in-up">
      <div class="empty-state__icon">
        <svg width="80" height="80" viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="40" cy="40" r="38" stroke="currentColor" stroke-width="1.5" stroke-dasharray="4 4" opacity="0.3"/>
          <path d="M28 52V32C28 30.9 28.9 30 30 30H50C51.1 30 52 30.9 52 32V52" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          <path d="M24 52H56" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          <path d="M34 30V26C34 24.9 34.9 24 36 24H44C45.1 24 46 24.9 46 26V30" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          <circle cx="40" cy="42" r="3" stroke="currentColor" stroke-width="2"/>
        </svg>
      </div>
      <h3 class="empty-state__title">{{ title }}</h3>
      <p class="empty-state__message">{{ message }}</p>
      <ng-content></ng-content>
    </div>
  `,
  styles: [`
    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      text-align: center;
      padding: var(--space-4xl) var(--space-lg);
      min-height: 360px;
    }

    .empty-state__icon {
      color: var(--color-secondary-light);
      margin-bottom: var(--space-lg);
      animation: pulse 2.5s ease-in-out infinite;
    }

    .empty-state__title {
      font-family: var(--font-display);
      font-size: 1.5rem;
      color: var(--color-primary);
      margin-bottom: var(--space-sm);
    }

    .empty-state__message {
      color: var(--color-secondary);
      font-size: 0.9375rem;
      max-width: 360px;
      line-height: 1.6;
      margin-bottom: var(--space-xl);
    }
  `]
})
export class EmptyStateComponent {
  @Input() title = 'Nothing here yet';
  @Input() message = 'Check back later for new items.';
}
