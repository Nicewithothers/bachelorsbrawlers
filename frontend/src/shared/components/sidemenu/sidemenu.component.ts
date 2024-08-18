import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {SeparatorComponent} from '../separator/separator.component';
import {AuthService} from '../../services/auth.service';
import {MessageService} from 'primeng/api';
import {WebSocketService} from '../../services/websocket.service';
import {Hero} from "../../models/hero.model";
import {HeroService} from "../../services/hero.service";
import {CharacterTooltipComponent} from '../tooltips/character-tooltip/character-tooltip.component';
import {CommonModule} from '@angular/common';
import {Subscription} from "rxjs";
import { LoadingComponent } from '../loading/loading.component';
import { NgOptimizedImage } from '@angular/common';
import {MailService} from "../../services/mail.service";
import { SoundService } from '../../services/sound.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { HelpComponent } from '../../../app/help/help.component';

@Component({
  selector: 'sidemenu',
  standalone: true,
  imports: [RouterLink, SeparatorComponent, CharacterTooltipComponent, CommonModule, LoadingComponent, NgOptimizedImage],
  providers: [DialogService],
  templateUrl: './sidemenu.component.html',
  styleUrl: './sidemenu.component.scss',
})
export class SidemenuComponent implements OnInit, OnDestroy {
  hero!: Hero;
  displayCharacterTooltip = false;
  unreadCount: number | null = null;
  private subscriptions: Subscription[] = [];
  ref: DynamicDialogRef | undefined;

  constructor(private auth: AuthService,
              private messageService: MessageService,
              private router: Router,
              private heroService: HeroService,
              private webSocketService: WebSocketService,
              private dialogService: DialogService,
              private mailService: MailService,
              private soundService: SoundService) {
  }

  redirectToYoutube() {
    window.open('https://www.youtube.com/watch?v=dQw4w9WgXcQ&autoplay=1', '_blank');
  }

  ngOnInit() {
    this.getHero();
    this.fetchUnreadCount();
    this.subscriptions.push(this.mailService.unreadCount$.subscribe({
      next: (count) => {
        this.unreadCount = count;
      }
    }));
    this.subscriptions.push(this.webSocketService.forint$.subscribe(
      (message: any) => {
        if (message) {
          // console.log(message.value);

          this.hero.forint = message.value;
        }
      }
    ));
    this.subscriptions.push(this.webSocketService.crypto$.subscribe(
      (message: any) => {
        if (message) {
          this.hero.crypto = message.value;
        }
      }
    ));
    this.subscriptions.push(this.webSocketService.schedule$.subscribe(
      (message: any) => {
        if (message) {
          console.log(message.value);

        }
      }
    ));
  }

  fetchUnreadCount() {
    this.subscriptions.push(this.mailService.getUnread().subscribe({
      next: (count) => {
        this.unreadCount = count;
      },
      error: (error) => {
        console.error('Failed to fetch initial unread count', error);
      }
    }));
  }

  getHero() {
    this.subscriptions.push(this.heroService.hero$.subscribe(hero => {
      if (hero) {
        this.hero = hero;
      }
    }));
  }

  signOut(): void {
    this.subscriptions.push(this.auth.signOut().subscribe((value) => {
      if (value) {
        this.messageService.add({
          severity: 'success',
          summary: 'Sikeresen kijelentkeztél',
        });
        this.router.navigate(['/login']);
      }
    }));
  }

  showCharacterTooltip() {
      this.displayCharacterTooltip = !this.displayCharacterTooltip;
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  showHelp() {
    this.ref = this.dialogService.open(HelpComponent, {
      width: '50vw',
      height: '50vw',
      contentStyle: { overflow: 'auto' },
      header: "Nem tudod merre van az előre a SZIN után, itt egy kis segítség:",
      modal:true,
    });
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}

