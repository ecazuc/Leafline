import { Component } from '@angular/core';
import { Router } from '@angular/router';


import { ProfilService } from 'src/app/services/profil.service';

import {AuthentificationService2} from "../../services/athentication-service2.service";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {

  errorMessage!: string;




  constructor(private router: Router, private authentificationService: AuthentificationService2, private profilService: ProfilService) {
    this.errorMessage=this.authentificationService.getErrorMessage();
  }


   onSignUp(username: string, password: string, passwordVerif:string) {
    if(password === passwordVerif){
      this.authentificationService.signUp(username, password)
          .subscribe(() => {
              // Connexion succeeded
              this.router.navigate(["login"]);
              this.errorMessage = ''; //Reinitialized error message if succeeded
          }, (error) => {
              // Connexion Error
              this.errorMessage = 'Error : ' + error.error.message; //Display error message from service
          });
       } else {
        this.errorMessage = 'Passwords are different';
       }

    }



}

