import { Component, Input, input } from '@angular/core';

@Component({
  selector: 'app-character-tooltip',
  standalone: true,
  imports: [],
  templateUrl: './character-tooltip.component.html',
  styleUrls: ['./character-tooltip.component.scss', '../../../../app/app.component.scss']
})
export class CharacterTooltipComponent {
  @Input() hero!: any;
  // @Input() heroName!: string;
  // @Input() heroLevel!: number;
  // @Input() currentXP!: bigint;
  // //TODO nextLevelXP bigint kell hogy legyen miutan megvan a hero modelben
  // @Input() nextLevelXP!: number;
}
