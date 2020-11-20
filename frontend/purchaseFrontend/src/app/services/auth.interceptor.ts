import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {AuthService} from "./auth.service";
import {appConfig} from "../app.config";
@Injectable()
export class AuthInterceptor implements HttpInterceptor{
  constructor(public auth: AuthService) {}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let request = req.clone({
      setHeaders: {
        Authorization: `${this.auth.getToken()}`,

        'Access-Control-Allow-Origin':appConfig.url,
        'Access-Control-Allow-Headers': 'Authorization'
      }
    });
    return next.handle(request);

  }
}
