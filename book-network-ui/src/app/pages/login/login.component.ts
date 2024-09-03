import {Component} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";
import {AuthenticationResponse} from "../../services/models/authentication-response";
import {HttpErrorResponse} from "@angular/common/http";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
  ) {
  }

  login() {
    this.errorMsg = [];

    this.authService
      .authenticate({body: this.authRequest})
      .subscribe({
        next: (response: AuthenticationResponse) => {
          this.tokenService.token = response.token as string;
          this.router.navigate(['books']);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err)
          if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else if (err.error.businessErrorDescription) {
            this.errorMsg.push(err.error.businessErrorDescription);
          } else {
            this.errorMsg.push(err.error.errors.message);
          }
        }
      })

  }

  register() {
    this.router.navigate(['register'])
  }
}
