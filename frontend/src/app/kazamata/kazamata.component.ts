import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { MonsterService } from '../../shared/services/monster.service';
import { Monster } from '../../shared/models/monster.model';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { HeroService } from '../../shared/services/hero.service';
import { Hero } from '../../shared/models/hero.model';
import { MessageService } from 'primeng/api';
import { FightService } from '../../shared/services/fight.service';
import { Router } from '@angular/router';
import { CarouselModule } from 'primeng/carousel';
import { interval, Subscription } from 'rxjs';
import { SoundService } from '../../shared/services/sound.service';

interface ExtendedMonster extends Monster {
  flipped: boolean;
}

@Component({
  selector: 'app-kazamata',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, LoadingComponent, CarouselModule],
  templateUrl: './kazamata.component.html',
  styleUrls: ['./kazamata.component.scss']
})
export class KazamataComponent implements OnInit, OnDestroy {
  hero!: Hero | null;
  currentPosition: number = 0;
  monsters: ExtendedMonster[] = [];
  displayKazamataTooltip = false;
  nextDungeonTryTime: string = '';
  private countdownSubscription!: Subscription;
  flippedStates: boolean[] = [];

  constructor(private heroService: HeroService,
              private monsterService: MonsterService,
              private messageService: MessageService,
              private fightService: FightService,
              private router: Router,
              private soundService: SoundService
            ) {}

  ngOnInit(): void {
    this.heroService.hero$.subscribe(hero => {
      this.hero = hero;
      if (this.hero) {
        this.startCountdown();
        this.currentPosition = this.hero.dungeonLevel - 1;
      }
    });

    this.monsterService.getAll().subscribe(monsters => {
      if (monsters) {
        this.monsters = monsters.map(monster => ({ ...monster, flipped: false }));
      } else {
        console.error('hiba');
      }
    });
  }

  ngOnDestroy(): void {
    if (this.countdownSubscription) {
      this.countdownSubscription.unsubscribe();
    }
  }

  onCarouselPage(event: any): void {
    this.currentPosition = event.page;
    this.monsters.forEach(monster => {
      monster.flipped = false;
    });
  }

  selectBoss(): void {
    console.log("monster dLevel: ",this.monsters[this.currentPosition]);
    console.log("hero dLevel: ",this.hero);
    if(this.monsters[this.currentPosition].dungeonLevel === this.hero!.dungeonLevel) {
      console.log(this.monsters[this.currentPosition]);
      this.fightService.enemy = {
        displayName: this.monsters[this.currentPosition].name,
        name: this.monsters[this.currentPosition].name,
        image: this.monsters[this.currentPosition].picture
      };
      this.router.navigate(['/fight'], {
        queryParams: {
          type: 'Monster',
        }
      });
    }
  }

  startCountdown(): void {
    const nextDungeonTryDate = new Date(this.hero!.nextDungeonTry).getTime();
    this.countdownSubscription = interval(1000).subscribe(() => {
      const now = new Date().getTime();
      const distance = nextDungeonTryDate - now;

      if (distance < 0) {
        this.nextDungeonTryTime = '';
      } else {
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);
        this.nextDungeonTryTime = `${minutes} perc ${seconds} másodperc múlva próbálkozhatsz újra!`;
      }
    });
  }

  isAttackButtonDisabled(): boolean {
    const nextDungeonTryDate = new Date(this.hero!.nextDungeonTry).getTime();
    const now = new Date().getTime();
    const distance = nextDungeonTryDate - now;

    return distance > 0 || this.monsters[this.currentPosition].dungeonLevel !== this.hero!.dungeonLevel;
  }

  toggleFlip(monster: ExtendedMonster): void {
    monster.flipped = !monster.flipped;
  }

  isAboveHeroLevel(monsterLevel: number): boolean {
    return this.hero ? monsterLevel > this.hero.dungeonLevel : false;
  }
  
  isBelowHeroLevel(monsterLevel: number): boolean {
    return this.hero ? monsterLevel < this.hero.dungeonLevel : false;
  }

  isMonsterCompleted(monster: ExtendedMonster): boolean {
    return this.hero ? monster.dungeonLevel < this.hero.dungeonLevel : false;
  }
}
