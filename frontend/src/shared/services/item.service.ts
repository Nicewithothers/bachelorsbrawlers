import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Item } from '../models/item.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ItemService {
  constructor(private http: HttpClient) {}

  getAllItems(): Observable<Item[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'items', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Tárgyak lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  getItem(id: string): Observable<Item | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'items/' + id, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Tárgy lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  addItem(item: Item): Observable<Item | null> {
    const body = {
      itemcategory: item.itemCategory,
      name: item.name,
      rarity: item.rarity,
      damageRange: item.damageRange,
      armor: item.armor,
      attributeModifiers: JSON.stringify(item.attributeModifiers)
    };  

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'items', body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a tárgy létrehozásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  update(item: Item): Observable<Item | null> {
    const body = {
      itemcategory: item.itemCategory,
      name: item.name,
      rarity: item.rarity,
      damageRange: item.damageRange,
      armor: item.armor,
      attributeModifiers: JSON.stringify(item.attributeModifiers)
    }; 

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.put(environment.API_URL + 'items/' + item.id, body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a tárgy módosításakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  delete(id: string): Observable<boolean> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.delete(environment.API_URL + 'items/' + id, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok) {
          return true;
        } else {
          throw new Error('Hiba történt a tárgy törlésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(false);
      }),
    );
  }
}
