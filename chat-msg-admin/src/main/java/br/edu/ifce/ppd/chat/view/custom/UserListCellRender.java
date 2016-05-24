package br.edu.ifce.ppd.chat.view.custom;

import br.edu.ifce.ppd.chat.models.UserAdmin;
import br.edu.ifce.ppd.chat.view.helper.Assets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

/**
 * Created by andrecoelho on 5/17/16.
 */
public class UserListCellRender implements ListCellRenderer<UserAdmin> {


    @Override
    public Component getListCellRendererComponent(JList<? extends UserAdmin> list, UserAdmin value, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer userLabel = new DefaultListCellRenderer();
        userLabel = (DefaultListCellRenderer) userLabel
                .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value.getUserWrapper().isOnline()) {
            userLabel.setIcon(Assets.online());
        } else {
            userLabel.setIcon(Assets.offline());
        }

        userLabel.setHorizontalAlignment(JLabel.LEFT);
        return userLabel;
    }
}
