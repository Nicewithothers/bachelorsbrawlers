import { Component, Input, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Quest } from '../../shared/models/quest.model';
import { TavernService } from '../../shared/services/tavern.service';
import { HeroService } from '../../shared/services/hero.service';
import { Hero } from '../../shared/models/hero.model';
import { MessageService } from "primeng/api";
import { Router } from '@angular/router';
import { FightService } from '../../shared/services/fight.service';
import {LoadingComponent} from "../../shared/components/loading/loading.component";
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-kocsma',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, LoadingComponent],
  templateUrl: './tavern.component.html',
  styleUrls: ['./tavern.component.scss', '../app.component.scss'],
})
export class TavernComponent implements OnInit {
  quests!: Quest[];
  selectedQuest!: Quest;

  @Input()
  hero!: Hero;

  constructor(
    private router: Router,
    private tavernService: TavernService,
    private heroService: HeroService,
    private fightService: FightService,
    private messageService: MessageService,
    private soundService: SoundService,
  ) {
  }

  ngOnInit(): void {
    this.getHero();
    this.getQuests();
  }

  onSelectQuest(selectedQuest: Quest) {
    this.selectedQuest = selectedQuest;
  }

  getHero() {
    this.heroService.hero$.subscribe((hero) => {
      if (hero) {
        this.hero = hero;
      }
    });
  }

  getQuests(): void {
    this.tavernService.getQuests().subscribe((quests) => {
      if (quests && quests.length > 0) {
        this.quests = [...quests];
      } else {
        console.error('hiba');
      }
    });
  }

  chooseQuest() {
    this.fightService.enemy = {
      displayName: this.selectedQuest.monsterName,
      name: this.selectedQuest.monsterType,
      image: this.selectedQuest.image
    };
    this.router.navigate(['/fight'], {
      queryParams: {
        type: 'Grunt',
      }
    });
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
