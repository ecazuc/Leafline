import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { Profil } from 'src/app/models/profil';

import { ProfilService } from 'src/app/services/profil.service';
import {AuthentificationService2, CurrentUser} from "../../services/athentication-service2.service";

@Component({
  selector: 'app-settings-page',
  templateUrl: './settings-page.component.html',
  styleUrls: ['./settings-page.component.css']
})
export class SettingsPageComponent {

  profils : Profil[]=[];

  profilIn!: Profil;

  currentUser!: string;

  private stopPolling = new Subject<void>();

    constructor( private router: Router, private profilService: ProfilService, private authentificationService: AuthentificationService2){
    this.profils = this.profilService.getProfils();
     
  }

   ngOnInit() {
    this.authentificationService.currentUser.subscribe((user) => {
      this.currentUser = this.authentificationService.getCurrentUserAddress();
    });
  }

 logOut(){
  this.authentificationService.logOut().subscribe(
    () => {
      this.stopPolling.next();
      this.stopPolling.complete();
    }, (error) => {
      //Connexion error
      console.log(error.error);
    });

  console.debug('### logged out', this.currentUser);
  this.router.navigate(["login"]);
 }


}
