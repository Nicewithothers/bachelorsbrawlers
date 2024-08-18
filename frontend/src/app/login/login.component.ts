import { Component } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { getLogInForm } from '../../shared/forms/login.form';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { SeparatorComponent } from '../../shared/components/separator/separator.component';
import { User } from '../../shared/models/user.model';
import { MessageService } from 'primeng/api';
import { NgOptimizedImage } from '@angular/common';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterModule, SeparatorComponent, NgOptimizedImage],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss', '../app.component.scss'],
})
export class LoginComponent {
  loginForm: FormGroup = getLogInForm();

  constructor(
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService,
    private soundService: SoundService
  ) {}

  redirectToYoutube() {
    window.open('https://www.youtube.com/watch?v=dQw4w9WgXcQ&autoplay=1', '_blank');
  }

  login(): void {
    if (this.loginForm.valid) {
      this.authService
      .login(
        this.loginForm.get('username')?.value,
        this.loginForm.get('password')?.value,
      )
      .subscribe((user: User | null) => {
        if (user) {
          this.messageService.add({
            severity: 'success',
            summary: 'Sikeres bejelentkezés!'
          });
          this.authService.startLogOutTimer();
          this.router.navigate(['/newcharacter']);
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Hiba történt a bejelentkezéskor!'
          });
        }
      });
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
