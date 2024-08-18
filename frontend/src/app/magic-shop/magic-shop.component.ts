import { Component, OnInit } from '@angular/core';
import { CharacterComponent } from '../../shared/components/character/character.component';
import { InventoryComponent } from '../../shared/components/inventory/inventory.component';
import { CommonModule } from '@angular/common';
import { ShopService } from '../../shared/services/shop.service';
import { Item } from '../../shared/models/item.model';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-magic-shop',
  standalone: true,
  imports: [
    CharacterComponent,
    InventoryComponent,
    CommonModule,
    NgOptimizedImage
  ],
  templateUrl: './magic-shop.component.html',
  styleUrls: ['./magic-shop.component.scss', '../app.component.scss'],
})
export class MagicShopComponent {
  items: Item[] | null = [];

  constructor(private shopService: ShopService) {}

  ngOnInit(): void {
    this.shopService.listShopItems().subscribe((items) => {
      this.items = items;
      // console.log('items ', items);
    });
  }

  refreshItems(): void {
    this.shopService.refreshItems().subscribe((items) => {
      this.items = items;
      // console.log('items ', items);
    });
  }
}
