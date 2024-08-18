import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, map, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {AuthService} from './auth.service';
import {MessageService} from 'primeng/api';
import { YearBook } from '../models/yearbook.model';

@Injectable({ providedIn: 'root' })
export class YearbookService {
  constructor(private http: HttpClient, private auth: AuthService, private messageService: MessageService) { }

  getAll(): Observable<YearBook[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'yearbook', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt az évkönyv lekérdezésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      })
    )
  }
}
