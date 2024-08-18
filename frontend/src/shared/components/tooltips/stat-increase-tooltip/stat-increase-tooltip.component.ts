import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stat-increase-tooltip',
  standalone: true,
  imports: [],
  templateUrl: './stat-increase-tooltip.component.html',
  styleUrls: ['./stat-increase-tooltip.component.scss', '../../../../app/app.component.scss']
})
export class StatIncreaseTooltipComponent {
  @Input() increaseCost!: number;
}
