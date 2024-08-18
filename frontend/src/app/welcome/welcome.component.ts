import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { SoundService } from '../../shared/services/sound.service';


@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [RouterLink, NgOptimizedImage],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss', '../app.component.scss'],
})
export class WelcomeComponent {
  constructor(private soundService: SoundService) {}

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
