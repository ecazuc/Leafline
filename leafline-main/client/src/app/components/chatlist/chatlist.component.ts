import {  Component, ElementRef, EventEmitter, Input, NgZone, Output, ViewChild } from '@angular/core';




import { Profil } from '../../models/profil';
import { Discussion, DiscussionService } from 'src/app/services/discussion.service';

import { ConversationsPageComponent } from '../conversations-page/conversations-page.component';
import { ProfilService } from 'src/app/services/profil.service';

import {AuthentificationService2, CurrentUser} from "../../services/athentication-service2.service";
import { Subject } from 'rxjs';
import { MessageDTO } from 'src/app/api';



@Component({
  selector: 'app-chatlist',
  templateUrl: './chatlist.component.html',
  styleUrls: ['./chatlist.component.css'],
})
export class ChatlistComponent {

  showInputField : boolean = false;
  newConversationUsername = '';
 


  @ViewChild('scrollableContainer') scrollableContainer!: ElementRef ;

  @Output() userSelected = new EventEmitter<Profil>();

  private stopPolling = new Subject<void>();

  @Input()
  set selected(selected: boolean) {
    if (selected){}
      
  }

  noConversation : boolean = true;


  @Input() conversations! : Discussion [];
  profils: Profil[] = [];



  filteredConversations: Discussion[] = [];

  currentUser: CurrentUser | null = null;

  selectedConversation: any;

  searchQuery : string = "";

  you:any;

  isInputFocused: boolean = false;

  searchUser: string="";

  // the scroll container is at the top by default
  ngAfterViewInit() {
    this.scrollToTop();
   }

  scrollToTop() {

    setTimeout(() => {
      const container = this.scrollableContainer.nativeElement;
      container.scrollTop = 0; // Scroll to the top

    });
  }



  constructor(private zone: NgZone, private conversationService: DiscussionService,private userService: ProfilService, private authentificationService: AuthentificationService2, private parentComponent: ConversationsPageComponent){
    this.profils = this.userService.getProfils();
    this.conversations = this.conversationService.discussions;
    this.filteredConversations = this.conversations;
    console.debug('### ChatList component');
    this.you = this.authentificationService.getCurrentUserAddress();
    
  }

  toggleInputField() {
    this.showInputField = !this.showInputField;
  }


  onInputFocus() {
    this.isInputFocused = true;
  }

  onInputBlur() {
    this.isInputFocused = false;
  }


  /**
     * true if the message is from the signed-in user.
     */
  isFromCurrentUser(message : MessageDTO) {
    if(message === undefined){
      return "";
    } else {
      return message.from == this.authentificationService.getCurrentUserAddress();
    }
  }

  /**
   * 
   * @param user user adresse
   * @returns login of the user without the domain
   */
  justLogin(user : string | undefined){
    if(user == undefined){
      return " ";
    }
    const login =user.split("@", 1);
    return login[0];
  }


  selectConversation(conversation: Discussion) {
    
    this.parentComponent.selectConversation(conversation);
    this.selectedConversation = conversation;
    conversation.newMessage = false;
    console.log("Selected", this.selectedConversation);
    console.log("CONV", this.conversations);

    //console.log("### last message", this.selectedConversation.messages[this.selectedConversation.messages.length-1].body);
}


deleteConversation(){
  let index = this.conversations.indexOf(this.selectedConversation)
  if(index >=0){
    if(index === this.conversations.length - 1){
      if(this.conversations.length === 0){
        this.selectedConversation = null;
        console.log("conversation selecetd", this.selectedConversation);
        this.noConversation = true;
      } else {
       this.conversationService.deleteDiscussion(this.selectedConversation);
        this.selectConversation(this.conversations[index-1]);
        console.log("conversation selecetd", this.selectedConversation);
      }
     
    } else {
      this.conversationService.deleteDiscussion(this.selectedConversation);
      console.log("conversation deleted", this.selectedConversation);
      console.log("taille array", this.conversations.length);
      this.selectConversation(this.conversations[index]);
      console.log("conversation selecetd", this.selectedConversation);
      
      
    }
    
    
  } else {
    console.debug("Pb with delete conv");
  }
  
}



addConversation(){
  console.debug("add conversation");
  const trimmedUsername = this.newConversationUsername.trim();
  if (trimmedUsername !== '') {
    const newConversationId = `conversation${this.conversations.length + 1}`;
    
    const newConversation = new Discussion({
      with: trimmedUsername,
      messages: [],
    });

     // Add the new conversation to the beginning of the array

    this.conversations.unshift(newConversation);
    this.newConversationUsername = '';
    // this.activateConversation(newConversation.getId());
    console.log("conversation added");
    this.toggleInputField();
    this.scrollToTop();
    this.selectConversation(newConversation);
    console.debug("CONVERSATION SELECTED", newConversation.isSelected);
    console.debug("CONVERSATION SELECTED", this.selectedConversation);
  }
}

  

  searchConv(){

     // If searchQuery is empty, show all conversations, otherwise filter
     if (this.searchQuery.trim() === "") {

      // Show all conversations
      this.filteredConversations = this.conversations;
      console.log('SEARCH EMPTY');

    } else {
      // Filter conversations based on the search query
      this.filteredConversations = this.conversations.filter(conv => {

          const login = this.justLogin(conv.with).toLowerCase();
          const searchQueryLower = this.searchQuery.toLowerCase();
        
          return login.includes(searchQueryLower);
      });
      console.log('SEARCH....', this.filteredConversations);  
    }

  }



  clearSearch(){
    this.searchQuery="";
    this.filteredConversations=this.conversations;
  }



}
