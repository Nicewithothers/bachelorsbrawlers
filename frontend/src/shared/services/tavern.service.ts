import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {Quest} from '../models/quest.model';
import {environment} from '../../environments/environment';

@Injectable({providedIn: 'root'})
export class TavernService {
  constructor(
    private http: HttpClient,
  ) {
  }

  getQuests(): Observable<Quest[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http
      .get(environment.API_URL + 'tavern/getQuests', {headers, observe: 'response'})
      .pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return response.body.map((quest: Quest) => {
              const imageURL = 'data:image/png;base64,' + quest.image;
              return {...quest, image: imageURL}
            });
          } else {
            throw new Error('Hiba történt a küldetések lekérdezésekor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  chooseQuest(gruntName: string, heroName: string): Observable<Quest | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http
      .post(environment.API_URL + 'tavern/chooseQuest', { }, {headers, observe: 'response', responseType: 'text', params: {gruntName, heroName}})
      .pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return response.body;
          } else {
            throw new Error('Hiba történt a küldetés választásakor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }
}
