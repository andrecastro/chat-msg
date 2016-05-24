package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.UserAdmin;
import br.edu.ifce.ppd.chat.view.custom.UserListCellRender;
import br.edu.ifce.ppd.ws.UserService;
import br.edu.ifce.ppd.ws.UserWrapper;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class UsersView extends JList<UserAdmin> {

    private DefaultListModel<UserAdmin> usersModel;
    private UserService userService;

    public UsersView(UserService userService, ChatView chatView) {
        init(userService, chatView);
    }

    private void init(UserService userService, ChatView chatView) {
        this.usersModel = new DefaultListModel<>();
        this.userService = userService;

        chatView.setUsersView(this);

        setCellRenderer(new UserListCellRender());
        setPreferredSize(new Dimension(200, 447));
        setBorder(BorderFactory.createTitledBorder("Users"));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayoutOrientation(JList.VERTICAL);
        setVisibleRowCount(-1);

        addListSelectionListener(e -> {
            UserAdmin user = UsersView.this.getSelectedValue();

            if (user != null) {
                chatView.updateChatViewWith(user.getUserWrapper());
                repaint();
            }
        });

        updateUsersNearBy(chatView.getCurrentUser());
        setModel(usersModel);
    }

    public void updateUsersNearBy(UserWrapper currentUser) {
        if (currentUser != null) {
            List<UserWrapper> userWrappers = userService.listAllUsersNearBy(currentUser.getUser());
            usersModel.removeAllElements();
            userWrappers.stream().map(UserAdmin::new).forEach(usersModel::addElement);
        }
    }
}
