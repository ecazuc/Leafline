//package fr.mightycode.cpoo.server;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureWebTestClient
//class ServerApplicationTests {
//
//  @Autowired
//  private MockMvc mvc;
//
//  @Autowired
//  private WebTestClient webClient;
//
//  @Test
//  void testSignUpSignInSignOut() throws Exception {
//
//    // Signing up an existing account should fail with CONFLICT
//    mvc.perform(post("/user/signup")
//        .contentType(APPLICATION_JSON)
//        .content("""
//          {
//            "login": "admin",
//            "password": "admin"
//          }"""))
//      .andExpect(status().isConflict());
//
//    // Signing up a non-existing account should succeed
//    mvc.perform(post("/user/signup")
//        .contentType(APPLICATION_JSON)
//        .content("""
//          {
//            "login": "test",
//            "password": "test"
//          }"""))
//      .andExpect(status().isOk());
//
//    // Signing up an existing account should fail with CONFLICT
//    mvc.perform(post("/user/signup")
//        .contentType(APPLICATION_JSON)
//        .content("""
//          {
//            "login": "test",
//            "password": "test"
//          }"""))
//      .andExpect(status().isConflict());
//
//    // Signing in with invalid credentials should fail with UNAUTHORIZED
//    webClient.post()
//      .uri("/user/signin")
//      .contentType(APPLICATION_JSON)
//      .bodyValue("""
//        {
//          "login": "user",
//          "password": "invalid"
//        }""")
//      .exchange()
//      .expectStatus().isUnauthorized();
//
//    // Signing in a fresh account should succeed
//    webClient.post()
//      .uri("/user/signin")
//      .contentType(APPLICATION_JSON)
//      .bodyValue("""
//        {
//          "login": "user",
//          "password": "user"
//        }""")
//      .exchange()
//      .expectStatus().isOk();
//
//    // Signing out a signed in account should succeed
//    // FIXME: session cookie is not returned
////    webClient.post()
////      .uri("/user/signout")
////      .exchange()
////      .expectStatus().isOk();
//  }
//}
