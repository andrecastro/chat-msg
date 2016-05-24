package br.edu.ifce.ppd.chat;

import br.edu.ifce.ppd.chat.view.MainView;
import br.edu.ifce.ppd.ws.ChatService;
import br.edu.ifce.ppd.ws.ChatServiceImplService;
import br.edu.ifce.ppd.ws.UserService;
import br.edu.ifce.ppd.ws.UserServiceImplService;

/**
 * Created by andrecoelho on 5/22/16.
 */
public class App {

    public static void main(String... args) throws Exception {
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        ChatServiceImplService chatServiceImplService = new ChatServiceImplService();

        UserService userService = userServiceImplService.getUserServiceImplPort();
        ChatService chatService = chatServiceImplService.getChatServiceImplPort();

        new MainView(userService, chatService);
    }

}
