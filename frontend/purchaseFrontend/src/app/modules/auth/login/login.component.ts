import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;

  constructor(
    public router: Router,
    private fb: FormBuilder,
    private auth:AuthService,

  ) {}


  ngOnInit(): void {

    this.loginForm = this.fb.group({
      'username': new FormControl(null, []),
      'password': new FormControl(null, []),
    });

  }

  login(data) {
    this.auth.login( data.password,data.username)
    this.router.navigate(['/purchases']);
    return true;


  }

}
