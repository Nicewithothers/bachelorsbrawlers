import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-separator',
  standalone: true,
  imports: [],
  templateUrl: './separator.component.html',
  styleUrl: './separator.component.scss',
})
export class SeparatorComponent {
  @Input() width: number = 492;
  @Input() height: number = 25;
  @Input() orientation: 'horizontal' | 'vertical' = 'horizontal';

  get firstLineEnd(): number {
    return this.width / 2 - this.width / 10;
  }

  get secondLineStart(): number {
    return this.width / 2 + this.width / 10;
  }

  get circlePosition(): number {
    // Calculate the x position of the circle based on the width of the SVG
    // You can adjust this calculation as needed for your specific requirements
    return this.width / 2;
  }

  get stroke(): number {
    return this.width < 150 ? 1 : 2;
  }

  get radius(): number {
    if (this.width < 200) {
      return Math.min(this.width / 20, 8);
    } else {
      return Math.max(this.width / 40, 10);
    }
  }
}
