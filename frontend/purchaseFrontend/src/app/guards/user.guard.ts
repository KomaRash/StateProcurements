import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  CanActivateChild,
  CanLoad, Router, Route, UrlSegment
} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from "../services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class UserGuard implements CanActivate,CanActivateChild,CanLoad{
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
    if(this.authService.isAuthenticated('User')){
      return true;
    }

    // // not gged in so redirect to login page with the return url
    this.router.navigate(['/login'], { queryParams: { returnUrl: url }});
    return false;
  }

}
