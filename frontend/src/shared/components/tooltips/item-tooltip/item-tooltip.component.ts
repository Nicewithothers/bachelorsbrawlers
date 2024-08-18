import { Component, Input } from '@angular/core';
import { NgIf, NgStyle, NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-item-tooltip',
  standalone: true,
  imports: [NgStyle, NgIf, NgOptimizedImage],
  templateUrl: './item-tooltip.component.html',
  styleUrls: [
    './item-tooltip.component.scss',
    '../../../../app/app.component.scss',
  ],
})
export class ItemTooltipComponent {
  @Input() item!: any;
  @Input() bottom = false;

  getItemColor(rarity: string): string {
    switch (rarity) {
      case 'COMMON':
        return '#545454';
      case 'RARE':
        return '#036612';
      case 'UNIQUE':
        return '#054fa3';
      case 'MASTER':
        return '#58048c';
      case 'LEGENDARY':
        return '#7a0101';
      default:
        return 'black';
    }
  }
}
