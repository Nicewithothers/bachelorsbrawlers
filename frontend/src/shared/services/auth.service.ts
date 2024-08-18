import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { BehaviorSubject, catchError, firstValueFrom, map, Observable, of, take, timer } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  public user$: Observable<User> = this.userSubject.asObservable();

  constructor(private http: HttpClient, private router: Router, private messageService: MessageService) { }

  login(username: string, password: string): Observable<User | null> {
    return this.http.post(environment.API_URL + 'login', { username, password }, { observe: 'response' }).pipe(
      map((response: any) => {
        const token = response.headers.get('Authorization');
        sessionStorage.setItem('token', token);
        if (response.ok && response.body) {
          const userObj = { ...response.body, token } as User;
          sessionStorage.setItem('profileName', userObj.username);
          this.userSubject.next(userObj);
          return userObj;
        } else {
          throw new Error('Sikertelen bejelentkezés');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  signOut(): Observable<boolean> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'signout', {}, { headers, observe: 'response', responseType: 'text' }).pipe(
      map((response: any) => {
        if (response.ok) {
          this.userSubject.next(null);
          sessionStorage.clear();
          return true;
        } else {
          throw new Error('Sikertelen kijelentkezés');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(false);
      }),
    );
  }

  getUserByToken(): Observable<User | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + '/getUserByToken', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const userObj = { ...response.body, token } as User;
          this.userSubject.next(userObj);
          return userObj;
        } else {
          throw new Error('Sikertelen felhasználó lekérdezés');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  register(userName: string, email: string, password: string): Observable<string | null> {
    return this.http.post(environment.API_URL + 'registration', { userName, email, password }, { observe: 'response', responseType: 'text' }).pipe(
      map((response: any) => {
        // console.log(response);
        
        if (response.ok && response.body) {
          sessionStorage.setItem('profileName', response.body);
          return response.body;
        } else {
          throw new Error('Sikertelen regisztráció');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  startLogOutTimer(): void {
    timer(20*60*1000).pipe(take(1)).subscribe(() => {
      this.signOut().subscribe(() => {
        this.messageService.add({
          severity: 'warn',
          summary: 'Munkamenet lejárt. Kérlek jelentkezz be újra!'
        });
        this.router.navigate(['/login']);
      });
    })
  }
}
