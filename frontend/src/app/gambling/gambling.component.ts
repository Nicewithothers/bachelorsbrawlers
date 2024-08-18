import { AfterViewChecked, ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { map, subscribeOn, Subscription, timer } from 'rxjs';
import { SC_CONFIG, SCRATCH_TYPE, ScratchCard } from 'scratchcard-js';
import { KeyValuePipe } from '@angular/common';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { HeroService } from '../../shared/services/hero.service';
import { Farao } from '../../shared/models/farao.model';
import { MessageService } from 'primeng/api';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-gambling',
  standalone: true,
  imports: [KeyValuePipe, LoadingComponent],
  templateUrl: './gambling.component.html',
  styleUrl: './gambling.component.scss'
})
export class GamblingComponent implements AfterViewChecked {
  loading = false;
  completed = false;
  created = false;
  didWin = true;
  scartchCardGenerated = false;
  lastWin: number | null = null;
  testItem: null | Farao = null;

  getLastWinSubscription!: Subscription;
  startGamblingSubscription!: Subscription;
  claimVictorySubscription!: Subscription;

  constructor(private heroService: HeroService, private messageService: MessageService, private soundService: SoundService) { }

  ngOnInit() {
    this.loading = true;
    this.getLastWinSubscription = this.heroService.getLastWin().subscribe(res => {
      // console.log(res);
      if (res != -1) {
        this.lastWin = res;
      }
      this.loading = false;
    });
  }

  ngAfterViewChecked(): void {
    if (this.testItem && !this.loading && !this.created) {
      this.createCard();
    }
  }

  buyKaparos(): void {
    if (this.startGamblingSubscription) {
      this.startGamblingSubscription.unsubscribe();
    }
    this.scartchCardGenerated = false;
    this.loading = true;
    this.startGamblingSubscription = this.heroService.startGambling().subscribe(res => {
      if (res) {
        // console.log(res);
        this.testItem = res;
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Nincs elég pénzed a szerencsejátékhoz!'
        });
        this.onButtonClicked('lose.wav');
      }
      this.loading = false;
    });
  }

  accept(): void {
    if (this.claimVictorySubscription) {
      this.claimVictorySubscription.unsubscribe();
    }
    this.loading = true;
    this.claimVictorySubscription = this.heroService.claimVictory().subscribe(res => {
      this.lastWin = null;
      this.testItem = null;
      this.completed = false;
      this.created = false;
      this.loading = false;
      this.scartchCardGenerated = false;
    });
  }

  createCard(): void {
    if (!this.scartchCardGenerated) {
      this.scartchCardGenerated = true;
      // const leftInfo = document.querySelector('#left-info');
      const numbersHtml = this.testItem!.playerNumbers.map((number: number) => `<div>${number}</div>`).join('');
      const config: SC_CONFIG = {
        scratchType: SCRATCH_TYPE.SPRAY,
        containerWidth: 933.14,
        containerHeight: 500,
        brushSrc: "",
        imageForwardSrc: "../../assets/images/farao.webp",
        imageBackgroundSrc: "",
        htmlBackground: `
        <div style="height: 500px; z-index: -1; position: absolute">
          <img style="height: 100%; z-index: -1; position: absolute" src="../../assets/images/faraoLekapart.webp" alt="Lekapart farao" />
          <div class="numbers">
            ${numbersHtml}
          </div>
        </div>
      `,
        clearZoneRadius: 25,
        nPoints: 45,
        pointSize: 4,
        percentToFinish: 17,
        enabledPercentUpdate: true,
        callback: () => {
          this.completed = true;
          this.didWin = this.testItem!.allWinnings > 0;
        }
      };
      const scratchCard = new ScratchCard('#scratch-container', config);
      scratchCard.init().then(() => {
        this.created = true;
        // scratchCard.canvas.addEventListener("scratch.move", () => {
        //   let percent = scratchCard.getPercent().toFixed(0);
        //   leftInfo!.innerHTML = percent + "%";
        // });
      })
        .catch((error) => {
          alert(error.message);
        });
    }
  }

  ngOnDestroy(): void {
    if (this.getLastWinSubscription) {
      this.getLastWinSubscription.unsubscribe();
    }
    if (this.startGamblingSubscription) {
      this.startGamblingSubscription.unsubscribe();
    }
    if (this.claimVictorySubscription) {
      this.claimVictorySubscription.unsubscribe();
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}