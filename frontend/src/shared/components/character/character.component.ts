import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule, NgOptimizedImage, NgStyle } from '@angular/common';
import { HeroService } from '../../services/hero.service';
import { Hero } from '../../models/hero.model';
import { firstValueFrom } from 'rxjs';
import { StatIncreaseTooltipComponent } from '../tooltips/stat-increase-tooltip/stat-increase-tooltip.component';
import { CharacterTooltipComponent } from '../tooltips/character-tooltip/character-tooltip.component';
import { ItemTooltipComponent } from '../tooltips/item-tooltip/item-tooltip.component';
import { SeparatorComponent } from '../separator/separator.component';
import { LoadingComponent } from '../loading/loading.component';
import { Item } from '../../models/item.model';
import { SoundService } from '../../../shared/services/sound.service';

@Component({
  selector: 'app-character',
  standalone: true,
  imports: [
    CommonModule,
    StatIncreaseTooltipComponent,
    CharacterTooltipComponent,
    ItemTooltipComponent,
    NgOptimizedImage,
    SeparatorComponent,
    LoadingComponent,
    NgStyle,
  ],
  templateUrl: './character.component.html',
  styleUrls: ['./character.component.scss', '../../../app/app.component.scss'],
})
export class CharacterComponent implements OnInit {
  @Input() hero!: Hero | null; // Hero bemenetként
  @Input() fromLeaderboardOrShop: boolean = false; // Új bemenet a leaderboard jelzésére
  displayStatIncreaseTooltip = false;
  displayCharacterTooltip = false;
  displayItemTooltip = false;
  tooltipIndex = 0;

  nextCosts: { [key: string]: number } = {
    endurance: 0,
    diligence: 0,
    intelligence: 0,
    dexterity: 0,
    luck: 0,
  };

  constructor(private heroService: HeroService, private soundService: SoundService) {}

  ngOnInit() {
    if (!this.hero) {
      this.heroService.hero$.subscribe(hero => {
        this.hero = hero;
        this.getStatIncreaseCosts();
      });
    } else {
      this.getStatIncreaseCosts();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['hero'] && changes['hero'].currentValue) {
      this.getStatIncreaseCosts();
    }
  }

  getStatIncreaseCosts() {
    this.getStatIncreaseCost('endurance');
    this.getStatIncreaseCost('diligence');
    this.getStatIncreaseCost('intelligence');
    this.getStatIncreaseCost('dexterity');
    this.getStatIncreaseCost('luck');
  }


  getStatIncreaseCost(statName: string) {
    this.heroService.getNextStatIncreaseCost(statName).subscribe((cost) => {
      if (cost) {
        this.nextCosts[statName] = cost;
      } else {
        console.error('error');
      }
    });
  }

  increaseStat(statName: string) {
    this.heroService.increaseHeroStat(statName).subscribe((stat) => {
      if (stat) {
        this.getStatIncreaseCost(statName);
        // console.log('Message:', hero);
        // this.hero = hero;
        switch (statName) {
          case "endurance": this.hero!.endurance = stat;
            break;
          case "diligence": this.hero!.diligence = stat;
            break;
          case "intelligence": this.hero!.intelligence = stat;
            break;
          case "dexterity": this.hero!.dexterity = stat;
            break;
          case "luck": this.hero!.luck = stat;
            break;
          default: throw new Error("Nem létező stat!");
        }
      } else {
        console.error('error');
      }
    });
  }

  showStatIncreaseTooltip() {
    this.displayStatIncreaseTooltip = !this.displayStatIncreaseTooltip;
  }
  showCharacterTooltip() {
    this.displayCharacterTooltip = !this.displayCharacterTooltip;
  }
  showItemTooltip(int: number) {
    this.displayItemTooltip = true;
    this.tooltipIndex = int;
  }

  hideItemTooltip(int: number) {
    this.displayItemTooltip = false;
    this.tooltipIndex = int;
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  onDrop(event: DragEvent, slotIndex: number) {
    event.preventDefault();
    const itemData = JSON.parse(event!.dataTransfer!.getData('item')) as Item;
    // console.log(itemData);
    this.heroService.equipItem(itemData.name).subscribe(items => {
      if (items) {
        this.hero!.equipmentItems = items.equipmentItems;
        this.hero!.backpackItems = items.backpackItems;
      } else {
        console.error('Error');
      }
    });
  }

  onDragStart(event: DragEvent, item: any) {
    event!.dataTransfer!.setData('item', JSON.stringify(item));
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
