import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SoundService {

  private audio: HTMLAudioElement;

  constructor() {
    this.audio = new Audio();
  }

  playSound(soundPath: string): void {
    this.audio.src = `../../assets/sounds/${soundPath}`;
    this.audio.load();
    this.audio.play();
  }

}