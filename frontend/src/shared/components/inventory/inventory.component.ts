import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroService } from '../../services/hero.service';
import { Hero } from '../../models/hero.model';
import { ShopService } from '../../services/shop.service';
import { ItemTooltipComponent } from '../tooltips/item-tooltip/item-tooltip.component';
import { LoadingComponent } from '../loading/loading.component';
import { NgOptimizedImage } from '@angular/common';
import { Item } from '../../models/item.model';
import { Router } from '@angular/router';
import { SoundService } from '../../../shared/services/sound.service';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [LoadingComponent, CommonModule, ItemTooltipComponent, NgOptimizedImage],
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss', '../../../app/app.component.scss']
})
export class InventoryComponent implements OnInit {
  @Input() hero!: Hero;
  @Input() fromCharacter: boolean = false;
  displayItemTooltip = false;
  tooltipIndex = 0;
  loading = true;

  constructor(private heroService: HeroService, private shopService: ShopService, private router: Router, private soundService: SoundService) { }

  ngOnInit(): void {
    this.heroService.hero$.subscribe(hero => {
      if (hero) {
        this.hero = hero;
        this.loading = false;
      }
    })
  }

  sellItem(itemName: string): void {
    if (this.router.url.includes('shop')) {
      this.loading = true;
      this.shopService.sellItem(itemName).subscribe(hero => {
        if (hero) {
          // console.log(hero);
          this.hero = hero;
          this.loading = false;
        } else {
          console.error("hiba");
        }
      });
    }
  }

  showItemTooltip(int: number) {
    this.displayItemTooltip = true;
    this.tooltipIndex = int;
  }

  hideItemTooltip(int: number) {
    this.displayItemTooltip = false;
    this.tooltipIndex = int;
  }

  onDragStart(event: DragEvent, item: any) {
    event!.dataTransfer!.setData('item', JSON.stringify(item));
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    const itemData = JSON.parse(event!.dataTransfer!.getData('item')) as Item;
    const item = this.hero.equipmentItems.find(item => item && item.name === itemData.name);
    if (item) {
      this.heroService.unEquipItem(itemData.name).subscribe(items => {
        if (items) {
          this.hero!.equipmentItems = items.equipmentItems;
          this.hero!.backpackItems = items.backpackItems;
        } else {
          console.error('Error');
        }
      });
    } else {
      console.error('Item not found in equipment items');
    }
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
