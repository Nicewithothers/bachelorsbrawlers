import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importáljuk a CommonModule-t
import { CharacterOverviewComponent } from '../character-overview/character-overview.component';
import { CharacterComponent } from '../../shared/components/character/character.component';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { HeroService } from '../../shared/services/hero.service';
import { Hero } from '../../shared/models/hero.model';
import { NgOptimizedImage } from '@angular/common';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-leaderboard',
  standalone: true,
  imports: [
    CommonModule, // Hozzáadjuk a CommonModule-t az imports-hoz
    CharacterOverviewComponent,
    TableModule,
    ButtonModule,
    NgOptimizedImage,
    LoadingComponent,
    CharacterComponent
  ],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.scss',
})
export class LeaderboardComponent implements OnInit {
  hero!: Hero;
  heroes!: Hero[];
  selectedHero?: Hero; // Választott karakter

  constructor(private heroService: HeroService, private soundService: SoundService) {}

  ngOnInit() {
    this.getHero();
    this.getHeroesByXpDesc();
  }

  getHero() {
    return this.heroService.hero$.subscribe((hero) => {
      if (hero) {
        this.hero = hero;
        if (!this.selectedHero) {
          this.selectedHero = hero; // Beállítjuk alapértelmezettként a bejelentkezett hőst
        }
      }
    });
  }

  getHeroesByXpDesc() {
    return this.heroService.getHeroesByXpDesc().subscribe((heroes) => {
      if (this.hero && heroes.length > 0) {
        this.heroes = [...heroes];
      }
    });
  }

  selectHero(hero: Hero) {
    this.selectedHero = hero; // Kiválasztott karakter beállítása
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
