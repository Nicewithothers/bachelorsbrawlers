import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Item } from '../models/item.model';
import { environment } from '../../environments/environment';
import { Hero } from '../models/hero.model';

@Injectable({ providedIn: 'root' })
export class ShopService {
  
  constructor(private http: HttpClient) {}

  listShopItems(): Observable<Item[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'shop/items', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Bót lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  refreshItems(): Observable<Item[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'shop/refresh', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Bót lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  buyItem(itemName: string): Observable<Hero | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'shop/buyItem', { }, { headers, observe: 'response', params: { itemName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a tárgy megvásárlásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  sellItem(itemName: string): Observable<Hero | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL +'shop/sellItem', { }, { headers, observe: 'response', params: { itemName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a tárgy eladásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }
}
