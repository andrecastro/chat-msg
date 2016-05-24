package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.UserWrapper;
import br.edu.ifce.ppd.chat.service.UserService;
import br.edu.ifce.ppd.chat.view.custom.UserListCellRender;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.edu.ifce.ppd.chat.App.currentUser;
import static java.util.stream.Collectors.toList;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class UsersView extends JList<UserWrapper> {

    private DefaultListModel<UserWrapper> usersModel;
    private UserService userService;

    private List<UserWrapper> currentElements;
    private ChatView chatView;

    public UsersView(UserService userService, ChatView chatView) {
        this.userService = userService;
        this.chatView = chatView;
        init();
    }

    private void init() {
        currentElements = Collections.synchronizedList(new ArrayList<>());
        usersModel = new DefaultListModel<>();

        setCellRenderer(new UserListCellRender());
        setPreferredSize(new Dimension(200, 447));
        setBorder(BorderFactory.createTitledBorder("Users"));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayoutOrientation(JList.VERTICAL);
        setVisibleRowCount(-1);

        addListSelectionListener(e -> {
            UserWrapper userWrapper = UsersView.this.getSelectedValue();

            if (userWrapper != null) {
                userWrapper.setHasNewMessages(false);
                chatView.startChatWith(userWrapper.getUser());
                repaint();
            }
        });

        updateUsersModel();
        countMessages(false);
        setModel(usersModel);

        new Thread(() -> {
            while (true) {
                try {
                    updateUsersModel();
                    Thread.sleep(120 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    countMessages(true);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void updateUsersModel() {
        try {
            // transform wrap users
            List<UserWrapper> users = userService.usersNearBy(currentUser());

            // filter only users that are in the current list but not in the space
            List<UserWrapper> removedUsers = currentElements.stream()
                    .filter(user -> !users.contains(user)).collect(toList());

            // filter only users that are not in the current list but are in the space
            List<UserWrapper> newUsers = users.stream()
                    .filter(user -> !currentElements.contains(user)).collect(toList());

            // update lists
            currentElements.stream().filter(users::contains).forEach(e -> e.setOnline(users.get(users.indexOf(e)).isOnline()));
            currentElements.removeAll(removedUsers);
            currentElements.addAll(newUsers);

            removedUsers.forEach(usersModel::removeElement);
            newUsers.forEach(usersModel::addElement);
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countMessages(boolean updateHasNewMessages) {
        for (UserWrapper userWrapper: currentElements) {
            try {
                Integer numberOfMessages = chatView.chatService
                        .countMessages(currentUser().nickname, userWrapper.getUser().nickname);

                if (updateHasNewMessages) {

                    if(!userWrapper.hasNewMessages()) {
                        boolean hasNewMessages = !numberOfMessages.equals(userWrapper.getNumberOfMessages());
                        userWrapper.setHasNewMessages(hasNewMessages);
                    }

                    if (userWrapper.getUser().equals(chatView.getTargetUser())) {
                        userWrapper.setHasNewMessages(false);
                    }
                }

                userWrapper.setNumberOfMessages(numberOfMessages);
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
