import {Component} from '@angular/core';
import {FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../shared/services/auth.service';
import {SeparatorComponent} from '../../shared/components/separator/separator.component';
import {getRegisterForm} from "../../shared/forms/register.form";
import { MessageService } from 'primeng/api';
import { NgOptimizedImage } from '@angular/common';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterModule, SeparatorComponent, NgOptimizedImage],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss', '../app.component.scss'],
})
export class RegisterComponent {
  registerForm: FormGroup = getRegisterForm();

  constructor(
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService,
    private soundService: SoundService
  ) {
  }

  redirectToYoutube() {
    window.open('https://www.youtube.com/watch?v=dQw4w9WgXcQ&autoplay=1', '_blank');
  }


  register(): void {
    if (this.registerForm.get('password')?.value === this.registerForm.get('rePassword')?.value && this.registerForm.valid) {
      this.authService
      .register(
        this.registerForm.get('userName')?.value,
        this.registerForm.get('email')?.value,
        this.registerForm.get('password')?.value,
      )
      .subscribe((user: string | null) => {
        if (user) {
          this.messageService.add({
            severity: 'success',
            summary: 'Sikeres regisztráció. Mostmár bejelentkezhetsz!'
          });
          this.router.navigate(['/login']);
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Hiba történt a regisztrációkor!'
          });
        }
      });
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
