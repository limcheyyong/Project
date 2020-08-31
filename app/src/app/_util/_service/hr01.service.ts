import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
const API_URL = environment.serviceUrl;
@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(private http: HttpClient) { }
  adminList(param: any) {
    console.log(param);
    return this.http.post<any>(`${API_URL}/api_backend/adminList`, param).pipe(map(data => {
      return data;
    }));
  }
}
