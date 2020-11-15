import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header-nav-bar',
  templateUrl: './header-nav-bar.component.html',
  styleUrls: ['./header-nav-bar.component.css']
})
export class HeaderNavBarComponent implements OnInit {

  constructor(private authService:AuthService,
              private router: Router) { }

  ngOnInit(): void {

  }
  logout() {
    this.authService.logout();
    this.router.navigate(['/login'])
  }

}
