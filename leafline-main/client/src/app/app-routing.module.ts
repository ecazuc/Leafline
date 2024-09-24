import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SettingsPageComponent } from './components/settings-page/settings-page.component';
import { ConversationsPageComponent } from './components/conversations-page/conversations-page.component';

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch:'full'}, //par defaut sur la page
  {path: 'login', component: HomePageComponent},
  {path: 'signUp', component: SignUpComponent},
  {path: 'settings', component: SettingsPageComponent},
  {path: 'conversation', component: ConversationsPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }