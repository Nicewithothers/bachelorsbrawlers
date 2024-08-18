import { ShopService } from './../../shared/services/shop.service';
import { Component, OnInit } from '@angular/core';
import { CharacterComponent } from '../../shared/components/character/character.component';
import { InventoryComponent } from '../../shared/components/inventory/inventory.component';
import { Item } from '../../shared/models/item.model';
import { ItemService } from '../../shared/services/item.service';
import { HeroService } from '../../shared/services/hero.service';
import { Hero } from '../../shared/models/hero.model';
import { MessageService } from 'primeng/api';
import { ItemTooltipComponent } from '../../shared/components/tooltips/item-tooltip/item-tooltip.component';
import { CommonModule } from '@angular/common';
import { combineLatest, forkJoin } from 'rxjs';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { NgOptimizedImage } from '@angular/common';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-weapon-shop',
  standalone: true,
  imports: [CharacterComponent, InventoryComponent, ItemTooltipComponent, CommonModule, LoadingComponent, NgOptimizedImage],
  templateUrl: './weapon-shop.component.html',
  styleUrls: ['./weapon-shop.component.scss', '../app.component.scss'],
})
export class WeaponShopComponent implements OnInit {
  shopItems!: Item[];
  hero!: Hero;
  displayItemToolTip = false;
  tooltipIndex = 0;
  loading = true;

  constructor(private shopService: ShopService,
              private heroService: HeroService,
              private messageService: MessageService,
              private soundService: SoundService) { }

  ngOnInit(): void {
    this.getHero();
    this.getShopItems();
  }

  getHero(): void {
    this.heroService.hero$.subscribe(hero => {
      if (hero) {
        this.hero = hero;
      }
    })
  }

  getShopItems(): void {
    this.shopService.listShopItems().subscribe(items => {
      if (items && items.length > 0) {
        if (JSON.stringify(items) === JSON.stringify(this.shopItems)) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Várj még egy kicsit, hogy frissüljön a bolt kínálata'
          });
        }
        this.shopItems = [...items];

      } else {
        console.error("hiba");
      }
    });
  }

  refreshItems(): void {
    this.shopService.refreshItems().subscribe((items) => {
      if (items !== null && items.length > 0) {
      this.shopItems = [...items];
      }
    });
  }

  buyItem(item: Item): void {
    this.shopService.buyItem(item.name).subscribe(hero => {
      if (hero) {
        this.hero = hero;
        this.shopItems = this.shopItems.filter(shopItem => shopItem.name !== item.name);
        this.messageService.add({
          severity: 'success',
          summary: 'Sikeres tárgyvásárlás!'
        })
      } else {
        console.error("hiba");
        this.messageService.add({
          severity: 'error',
          summary: 'Sikertelen tárgyvásárlás!'
        })
      }
    });
  }

  showItemTooltip(int : number){
    this.displayItemToolTip = !this.displayItemToolTip;
    this.tooltipIndex = int;
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
