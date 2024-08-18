import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Hero } from '../models/hero.model';

@Injectable({ providedIn: 'root' })
export class ArenaService {
  constructor(private http: HttpClient) { }

  getOpponents(): Observable<Hero[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'arena/opponents', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body.map((hero: Hero) => {
            const imageURL = "data:image/png;base64," + hero.picture;
            return {...hero, picture: imageURL}
          });
        } else {
          throw new Error('Hiba történt az ellenfél lekérdezése során');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }
}
