import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skeleton-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="skeleton-card" [style.animation-delay]="delay + 'ms'">
      <div class="skeleton skeleton-image"></div>
      <div style="padding: var(--space-md);">
        <div class="skeleton skeleton-text lg"></div>
        <div class="skeleton skeleton-text" style="width: 80%"></div>
        <div class="skeleton skeleton-text sm" style="margin-top: var(--space-md);"></div>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
    }
    .skeleton-card {
      background: var(--color-surface);
      border-radius: var(--rounded-lg);
      border: 1px solid var(--color-border);
      overflow: hidden;
    }
    .skeleton-image {
      height: 220px;
      border-radius: 0;
    }
  `]
})
export class SkeletonCardComponent {
  @Input() delay = 0;
}
