import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { ConversationsPageComponent } from './components/conversations-page/conversations-page.component';
import { SettingsPageComponent } from './components/settings-page/settings-page.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ChatlistComponent } from './components/chatlist/chatlist.component';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ChatComponent } from './components/chat/chat.component';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {  ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ApiModule, Configuration } from './api';


@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    ConversationsPageComponent,
    SignUpComponent,
    SettingsPageComponent,
    ChatlistComponent,
    ChatComponent,
  ],
  //put import from materials here
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    MatGridListModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    HttpClientModule,
    // To avoid CORS issues and limitations, we use an Angular reverse proxy to access the server API
    // (see proxy-config.json: it specifies that all HTTP calls to /serverapi/* URLs should be redirected to the server API at http://localhost:8080/*)
    // Here we configure the generated API client for it to use this base path.
    ApiModule.forRoot(() => new Configuration({ basePath: '/serverapi' }))

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
