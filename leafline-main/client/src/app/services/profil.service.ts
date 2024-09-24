import { Injectable } from '@angular/core';
import { Profil } from '../models/profil';

@Injectable({
  providedIn: 'root'
})
export class ProfilService {

  constructor() { }

  private profils: Profil[] = [
    {name:"Alice", picture:"feuille1.svg",isSelected: false},
    {name:"Bob", picture:"feuille2.svg",isSelected: false},
    {name:"Charlie", picture:"feuille3.svg",isSelected: false},
    {name:"Papa", picture:"feuille3.svg",isSelected: false},
    {name:"Maman", picture:"feuille3.svg",isSelected: false},
    {name:"Denis", picture:"feuille4.svg",isSelected: false},
    {name:"Francoise", picture:"feuille5.svg",isSelected: false},
    {name:"Guillaume", picture:"feuille6.svg",isSelected: false},
  ];

  getProfils(){
    return this.profils;
  }


  signUp(username: string): boolean{
    
      if(this.profils.find(profil => profil.name === username) === undefined){
        let newProfil: Profil = {name: username, picture:"feuille1.svg", isSelected:false };
        this.profils.push(newProfil)
        return true;
      } else {
      return false;
    }
  }


}
