import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Monster } from '../models/monster.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MonsterService {
  constructor(private http: HttpClient) { }

  getAll(): Observable<Monster[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'monsters', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body.map((monster: Monster) => {
            const imageURL = "data:image/webp;base64," + monster.picture;
            return {...monster, picture: imageURL};
          });
        } else {
          throw new Error('Hiba történt a szörnyek lekérdezésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      })
    )
  }

  getMonster(): Observable<Monster | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'monsters/random', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a szörny lekérdezésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      })
    )
  }
}
