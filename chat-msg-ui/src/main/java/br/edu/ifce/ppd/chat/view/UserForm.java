package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.view.custom.Form;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.*;

/**
 * Created by andrecoelho on 5/21/16.
 */
public class UserForm extends JPanel implements Form {

    private JTextField nickname;
    private JTextField latitude;
    private JTextField longitude;
    private List<String> lastErrors;

    private User user;

    private boolean changeNickname;
    private String nicknameValue;

    public UserForm(String nicknameValue) {
        this.nicknameValue = nicknameValue;
        this.changeNickname = false;
        init();
    }

    public UserForm() {
        this.changeNickname = true;
        init();
    }

    private void init() {
        JLabel nicknameLabel = new JLabel("Nickname");
        JLabel latitudeLabel = new JLabel("Latitude");
        JLabel longitudeLabel = new JLabel("Longitude");

        nickname = new JTextField();
        latitude = new JTextField();
        longitude = new JTextField();

        if (!changeNickname) {
            nickname.setEditable(false);
            nickname.setText(nicknameValue);
        }

        setLayout(new GridLayout(3, 3));
        setPreferredSize(new Dimension(300, 100));

        add(nicknameLabel);
        add(nickname);

        add(latitudeLabel);
        add(latitude);

        add(longitudeLabel);
        add(longitude);
    }

    @Override
    public boolean isFormValid() {
        lastErrors = new ArrayList<>();
        boolean valid = true;

        String nicknameValue = nickname.getText();
        String latitudeValue = latitude.getText();
        String longitudeValue = longitude.getText();

        if (nicknameValue == null || nicknameValue.trim().isEmpty()) {
            lastErrors.add("Nickname must not be blank.");
            valid = false;
        }

        if (latitudeValue == null || latitudeValue.trim().isEmpty()) {
            lastErrors.add("Latitude must not be blank.");
            valid = false;
        } else if(!isNumber(latitudeValue)) {
            lastErrors.add("Latitude must be a number");
            valid = false;
        }

        if (longitudeValue == null || longitudeValue.trim().isEmpty()) {
            lastErrors.add("Longitude must not be blank.");
            valid = false;
        } else if(!isNumber(longitudeValue)) {
            lastErrors.add("Longitude must be a number");
            valid = false;
        }

        if (valid) {
            user = new User(nicknameValue, valueOf(latitudeValue), valueOf(longitudeValue), true);
        }

        return valid;
    }

    @Override
    public List<String> getLastInvalidStatus() {
        return lastErrors;
    }

    public boolean isNumber(String value) {
        try {
            valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return  false;
        }
    }

    public User getUser() {
        return user;
    }
}
