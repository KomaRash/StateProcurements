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

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate,CanActivateChild,CanLoad{
  constructor(private authService: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean{
    let url=state.url;
    //console.log("жаник пидр")
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
    if(this.authService.isAuthenticated()){
      return true;
    }
    this.router.navigate(['/purchases'],{ queryParams: { returnUrl: url }});

    return false;
  }

}
