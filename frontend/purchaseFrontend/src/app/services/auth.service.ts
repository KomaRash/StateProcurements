import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {appConfig} from "../app.config";
import {Position} from "../models/Position";
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': appConfig.url,
    'Access-Control-Allow-Headers': 'Authorization'
  });

  constructor(private http: HttpClient) {
  }

  login(password: String, email: String): Observable<Position> {
    let response = this.http.post(appConfig.url + "login", JSON.stringify({
      email: email,
      password: password
    }), {headers: this.headers, observe: 'response'})
    return response.pipe(map((resp: HttpResponse<Position>) => {
        let token = resp.headers.get("Authorization");
        let user: Position = resp.body
        localStorage.setItem("token", token)
        localStorage.setItem("position", user.positionRole)
        return user
      }
    ))


  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('position');
  }

  isAuthenticated(role:string) {
    return (localStorage.getItem('token')) && (localStorage.getItem('position')===role) ? true : false;

  }

  getToken(): String {
    console.log(localStorage.getItem('token'))
    return localStorage.getItem('token')
  }

  isUser() {
    return 'User' === (localStorage.getItem('position'))
  }
  isAdmin() {
    return 'Director' === (localStorage.getItem('position'))
  }
}
