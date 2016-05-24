package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.UserAdmin;
import br.edu.ifce.ppd.ws.Chat;
import br.edu.ifce.ppd.ws.ChatService;
import br.edu.ifce.ppd.ws.Message;
import br.edu.ifce.ppd.ws.User;
import br.edu.ifce.ppd.ws.UserService;
import br.edu.ifce.ppd.ws.UserWrapper;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * Created by andrecoelho on 2/14/16.
 */
public class ChatView extends JPanel {

    private final UserService userService;
    private final ChatService chatService;

    private JTextPane textView;
    private JComboBox<UserAdmin> usersComboBox;

    private UsersView usersView;
    private DefaultComboBoxModel<UserAdmin> model;

    public ChatView(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
        init();
    }

    private void init() {
        textView = new JTextPane();
        textView.setEditable(false);
        textView.setContentType("text/html");
        textView.setText(null);

        model = new DefaultComboBoxModel<>();
        updateUsersComboBox();

        usersComboBox = new JComboBox<>(model);
        usersComboBox.addActionListener(e -> {
            textView.setText(null);
            usersView.updateUsersNearBy(getCurrentUser());
        });

        JScrollPane textViewPanel = new JScrollPane(textView);
        textViewPanel.setPreferredSize(new Dimension(245, 580));
        textViewPanel.setBorder(BorderFactory.createTitledBorder("Chat View"));

        setBorder(createEmptyBorder(2, 2, 2, 2));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 447));


        add(usersComboBox, BorderLayout.NORTH);
        add(textViewPanel, BorderLayout.CENTER);
    }

    public void updateChatViewWith(UserWrapper userWrapper) {
        UserWrapper currentUser = getCurrentUser();

        if (currentUser != null) {
            Chat chat = chatService.retrieveChat(currentUser.getUser(), userWrapper.getUser());
            textView.setText(null);

            if (chat != null) {
                chat.getHistory().stream().map(Message::getText).collect(toList()).forEach(this::updateViewText);
            } else {
                updateViewText("Nothing to show.");
            }
        }
    }

    public void updateUsersComboBox() {
        textView.setText(null);
        model.removeAllElements();
        List<UserWrapper> allUsers = userService.listAllUsers();
        allUsers.stream().map(UserAdmin::new).collect(toList()).forEach(model::addElement);
    }

    public UserWrapper getCurrentUser() {
        UserAdmin userAdmin = (UserAdmin)usersComboBox.getSelectedItem();

        if (userAdmin != null) {
            return userAdmin.getUserWrapper();
        }

        return null;
    }

    public void setUsersView(UsersView usersView) {
        this.usersView = usersView;
    }

    private synchronized void updateViewText(String message) {
        try {
            HTMLDocument doc = (HTMLDocument) textView.getDocument();
            doc.insertBeforeEnd(doc.getDefaultRootElement().getElement(0), message);
            textView.setCaretPosition(textView.getDocument().getLength());
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }
}
