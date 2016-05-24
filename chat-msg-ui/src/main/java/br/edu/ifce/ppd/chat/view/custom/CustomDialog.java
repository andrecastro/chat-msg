package br.edu.ifce.ppd.chat.view.custom;

import br.edu.ifce.ppd.chat.view.UserForm;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import static java.util.stream.Collectors.toList;

public class CustomDialog extends JDialog
        implements ActionListener,
        PropertyChangeListener {

    private Form form;
    private JOptionPane optionPane;
    private String btnString1 = "Salvar";
    private String btnString2 = "Cancelar";

    /**
     * Creates the reusable dialog.
     */
    public CustomDialog(Form form, String title) {
        super((Frame) null, true);

        setTitle(title);

        this.form = form;

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(form,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
//                textField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
//        textField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        pack();
    }

    /**
     * This method handles events for the text field.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /**
     * This method reacts to state changes in the option pane.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
                if (form.isFormValid()) {
                    exit();
                } else {
                    //text was invalid
                    List<String> errors = form.getLastInvalidStatus();
                    Object[] lables = errors.stream().map(error -> "- " + error).collect(toList()).toArray();
                    JOptionPane.showMessageDialog(this, lables, "Try again", JOptionPane.ERROR_MESSAGE);
                }
            } else { //user closed dialog or clicked cancel
                exit();
            }
        }
    }

    /**
     * This method clears the dialog and hides it.
     */
    public void exit() {
        dispose();
    }

    public void open() {
        setVisible(true);
    }

    public static CustomDialog createDialog(Form form, String title) {
        return new CustomDialog(form, title);
    }
}