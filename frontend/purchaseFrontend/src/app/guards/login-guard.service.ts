import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  CanLoad, Route,
  Router,
  RouterStateSnapshot, UrlSegment
} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Injectable()
export class LoginGuardService implements CanActivate,CanActivateChild,CanLoad{
  constructor(private authService: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean{
    let url=state.url;
    return this.checkLogin(url);
  }
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean{
    return this.canActivate(childRoute,state);
  }

  canLoad(route: Route, segments: UrlSegment[]):boolean{
    let url = `/${route.path}`;
    return this.checkLogin(url)
  }
  checkLogin(url:string): boolean {
    if(this.authService.isAuthenticated('Director')){
      this.router.navigate(['/admin'],/*{ queryParams: { returnUrl: url }}*/);
      return false;
    } else
    if(this.authService.isAuthenticated('User')){
      this.router.navigate(['/user'],/*{ queryParams: { returnUrl: url }}*/);
      return false;
    }
    return true;
  }

}
