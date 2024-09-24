import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthentificationService2, CurrentUser} from "../../services/athentication-service2.service";
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

  errorMessage! : string ;





  users:User[]=[]

  constructor( private route: ActivatedRoute,private router: Router, private authentificationService: AuthentificationService2){
    this.errorMessage  = this.authentificationService.getErrorMessage();
  }

   

  


  onSignIn(username: string, password: string) {
    this.authentificationService.signIn(username, password)
        .subscribe(() => {
          // Connexion succeeded
          this.authentificationService.currentUser.subscribe((currentUser) => {
            if (currentUser) {
              // Navigate to the conversation page
              this.router.navigate(['conversation']);
              console.debug('### current work', );
              this.errorMessage = ''; // Reinitialized error message if succeeded
            } else {
              console.debug('### current NOT work');
            }
          }
          );
        }, (error) => {
          //Connexion error
          console.log(error.error);
          this.errorMessage = 'Error : ' + error.error.message ; // Display error message from service
        });

  
  }

}
