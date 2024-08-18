import { Component, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FightService } from '../../shared/services/fight.service';
import { MessageService } from 'primeng/api';
import { HeroService } from '../../shared/services/hero.service';
import { MonsterService } from '../../shared/services/monster.service';
import { firstValueFrom, Subscription, timer } from 'rxjs';
import { Monster } from '../../shared/models/monster.model';
import { Hero } from '../../shared/models/hero.model';
import { RiveModule } from 'ng-rive';
import { DynamicCanvasDirective } from '../../shared/resize.directive'
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { WebSocketService } from '../../shared/services/websocket.service';
import { Attack } from '../../shared/models/attack.model';
import { CustomAnimations } from './animations.enum';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Enemy } from '../../shared/models/enemy.model';
import { shakeHero, shakeEnemy, heroHitText, enemyHitText } from './avatar.animation';
import { Item } from '../../shared/models/item.model';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-fight',
  standalone: true,
  imports: [CommonModule, RiveModule, DynamicCanvasDirective, LoadingComponent, NgOptimizedImage],
  templateUrl: './fight.component.html',
  styleUrls: ['./fight.component.scss', '../app.component.scss'],
  animations: [shakeHero, shakeEnemy, heroHitText, enemyHitText]
})
export class FightComponent implements OnInit {
  battleLog: string[] = [];
  hero!: Hero;
  enemy!: Enemy;
  loading = true;
  riv: string = CustomAnimations[0];
  timeLine: string = 'Forward';
  start = false;
  flipped = false;
  type!: string;
  scheduleSubscription!: Subscription;
  heroHealth: number = 100;
  enemyHealth: number = 100;
  shakeEnemy = false;
  shakeHero = false;
  heroText: any = null;
  enemyText: any = null;
  equippedItem: Item | null = null;

  constructor(private websocketService: WebSocketService,
              private fightService: FightService,
              private messageService: MessageService,
              private heroService: HeroService,
              private location: Location,
              private route: ActivatedRoute,
              private soundService: SoundService,
            ) { }

  async ngOnInit(): Promise<void> {
    const params = await firstValueFrom(this.route.queryParams);
    if (!params) {
      console.error('hiba');
    }
    this.type = params['type'];
    this.enemy = this.fightService.enemy;

    const hero = await firstValueFrom(this.heroService.hero$);
    if (!hero) {
      console.error('hiba');
      return;
    }
    this.hero = hero;

    const animatedItems = hero.equipmentItems.filter(item =>
      item && Object.keys(CustomAnimations).includes(item.fileName)
    );
    if (animatedItems.length > 0) {
      this.equippedItem = animatedItems[0];
    }

    this.scheduleSubscription = this.websocketService.schedule$.subscribe(message => {
      if (message && message.value) {
        this.flipCanvas();
        const attack = message.value as Attack;
        // console.log(attack);
        if (attack.heroAttack) {
          this.start = false;
          
          if (this.equippedItem) {
            this.riv = this.equippedItem.fileName;
          } else {
            this.riv = 'fist';
          }

          if (attack.crit) {
            this.timeLine = 'ForwardCrit';
          } else {
            this.timeLine = 'Forward'
          }

          this.start = true;
          this.randomDamage().then(value => {
            this.enemyHealth -= value;
            const x = Math.random() * 100;
            const y = Math.random() * 100;
            this.enemyText = { message: attack.crit ? 'CRIT' : 'HIT', x, y };
            this.shakeEnemy = true;
            timer(100).subscribe(_ => this.shakeEnemy = false);
            timer(300).subscribe(_ => this.enemyText = null);
          });
        } else {
          this.start = false;
          this.randomAnim(attack.crit);
          this.start = true;
          this.randomDamage().then(value => {
            this.heroHealth -= value;
            const x = Math.random() * 100;
            const y = Math.random() * 100;
            this.heroText = { message: attack.crit ? 'CRIT' : 'HIT', x, y };
            this.shakeHero = true;
            timer(100).subscribe(_ => this.shakeHero = false);
            timer(300).subscribe(_ => this.heroText = null);
          });
        }
      }
    });
    this.loading = false;
  }

  flipCanvas(): void {
    const canvas = document.getElementById("attack-canvas") as HTMLCanvasElement;
    let ctx = canvas!.getContext("2d");
    ctx!.translate(canvas.width, 0);
    ctx!.scale(-1, 1);
  }

  randomAnim(crit: boolean): void {
    let anim: string;
    
    do {
      const index = Math.floor(Math.random() * 9);
      anim = CustomAnimations[index];
    } while (anim === this.equippedItem!.fileName)
    
    this.riv = anim;

    if (crit) {
      this.timeLine = 'ForwardCrit';
    } else {
      this.timeLine = 'Forward'
    }
  }

  async startFight(): Promise<void> {
    this.fightService.runFight(this.type, this.enemy.name).subscribe(value => {
      if (value?.includes('Hero')) {
        this.messageService.add({
          severity: 'success',
          summary: 'Nyertél!'
        });
        this.enemyHealth = 0;
        this.onButtonClicked('win.wav');
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Az ellenfél nyert!'
        });
        this.heroHealth = 0;
        this.onButtonClicked('lose.wav');
      }
    });
  }

  async randomDamage() {
    await firstValueFrom(timer(1000));
    return Math.floor(Math.random() * (30 - 20) + 20);
  }

  goBack(): void {
    this.location.back();
  }

  ngOnDestroy(): void {
    if (this.scheduleSubscription) {
      this.scheduleSubscription.unsubscribe();
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
