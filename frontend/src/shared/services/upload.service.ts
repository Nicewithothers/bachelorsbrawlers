import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, map, of} from "rxjs";
import {Hero} from "../models/hero.model";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private http: HttpClient) {
  }

  upload(file: File) {
    const token = sessionStorage.getItem('token') as string;
    const formData = new FormData();
    formData.append('file', file);
    const headers = new HttpHeaders({
      'Authorization': token,
    })
    return this.http.post(environment.API_URL + 'files/upload', formData, {
      headers: headers,
      responseType: 'text',
      observe: 'response'
    }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a hallgató lekérdezésekor');
        }
      }),
      catchError((error) => {
        console.error(`Error with upload: ${error}`);
        return of(null);
      })
    );
  }
}
