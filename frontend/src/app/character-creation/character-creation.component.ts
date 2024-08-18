import {Component, OnInit, ViewChild} from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { StepperComponent } from '../../shared/components/stepper/stepper.component';
import { HeroService } from '../../shared/services/hero.service';
import html2canvas from 'html2canvas';
import { MessageService } from 'primeng/api';
import { ReactiveFormsModule } from '@angular/forms';
import { getHeroFrom } from '../../shared/forms/hero.form';
import { NgOptimizedImage } from '@angular/common';
import {UploadService} from "../../shared/services/upload.service";
import {User} from "../../shared/models/user.model";
import {AuthService} from "../../shared/services/auth.service";
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-character-creation',
  standalone: true,
  imports: [
    RouterLink, StepperComponent, ReactiveFormsModule, NgOptimizedImage
  ],
  templateUrl: './character-creation.component.html',
  styleUrl: './character-creation.component.scss',
})
export class CharacterCreationComponent implements OnInit{
  @ViewChild('avatar') avatar!: any;
  selectedSkin = 1;
  selectedNose = 1;
  selectedEar = 1;
  selectedAccessory = 1;
  selectedHair = 1;
  selectedHairColor = 1;
  selectedEye = 1;
  selectedMouth = 1;
  heroForm = getHeroFrom();
  userName!: string;

  constructor(private router: Router,
              private heroService: HeroService,
              private messageService: MessageService,
              private uploadService: UploadService,
              private authService: AuthService,
              private soundService: SoundService,
              ) { }

  ngOnInit() {
    this.authService.user$.subscribe((user) => {
      if (user) {
        this.userName = user.username
      }
    })
  }

  async create(): Promise<void> {
    if (this.heroForm.invalid) {
      return;
    }

    try {
      await this.generateAvatar();
    } catch (e) {
      console.error(e);
    }


    this.heroService.create({
      id: BigInt(0),
      name: this.heroForm.get('heroName')?.value as string,
      maxHp: 100,
      intelligence: 3,
      luck: 3,
      endurance: 3,
      dexterity: 3,
      diligence: 3,
      adventure: 100,
      xp: BigInt(0),
      maxXp: BigInt(100),
      level: 0,
      forint: 5000,
      crypto: 10,
      boosterItem: '',
      equipmentItems: [],
      backpackItems: [],
      dungeonLevel: 1,
      image: '',
      picture: '',
      nextDungeonTry: new Date(),
    }).subscribe(hero => {
      if (hero) {
        this.messageService.add({
          severity: 'success',
          summary: 'Karakter sikeresen létrehozva!'
        });
        this.router.navigate(['/map']);
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Hiba történt a karakter létrehozásakor!'
        });
      }
    });
  }

  generateAvatar(): Promise<Blob> {
    return new Promise((resolve, reject) => {
      html2canvas(this.avatar.nativeElement).then((canvas: any) => {
        let dataUrl = canvas.toDataURL('image/png', 0.9);
        // let base64String = dataUrl.split(',')[1];
        // let byteArray = Uint8Array.from(atob(base64String), char => char.charCodeAt(0));
        // resolve(byteArray);
        fetch(dataUrl)
          .then(res => res.blob())
          .then(blob => {
            const file = new File([blob], `${this.userName}.png`, { type: 'image/png' });
            // const link = document.createElement('a');
            // link.href = URL.createObjectURL(file);
            // link.download = file.name;
            //link.click();
            this.uploadService.upload(file).subscribe((file) => {
              if (file) {
                resolve(blob);
              } else {
                reject(new Error());
              }
            })
          })
          .catch(error => {
            reject(error);
          });
      });
    });
  }
  
  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
