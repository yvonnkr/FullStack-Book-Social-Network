import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {NgIf} from "@angular/common";
import {CodeInputModule} from "angular-code-input";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [
    NgIf,
    CodeInputModule
  ],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message = '';
  isOkay = true;
  submitted = false;

  constructor(private router: Router, private authService: AuthenticationService) {
  }


  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['login'])
      .catch(err => {
        console.log(err)
      })
  }

  private confirmAccount(token: string) {
    this.message = "";
    this.authService
      .confirm({token})
      .subscribe({
        next: (response: any) => {
          this.message = "Your account has been successfully activated! \n You can now proceed to login.";
          this.submitted = true;
          this.isOkay = true;

        },
        error: (err: HttpErrorResponse) => {
          if (err.error.errors) {
            this.message = err.error.errors.message;
            this.submitted = true;
            this.isOkay = false;
          }
        }
      })
  }
}
