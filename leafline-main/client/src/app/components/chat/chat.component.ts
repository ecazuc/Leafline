import { Component, ElementRef, ViewChild, AfterViewInit, NgZone, Input, SimpleChanges, ChangeDetectionStrategy, OnInit } from '@angular/core';

import {Message} from '../../models/message';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';


import { Discussion, DiscussionService } from 'src/app/services/discussion.service';
import { FormControl } from '@angular/forms';
import { Profil } from 'src/app/models/profil';
import { ProfilService } from 'src/app/services/profil.service';

import {AuthentificationService2, CurrentUser} from "../../services/athentication-service2.service";
import { MessageDTO } from 'src/app/api';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';




@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class ChatComponent implements AfterViewInit {



  @Input() selectedConversation!: Discussion;


  @ViewChild('messageContainer') messageContainer!: ElementRef;



  @ViewChild('messageInput') messageInput!: ElementRef;


  options: string[] = [];
  filteredOptions!: Observable<string[]>;
  currentUser: CurrentUser | null = null;
  // previousDate: Date |null = null;

  @Input() noConversation : any;
  messages: MessageDTO[] = [];
  profils:Profil[]=[];
  newCorrespondant: string ="";


    constructor(private zone: NgZone, private conversationService: DiscussionService, private userService: ProfilService, private authentificationService: AuthentificationService2 ) {
      console.debug('### Chat component');
      this.profils= this.userService.getProfils();
      console.log("PROFIL",this.profils);

   
  }


  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    console.log("option",this.options);
    console.log("user",this.profils);

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }




  ngOnChanges(changes: SimpleChanges) {

    console.log("seLeCTED CONV", this.selectedConversation);
    if ('selectedConversation' in changes) {
      this.scrollToBottom();
     this.messages = this.getMessages();
     if(this.selectedConversation === undefined){
        console.log("null", this.selectedConversation);
        this.noConversation = true;
        console.log( this.noConversation);
      } else {
        console.log(" NON null", this.selectedConversation);
        this.noConversation = false;
        console.log( this.noConversation);
      }
    }
  }


  /**
   * true if the message is from the signed-in user.
   */
  isFromCurrentUser(message : MessageDTO) {
    return message.from == this.authentificationService.getCurrentUserAddress();
  }


  getMessages(){
    this.scrollToBottom();
    return this.selectedConversation?.messages || [];
  }

  newMessage: string= "";

  // the scroll container is at the bottom by default
  ngAfterViewInit() {
   this.scrollToBottom();
  }

  scrollToBottom() {
     // Use a timeout to ensure the view has updated
     setTimeout(() => {
      const container = this.messageContainer.nativeElement;
      container.scrollTop = container.scrollHeight;
    });
  }


  //to adapt the message block to the size of the message
  calculateMessageSize(message : MessageDTO){
    const textLength = message.body!.length ;
    if(textLength<16){
      return `${textLength * 0.5}vw`;
    } else {
      return `${Math.min(38, textLength * 0.5)}vw`; // Adjust the multiplier and maximum width as needed

    }
  }

  calculateMinWidth(message : MessageDTO){
    const messageSize = this.calculateMessageSize(message);
    return `${Math.max(3.5,parseFloat(messageSize) *1.1)}vw`;
  }


  calculatePadding(message : MessageDTO){
    const messageSize = this.calculateMessageSize(message);
    if(parseFloat(messageSize) <16){
      if(parseFloat(messageSize) <5){
        return `${parseFloat(messageSize) +15}%`;
      } else {
      return `${Math.min(7, parseFloat(messageSize) + 5)}%`;
      }
    } else {
      return `${Math.min(3, parseFloat(messageSize) *0.2)}%`;
    }

  }



  isSameDay(currentTimestamp: number, conv : Discussion): boolean {
    const currentDate = new Date(currentTimestamp);
  
    if (conv.firstDate === null) {
      conv.firstDate = new Date(currentDate);
      return false;
      
    } else {

      if(conv.messages[0].timestamp === currentTimestamp){
        return false;
      } else {
        const isSameDay =
          conv.firstDate.getDate() === currentDate.getDate() &&
          conv.firstDate.getMonth() === currentDate.getMonth() &&
          conv.firstDate.getFullYear() === currentDate.getFullYear();

        conv.firstDate = currentDate;
        return isSameDay;
        }
    }
  }



  


  send(){

    //if message not empty
    if(this.newMessage != "" ){
      this.conversationService.sendMessage(<Discussion>this.selectedConversation, this.newMessage);
      this.scrollToBottom();
    }
    this.newMessage="";
    this.scrollToBottom();
  }

    

    

  }



