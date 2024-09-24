import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of, Subject} from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ProfilDTO } from '../api';


export type CurrentUser = ProfilDTO | null | undefined;

@Injectable({
  providedIn: 'root'
})
export class AuthentificationService2 {
  private backendUrl = 'serverapi'; // backend URL
  errorMessage: string = "";

  // Current user of the app is unknown yet
  private _currentUser: CurrentUser = undefined;

  // Subject and Observable allowing subscribers to be informed about a change of the current user
  private currentUserSubject = new Subject<CurrentUser>();
  currentUser$ = this.currentUserSubject.asObservable();

  listUsers: any;

  testLogin : any;

  setErrorMessage(arg0: string) {
    let errorMessage = arg0;
    throw new Error(arg0);
  }

  /**
   * Get the current user of the app.
   * @return the profile of the user if signed-in, null if not signed-in, or undefined if unknown
   */
  get currentUser(): Observable<CurrentUser> {

    // If the current user is already known, return it directly
    if (this._currentUser !== undefined)
        return of(this._currentUser);

    // Otherwise, try to get his profile from the backend
    return this.profil().pipe(
        map(userProfile => {
            this.currentUser = userProfile;
            return userProfile;
        }),
        catchError(error => {

            // Current user is still unknown because of an unknown error (ie. server unreachable)
            this.currentUser = undefined;
            return of(undefined);
        })
    );
  }

  /**
   * Set the current user of the app and inform subscribers.
   * @param currentUser The profile of the user if signed-in, null if not signed-in, or undefined if unknown
   */
  private set currentUser(currentUser: CurrentUser) {
      this._currentUser = currentUser;
      this.currentUserSubject.next(currentUser);
  }




  constructor(private http: HttpClient,) {
      console.debug('### authentificationService()');
    }



  signUp(username: string, password: string): Observable<any> {


      const url = `${this.backendUrl}/user/signup`;
      const body = { login: username, password };
      return this.http.post(url, body).pipe(
        map(_ => {
          console.debug('### signed up as', username);
          this.currentUser = undefined; // current user profile is unknown (it will be refreshed from backend upon next access)
          return;
        }),
          catchError((error) => {
            //Manage errors here, for exemple, if username already exists
            console.error('Error during signup:', error);
            throw error;
          })
      );
  }


  signIn(username: string, password: string): Observable<any> {
    // return this.authenticationservice.userSigninPost({login: username, password: password}).pipe(
    //     map(_ => console.debug('### sign in ok', username)))
    console.debug(`### signing in as ${username}...`);
    const url = `${this.backendUrl}/user/signin`;
    const body = { login: username, password };
    return this.http.post(url, body, { observe: 'response' }).pipe(
        map(_=> {
          console.debug('### signed in as', username);
          this.currentUser = undefined; // current user profile is unknown (it will be refreshed from backend upon next access)
          return;
        }),
        catchError(error => {
          if (error.status === 409) {
            console.debug('### already signed in');
            return of(void 0);
          }
          console.debug('### pb signIn');
          throw error;
        })
      );
  }

  logOut(): Observable<any> {
    // return this.authenticationservice.userSignoutPost().pipe(
    //     map(_ => console.debug('### sign out ok')),
    const url = `${this.backendUrl}/user/signout`;
    return this.http.post(url, {}).pipe(
      map(() => {
        console.debug('### logged out correctly');
        this._currentUser = null //current user is not signed in
      }),
        catchError((error) => {
          // Manage errors here
          console.error('Error during signout:', error);
          throw error;
        })
    );
  }

  getErrorMessage(){
    return this.errorMessage;
  }


  profil(): Observable<CurrentUser> {
    console.debug('### getting profile...');
    //return this.authenticationservice.userProfileGet().pipe(
   return this.http.get<CurrentUser>(`${this.backendUrl}/user/profile`).pipe(
      map(userProfil => {
        console.debug('### user profil', userProfil);
        return userProfil;
      }),
      catchError((error) => {
        // Manage errors here

        if(error.status === 403){
          console.debug('### not signed in');
          return of(null); // current user is not signed in

        }
        console.error('Error during getCurrent:', error);
        throw error;
      })
    );
  }




     /**
   * @return the address of the signed-in user
   */
  getCurrentUserAddress() {
    return this._currentUser?.login + '@leafline';
  }

  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(`/user`);
  }


}
