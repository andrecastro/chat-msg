package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.ws.ChatService;
import br.edu.ifce.ppd.ws.UserService;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class MainView extends JFrame {

    private ChatView chatView;
    private UsersView usersView;
    private JToolBar toolBar;

    public MainView(UserService userService, ChatService chatService) {
        init(userService, chatService);
    }

    private void init(UserService userService, ChatService chatService) {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton refreshUsers = new JButton("Refresh Users View");
        refreshUsers.addActionListener(e -> refreshUsers());

        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(refreshUsers);

        chatView = new ChatView(userService, chatService);
        usersView = new UsersView(userService, chatView);

        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.PAGE_START);
        add(chatView, CENTER);
        add(usersView, EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(830, 630));
        setTitle("Admin Chat");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void refreshUsers() {
        chatView.updateUsersComboBox();
        usersView.updateUsersNearBy(chatView.getCurrentUser());
    }
}
