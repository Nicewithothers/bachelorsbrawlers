import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {BoosterItem} from "../models/boosteritem.model";
import {environment} from "../../environments/environment";
import {Hero} from "../models/hero.model";

@Injectable({providedIn: 'root'})
export class TobaccoShopService {
  constructor(private http: HttpClient) {}

  listBoosterItems(): Observable<BoosterItem[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'listBoosterItems', { headers, observe: 'response'}).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Lekérdezés sikertelen!');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      })
    )
  }

  updateBoosterItem(boosterItemName: string): Observable<Hero | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'updateBoosterItem', { }, { headers, observe: 'response', params: { boosterItemName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a booster tárgy megvásárlásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }
}
