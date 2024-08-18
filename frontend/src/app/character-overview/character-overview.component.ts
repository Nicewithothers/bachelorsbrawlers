import { Component } from '@angular/core';
import { InventoryComponent } from '../../shared/components/inventory/inventory.component';
import { CharacterComponent } from '../../shared/components/character/character.component';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-character-overview',
  standalone: true,
  imports: [InventoryComponent, CharacterComponent, NgOptimizedImage],
  templateUrl: './character-overview.component.html',
  styleUrl: './character-overview.component.scss'
})
export class CharacterOverviewComponent {

}
