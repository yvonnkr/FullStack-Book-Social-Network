import {Component} from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registerRequest: RegistrationRequest = {email: '', firstname: '', lastname: '', password: ''};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
  ) {
  }

  register() {
    this.errorMsg = [];
    this.authService
      .register({body: this.registerRequest})
      .subscribe({
        next: () => {
          this.router.navigate(['activate-account'])
            .catch(err => {
              console.log(err)
            })
        },
        error: (err: HttpErrorResponse) => {
          console.log(err)
          if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else {
            this.errorMsg.push(err.error.errors.message);
          }
        }
      })
  }

  login() {
    this.router.navigate(['login'])
      .catch(err => {
        console.log(err)
      })
  }

}
