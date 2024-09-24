import { ChangeDetectorRef, Component, HostListener, Input } from '@angular/core';

import { Profil } from 'src/app/models/profil';

import { Discussion, DiscussionService } from 'src/app/services/discussion.service';
import { AuthentificationService2} from "../../services/athentication-service2.service";
import { Subject } from 'rxjs';




@Component({
  selector: 'app-conversations-page',
  templateUrl: './conversations-page.component.html',
  styleUrls: ['./conversations-page.component.css']
})
export class ConversationsPageComponent {

  showChatList: boolean=true;
  isSmallScreen = window.innerWidth <= 468;

  
  users: Profil[] = [];


  conversations! : Discussion [];

  filteredUsers: Profil[] = [];

  private stopListening = new Subject<void>();

  currentConversation: any;

  currentUser!: string;


  constructor(private changeDetectorRef: ChangeDetectorRef, private conversationService: DiscussionService, private authentificationService: AuthentificationService2) {
    this.conversations = conversationService.discussions;
    this.currentConversation = this.conversations[0];
  }


  async ngOnInit() {
    this.authentificationService.currentUser.subscribe((user) => {
      this.currentUser = this.authentificationService.getCurrentUserAddress();

      // Initialize discussions
     this.conversationService.initializeDiscussions();
     // Start listening for new messages and updating discussions
     this.conversationService.listenForNewMessages(this.stopListening,
       message => {
         console.debug('### new message notified', message);
         //if message from current user
         if(this.currentUser === message.from){
          let discussion = this.conversations.find(conv => conv.with === message.to);
         } else  {
          let discussion = this.conversations.find(conv => conv.with === message.from);
          discussion!.newMessage = true;
         }
         
        
        this.changeDetectorRef.detectChanges();
        console.log(this.currentConversation);
       
       });

    });
     

      
  }






  public selectConversation(conversation: Discussion) {
    // Deselect all conversations first
    this.conversations.forEach(conv => {
          conv.isSelected = false;
   });
  

    // Select the clicked conversation
    conversation.isSelected = true;
    this.currentConversation = conversation;
  

  }


  onUserSelected(user: Profil) {
    // Handle user selection and update filteredUsers
    this.filteredUsers.push(user);
  }





  @HostListener('window:resize', ['$event'])
    onResize(event: any) {
    this.isSmallScreen = event.target.innerWidth <= 468; // Adjust the breakpoint as needed
}

  menu(){
    this.showChatList = !this.showChatList;

  }
  
  ngOnDestroy() {

    // Stop listening for new messages
    this.stopListening.next(void 0);
  }


}
