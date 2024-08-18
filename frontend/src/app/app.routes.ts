import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { MagicShopComponent } from './magic-shop/magic-shop.component';
import { WeaponShopComponent } from './weapon-shop/weapon-shop.component';
import { MapComponent } from './map/map.component';
import { FightComponent } from './fight/fight.component';
import { CharacterOverviewComponent } from './character-overview/character-overview.component';
import { ArenaComponent } from './arena/arena.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { CharacterCreationComponent } from './character-creation/character-creation.component';
import { TavernComponent } from './tavern/tavern.component';
import { heroGuard } from '../shared/guards/hero.guard';
import { GamblingComponent } from './gambling/gambling.component';
import {TobaccoShopComponent} from './tobacco-shop/tobacco-shop.component';
import { KazamataComponent } from './kazamata/kazamata.component';
import {MailComponent} from "./mail/mail.component";
import { YearbookComponent } from "./yearbook/yearbook.component";

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'magicshop', component: MagicShopComponent, canActivate: [heroGuard] },
  { path: 'weaponshop', component: WeaponShopComponent, canActivate: [heroGuard] },
  { path: 'tobaccoshop', component: TobaccoShopComponent, canActivate: [heroGuard] },
  { path: 'tavern', component: TavernComponent, canActivate: [heroGuard] },
  { path: 'arena', component: ArenaComponent, canActivate: [heroGuard] },
  { path: 'kazamata', component: KazamataComponent, canActivate: [heroGuard] },
  { path: 'gambling', component: GamblingComponent, canActivate: [heroGuard] },
  { path: 'map', component: MapComponent, canActivate: [heroGuard] },
  {
    path: 'character',
    component: CharacterOverviewComponent,
    canActivate: [heroGuard],
  },
  {
    path: 'newcharacter',
    component: CharacterCreationComponent,
    canActivate: [heroGuard],
  },
  {
    path: 'leaderboard',
    component: LeaderboardComponent,
    canActivate: [heroGuard],
  },
  { path: 'fight', component: FightComponent, canActivate: [heroGuard] },
  { path: 'mail', component: MailComponent, canActivate: [heroGuard] },
  { path: 'yearbook', component: YearbookComponent, canActivate: [heroGuard] },
  { path: '**', redirectTo: '/welcome' },
];
