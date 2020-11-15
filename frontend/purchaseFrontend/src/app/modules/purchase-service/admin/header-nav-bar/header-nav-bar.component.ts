import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-header-nav-bar',
  templateUrl: './header-nav-bar.component.html',
  styleUrls: ['./header-nav-bar.component.css']
})
export class HeaderNavBarComponent implements OnInit {

  constructor(private authService:AuthService) { }

  ngOnInit(): void {
  }

  logout() {
    this.authService.logout()
  }
}
