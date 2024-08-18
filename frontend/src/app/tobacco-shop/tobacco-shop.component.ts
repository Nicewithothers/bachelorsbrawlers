import {Component, OnInit} from '@angular/core';
import {Hero} from "../../shared/models/hero.model";
import {HeroService} from "../../shared/services/hero.service";
import {MessageService} from "primeng/api";
import { TobaccoShopService } from '../../shared/services/tobacco-shop.service';
import { BoosterItem } from '../../shared/models/boosteritem.model';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { NgOptimizedImage } from '@angular/common';
import { SoundService } from '../../shared/services/sound.service';


@Component({
  selector: 'app-tobacco-shop',
  standalone: true,
  imports: [LoadingComponent, NgOptimizedImage],
  templateUrl: './tobacco-shop.component.html',
  styleUrl: './tobacco-shop.component.scss'
})
export class TobaccoShopComponent implements OnInit {
  boosterItems: BoosterItem[] = [];
  hero!: Hero;
  selectedBoosterItem!: BoosterItem;
  loading = true;

  constructor(private tobaccoShopService: TobaccoShopService,
              private heroService: HeroService,
              private messageService: MessageService,
              private soundService: SoundService) {
  }

  ngOnInit() {
    this.getHero();
    this.getBoosterItems();
  }

  getHero() {
    this.heroService.hero$.subscribe(hero => {
      if (hero) {
        this.hero = hero;
      }
    });
  }

  getBoosterItems() {
    this.tobaccoShopService.listBoosterItems().subscribe(bitem => {
      if (bitem && bitem.length > 0) {
        this.boosterItems = [...bitem];
        this.loading = false;
      } else {
        console.error("Nincs booster.");
        this.loading = false;
      }
    });
  }

  updateBoosterItem(boosterItemName: string) {
    this.tobaccoShopService.updateBoosterItem(boosterItemName).subscribe(hero => {
      if (hero) {
        this.hero = hero;
        this.messageService.add({
          severity: 'success',
          summary: 'Sikeres italvétel!',
        })
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Sikertelen italvétel!',
        })
      }
    })
  }

  onSelect(bitem: BoosterItem = this.boosterItems[0]) {
    this.selectedBoosterItem = bitem;
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
