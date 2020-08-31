import { AdminInfo } from './../_models/adminInfo';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { User } from '../_models/user';
import { Router } from '@angular/router';

const API_URL = environment.serviceUrl;
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
    this.user = this.userSubject.asObservable();
  }
  public get userValue(): User {
    return this.userSubject.value;
  }
  login(param) {
    console.log(param)
    return this.http.post<any>(`${API_URL}/api_backend/login_backend`, param)
      .pipe(map(user => {
        console.log("test")
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        if (user.result) {
          user.data.authdata = window.btoa(user.data.userCode + ':' + user.data.adminId);
          localStorage.setItem('user', JSON.stringify(user.data));
          console.log(user.data.authdata);
          this.userSubject.next(user);

        }
        return user;

      }));
  }
  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('user');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  // login(loginData): Observable<boolean> {
  //   return this.loginServer(loginData).pipe(map(res => {
  //     if (res.result) {
  //       this.loginStatus.next(true);
  //       this.currentUser.next(loginData.username);
  //       return true;
  //     } else {
  //       console.log('can not login');
  //       return false;
  //     }
  //   },
  //     (err: HttpErrorResponse) => {
  //       if (err.error instanceof Error) {
  //         console.log('client-side error');
  //       } else {
  //         console.log('server-side error');
  //       }
  //       return of(false);
  //     }))
  // }
  // logout() {
  //   this.loginStatus.next(false);
  //   this.currentUser.next(null);
  // }
  // getLoginStatus(): Observable<boolean> {
  //   return this.loginStatus;
  // }
  // getCurrentUser(): Observable<User> {
  //   return this.currentUser;
  // }
}
