import { Injectable } from '@angular/core';

import { AuthentificationService2 } from './athentication-service2.service';
import { Observable, firstValueFrom, map, repeat, retry, share, takeUntil } from 'rxjs';
import { MessageDTO } from '../api';
import { NewMessageDTO } from '../api/model/newMessageDTO'
import { MessageService } from '../api/api/message.service';

export class Discussion {

  with: string;           // address of the interlocutor
  messages: MessageDTO[]; // messages of the discussion
  isSelected: boolean;
  newMessage : boolean;
  firstDate: Date | null;
  picture: string;


  constructor(props: { with: string, messages: MessageDTO[] }) {
    this.with = props.with;
    this.messages = props.messages;
    this.isSelected = false;
    this.newMessage = false;
    this.firstDate = null;
    this.picture= "feuille7.svg";
    
  }

  /**
   * @return the id of the discussions (built from the address of the interlocutor)
   */
  getId() {
    return this.with.replace('@', '');
  }
}


@Injectable({
  providedIn: 'root'
})
export class DiscussionService {

  public discussions : Discussion[] = []



  profilPicture : string[]=[
    "feuille1.svg", "feuille2.svg", "feuille3.svg", "feuille4.svg",
    "feuille5.svg", "feuille6.svg", "feuille7.svg"
  ]

  constructor(private authentificationService : AuthentificationService2, private messageService : MessageService ) {
    console.debug('### DiscussionService()');
  }



  /**
   * Initialize discussions of the current user.
   */
  async initializeDiscussions() {



    // Get all messages related to the current user
    let messages = await firstValueFrom(this.messageService.messagesGet());

    console.debug('### messages initialized');

    // Reset all discussions (without reassigning the array)
    this.discussions.length = 0;




    // Build discussions from messages
    messages.forEach((message: MessageDTO) => {

      // Get the discussion related to the message
      let discussion = this.getTargetDiscussion(message);

      //get random number between 0 and 6 
      let index = Math.floor(Math.random() * 7);

      //initialized conversation picture
      discussion.picture = this.profilPicture[index];

      // Add the message to the discussion
      this.addMessageToDiscussion(discussion, message, true);
    });



  }

   /**
   * Start listening for new messages and updating discussions.
   * @param stopListening Control observable: any event sent on this observable stops the process
   * @param onNewMessage Callback function called to notify each new message received
   */
   listenForNewMessages(stopListening: Observable<void>, onNewMessage: (message: MessageDTO) => void) {

    let source = new EventSource("/serverapi/message");

    source.onmessage = (event: MessageEvent) => {

      // Build the message from received data
      const message = JSON.parse(event.data);

      // Get the discussion related to the message
      const discussion = this.getTargetDiscussion(message);

      // Add the message to the discussion
      this.addMessageToDiscussion(discussion, message);

      // Notify the new message
      onNewMessage(message);
    }

    source.onerror = ev => {
      console.error('### Error listenMessage', ev);
    }

    stopListening.subscribe(_ => source.close());
  }



  /**
   * Search for the discussion related to a given message in the array of discussions.
   * Create a new one and add it the array of discussions if it does not exist yet.
   * @param message The message
   */
  private getTargetDiscussion(message: MessageDTO) {

    // If message is sent by current user: search for an existing discussion with the receiver,
    // otherwise, if message is received by current user: search for an existing discussion with the sender
    let _with = <string>(message.from === this.authentificationService.getCurrentUserAddress() ? message.to : message.from);
    let discussion = this.discussions.find(discussion => discussion.with === _with);

    // If no discussion has been found, create a new one and add it to the array of discussions
    if (!discussion) {
      discussion = new Discussion({ with: _with, messages: [] });
      this.discussions.unshift(discussion);
    }

    return discussion;
  }

    /**
   * Add a message at the beginning or at the end of a discussion.
   * If the message is already present, nothing is done.
   * @param discussion The discussion
   * @param message The message
   * @param beginning True if the message must be added at the beginning of the discussion (default: false)
   */
  private addMessageToDiscussion(discussion: Discussion, message: MessageDTO, beginning = false) {

    // If a message with the same id is already present in the discussion, nothing to do
    if (discussion.messages.find(m => m.id == message.id))
      return;

    // Add the message to the discussion
    if (beginning)
      discussion.messages.push(message);
    else
      discussion.messages.push(message);

    let index = this.discussions.indexOf(discussion);
    this.discussions.splice(index,1);
    this.discussions.unshift(discussion);
    

  }

   /**
   * Send a new message to an interlocutor.
   * @param discussion The discussion
   * @param body The message
   */
   sendMessage(discussion: Discussion, body: string) {

    // Build a new message for the recipient
    const newMessage: NewMessageDTO = { body: body, type: 'text/plain', to: discussion.with };

    console.debug("WANT TO SEND MESSAGE");
    // Send the message to the recipient by posting it to the server
    this.messageService.messagePost(newMessage).subscribe();
    console.debug("MESSAGE SENT");
  }

   /**
   * Create a new empty discussion with a given interlocutor.
   * If a discussion with the interlocutor already exists, no new discussion is created.
   * @param _with The address of the interlocutor
   */
   newDiscussion(_with: string) {

    // Search for an existing discussion with the interlocutor
    let discussion = this.discussions.find(discussion => discussion.with === _with);

    // If a discussion already exists, nothing to do
    if (discussion)
      return;

    // Otherwise, create a new one and add it to the array of discussions
    discussion = new Discussion({ with: _with, messages: [] });
    this.discussions.unshift(discussion);
  }


  /**
   * Delete the given discussion 
   * @param discussion discussion to delete
   */
  deleteDiscussion(discussion : Discussion){
    discussion.messages.forEach( (m : MessageDTO) => {
      this.messageService.messageIdDelete(m.id!).subscribe(
        () => console.log("Message deleted:", m),
      (error) => console.error("Error deleting message:", error)
      );
    }
    );
    let index = this.discussions.indexOf(discussion);
    this.discussions.splice(index,1);
    }




}




