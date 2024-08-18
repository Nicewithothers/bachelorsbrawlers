import { Component, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { SidemenuComponent } from '../shared/components/sidemenu/sidemenu.component';
import { ToastModule } from 'primeng/toast'
import { HeroService } from '../shared/services/hero.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidemenuComponent, ToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'frontend';

  constructor(protected router: Router, private heroService: HeroService) {}

}
