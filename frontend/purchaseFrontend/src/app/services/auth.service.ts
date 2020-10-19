import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { map } from 'rxjs/operators';
import {appConfig} from "../app.config";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  constructor(private http: HttpClient) { }
  login(password:String,username:String) {

    return this.http.post(appConfig.url + "/login", JSON.stringify({
      username: username,
      password: password
    }),{headers:this.headers}).pipe(map((response: Response) => {
      let userRole = response.json();
      let userToken = response.headers.get("Token")
      localStorage.setItem('role', JSON.stringify(userRole));
      localStorage.setItem('Token', JSON.stringify(userToken));

    }));
  }
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    }
  isAuthenticated(){
    return ( localStorage.getItem('token') )? true : false;

  }
}
