import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Enemy } from '../models/enemy.model';

@Injectable({ providedIn: 'root' })
export class FightService {
  enemy!: Enemy

  constructor(private http: HttpClient) { }

  runFight(type: string, enemyName: string): Observable<string | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'scheduleHeroVs' + type, {}, { headers, observe: 'response', responseType: 'text', params: { enemyName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a harc során');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }
}
