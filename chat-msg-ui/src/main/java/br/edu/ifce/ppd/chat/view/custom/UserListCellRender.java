package br.edu.ifce.ppd.chat.view.custom;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.models.UserWrapper;
import br.edu.ifce.ppd.chat.view.helper.Assets;

import javax.swing.*;
import java.awt.*;

/**
 * Created by andrecoelho on 5/17/16.
 */
public class UserListCellRender implements ListCellRenderer<UserWrapper> {


    @Override
    public Component getListCellRendererComponent(JList<? extends UserWrapper> list, UserWrapper value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        DefaultListCellRenderer userLabel = new DefaultListCellRenderer();
        userLabel = (DefaultListCellRenderer) userLabel
                .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        User user = value.getUser();
        Boolean hasNewMessages = value.hasNewMessages();

        if (value.isOnline()) {
            userLabel.setIcon(Assets.online());
        } else {
            userLabel.setIcon(Assets.offline());
        }

        userLabel.setHorizontalAlignment(JLabel.LEFT);

        if (isSelected || !hasNewMessages) {
            userLabel.setText(user.nickname);
        } else {
            userLabel.setText(user.nickname + " (new)");
            userLabel.setForeground(Color.GREEN);
        }

        return userLabel;
    }

}
