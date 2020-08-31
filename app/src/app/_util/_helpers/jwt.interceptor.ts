import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../_service/user.service';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: UserService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with basic auth credentials if available
    const currentUser = this.authenticationService.userValue;
    if (currentUser && currentUser.authdata) {

      request = request.clone({
        setHeaders: {
          Authorization: `Basic ${currentUser.authdata}`
        }
      });
    }

    return next.handle(request);
  }
}
