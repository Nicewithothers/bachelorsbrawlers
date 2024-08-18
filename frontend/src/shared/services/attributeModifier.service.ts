import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { AttributeModifier } from '../models/modifier.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AttributeModifierService {
  constructor(private http: HttpClient) { }

  getAllModifiers(): Observable<AttributeModifier[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'modifier', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Attribútummódosítók lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  getModifier(id: string): Observable<AttributeModifier | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'modifier/' + id, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Attribútummódosító lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  addModifier(modifier: AttributeModifier): Observable<AttributeModifier | null> {
    const body = {
      maxHpModifier: modifier.maxHpModifier,
      intelligenceModifier: modifier.intelligenceModifier,
      luckModifier: modifier.luckModifier,
      enduranceModifier: modifier.enduranceModifier,
      dexterityModifier: modifier.dexterityModifier
    };

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'modifier', body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt az attribútummódosító létrehozásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  update(modifier: AttributeModifier): Observable<AttributeModifier | null> {
    const body = {
      maxHpModifier: modifier.maxHpModifier,
      intelligenceModifier: modifier.intelligenceModifier,
      luckModifier: modifier.luckModifier,
      enduranceModifier: modifier.enduranceModifier,
      dexterityModifier: modifier.dexterityModifier
    };

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.put(environment.API_URL + 'modifier/' + modifier.id, body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt az attribútummódosító módosításakor');
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
    return this.http.delete(environment.API_URL + 'modifier/' + id, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok) {
          return true;
        } else {
          throw new Error('Hiba történt az attribútummódosító törlésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(false);
      }),
    );
  }
}
