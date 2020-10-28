import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { map } from 'rxjs/operators';
import {appConfig} from "../app.config";
import {Observable} from "rxjs";
import {Position} from "../models/Position";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private headers = new HttpHeaders({'Content-Type': 'application/json',
    'Access-Control-Allow-Origin':appConfig.url,
    'Access-Control-Allow-Headers': 'Authorization'});
  constructor(private http: HttpClient) { }
  login(password:String,email:String) {
    let response=this.http.post(appConfig.url+"login", JSON.stringify({
      email:email,
      password:password
    }),{headers:this.headers,observe:'response'})
    response.subscribe((resp:HttpResponse<Position>)=>{
      let token=resp.headers.get("Authorization");
      let position:Position=resp.body
      localStorage.setItem("token",token)
      localStorage.setItem("position",position.positionRole)
      },
        err=>console.log(err))
  }
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('position');
    }
  isAuthenticated(){
    return ( localStorage.getItem('token') )? true : false;

  }
  getToken():String{
    console.log(localStorage.getItem('token'))
    return localStorage.getItem('token')}
}
