//package fr.mightycode.cpoo.server.controller;
//
//
//import fr.mightycode.cpoo.server.dto.ConversationDTO;
//import fr.mightycode.cpoo.server.dto.MessageDTO;
//import fr.mightycode.cpoo.server.model.Conversation;
//import fr.mightycode.cpoo.server.model.Message;
//import fr.mightycode.cpoo.server.service.ConversationService;
//import fr.mightycode.cpoo.server.service.RouterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("conversation")
//@CrossOrigin
//public class ConversationController {
//
//    @Autowired
//    private ConversationService service;
//
//    @Autowired
//    private RouterService routerService;
//
//    @Value("leafline")
//    private String serverDomain;
//
//    @GetMapping(path = "listConversation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public List<ConversationDTO> getAllConversation(@RequestBody final Principal user) {
//        List<ConversationDTO> temp = null;
//        try {
//            temp = service.getAllConversation(user.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return temp;
//    }
//
//    @PostMapping(path = "add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ConversationDTO addConversation(@RequestBody ConversationDTO t) {
//        Conversation temp = null;
//        try {
//            temp = service.newConversation(new Conversation(t));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ConversationDTO(temp);
//    }
//
//    @PostMapping(path = "sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public MessageDTO sendMessage(final Principal user, @RequestBody final MessageDTO message) {
//        try{
//            RouterService.Message routerMessage = new RouterService.Message(
//                    UUID.randomUUID(),
//                    System.currentTimeMillis(),
//                    user.getName() + "@" + serverDomain,
//                    message.to(),
//                    message.body()
//            );
//
//            // Route the message
//            routerService.routeMessage(routerMessage);
//            Message temp = new Message(routerMessage);
//            service.sendMessage(temp);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return message;
//    }
//
//    @GetMapping(path = "receiveMessage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public MessageDTO receiveMessage(@RequestBody MessageDTO message) {
//        try{
//            service.receiveMessage(new Message(message));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return message;
//    }
//
//    @DeleteMapping(path = "deleteConversation", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void deleteConversation(@RequestBody ConversationDTO conversation) {
//        service.deleteConversation(new Conversation(conversation));
//    }
//
//    @GetMapping(path = "/{conversationID}/listMessage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//        public List<MessageDTO> getConversation(@PathVariable("conversationID") UUID id) {
//        List<Message> messages = service.getConversation(id);
//        List<MessageDTO> messagesDTO = null;
//        for(Message m : messages){
//            messagesDTO.add(new MessageDTO(m));
//        }
//        return messagesDTO;
//    }
//
//
//
//
//}
