import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, catchError, map, Observable, of} from "rxjs";
import {environment} from "../../environments/environment";
import {Mail} from "../models/mail.model";

@Injectable({
  providedIn: 'root'
})
export class MailService {
  private unreadCountSubject = new BehaviorSubject<number | null>(null);
  unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {
  }

  listMails(): Observable<Mail[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'mailing/mails', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body ) {
          const mails = response.body.sort((a: Mail, b: Mail) => new Date(b.created).getTime() - new Date(a.created).getTime());
          this.updateUnreadCount(mails);  // Ensure unread count is updated when mails are listed
          return mails;
        } else {
          throw new Error('Levelek lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  getUnread(): Observable<number | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'mailing/unread', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body !== null && response.body !== undefined) {
          return response.body;
        } else {
          throw new Error('Olvasatlan levelek lekérdezése sikertelen');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  updateUnreadCount(mails: Mail[]): void {
    const unreadCount = mails.filter(mail => !mail.seen).length;
    this.unreadCountSubject.next(unreadCount);
  }
}
