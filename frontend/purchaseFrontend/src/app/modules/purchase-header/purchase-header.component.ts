import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-purchase-header',
  templateUrl: './purchase-header.component.html',
  styleUrls: ['./purchase-header.component.css']
})
export class PurchaseHeaderComponent implements OnInit {

  constructor(private authService:AuthService,
              private router: Router) { }

  ngOnInit(): void {

  }
  logout() {
  this.authService.logout();
  this.router.navigate(['/login'])
  }
}
