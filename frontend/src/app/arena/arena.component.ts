import { Component, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ArenaService } from '../../shared/services/arena.service';
import { Hero } from '../../shared/models/hero.model';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { FightService } from '../../shared/services/fight.service';
import { Router } from '@angular/router';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-arena',
  templateUrl: './arena.component.html',
  standalone: true,
  styleUrls: ['./arena.component.scss', '../app.component.scss'],
  imports: [CommonModule, NgOptimizedImage, LoadingComponent],
})
export class ArenaComponent implements OnInit {
  enemies!: Hero[];
  selectedEnemy!: Hero;

  constructor(
    private arenaService: ArenaService, 
    private fightService: FightService, 
    private router: Router,
    private soundService: SoundService,
  ) { }

  ngOnInit(): void {
    this.arenaService.getOpponents().subscribe(enemies => {
      if (enemies) {
        this.enemies = enemies;
      } else {
        console.error('hiba');
      }
    })
  }

  onSelectEnemy(enemy: Hero): void {
    this.selectedEnemy = enemy;
  }

  chooseEnemy() {
    this.fightService.enemy = {
      displayName: this.selectedEnemy.name,
      name: this.selectedEnemy.name,
      image: this.selectedEnemy.picture
    };
    this.router.navigate(['/fight'], {
      queryParams: {
        type: 'Hero',
      }
    });
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
