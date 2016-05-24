package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.App;
import br.edu.ifce.ppd.chat.service.ChatService;
import br.edu.ifce.ppd.chat.service.UserService;

import javax.swing.*;
import java.awt.*;

import static br.edu.ifce.ppd.chat.App.currentUser;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class MainView extends JFrame {

    private ChatView chatView;
    private UsersView usersView;
    private JToolBar toolBar;

    private UserService userService;
    private ChatService chatService;

    public MainView(ChatService chatService, UserService userService) {
        this.userService = userService;
        this.chatService = chatService;
        init();
    }

    private void init() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton changeLocation = new JButton("Change location");
        changeLocation.addActionListener(e -> changeLocation());

        JButton refreshUsers = new JButton("Refresh Users");
        refreshUsers.addActionListener(e -> refreshUsers());

        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(changeLocation);
        toolBar.add(refreshUsers);

        chatView = new ChatView(chatService);
        usersView = new UsersView(userService, chatView);

        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.PAGE_START);
        add(chatView, CENTER);
        add(usersView, EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(830, 630));
        setTitle(getTitleName());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void refreshUsers() {
        usersView.updateUsersModel();
    }

    private void changeLocation() {
        try {
            App.handleUserInformation(App.currentUser().nickname, "Change location");
            setTitle(getTitleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTitleName() {
        return "MSG Chat - Logged with " +
                currentUser().nickname + " (" + currentUser().x + ":" + currentUser().y + ")";
    }
}
