package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.service.ChatService;
import br.edu.ifce.ppd.chat.service.UserService;
import br.edu.ifce.ppd.chat.view.custom.CustomDialog;

/**
 * Created by andrecoelho on 5/22/16.
 */
public class UserInformationControl {

    private UserService userService;
    private ChatService chatService;

    public UserInformationControl(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    public User changeUserInformation(String nickname, String windowTitle) throws Exception {

        UserForm userForm;

        if (nickname == null) {
            userForm = new UserForm();
        } else {
            userForm = new UserForm(nickname);
        }

        CustomDialog.createDialog(userForm, windowTitle).open();
        User user = userForm.getUser();

        if (user == null) {
            return null;
        }

        userService.updateOrCreate(user);
        chatService.readAllChatFromQueueOf(user);
        return user;
    }
}
