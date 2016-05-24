package br.edu.ifce.ppd.chat.view;

import br.edu.ifce.ppd.chat.models.Chat;
import br.edu.ifce.ppd.chat.models.Message;
import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.service.ChatService;
import br.edu.ifce.ppd.chat.view.helper.Assets;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static br.edu.ifce.ppd.chat.App.currentUser;
import static java.util.stream.Collectors.toList;
import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * Created by andrecoelho on 2/14/16.
 */
public class ChatView extends JPanel {

    private static final String MESSAGE_TEMPLATE = "<p style=\"padding: 5px; border-bottom: " +
            "1px solid black; width: 450px\"><b>%s: </b> %s</p>";

    private static final String ERROR_MESSAGE_TEMPLATE = "<p style=\"padding: 5px; border-bottom: " +
            "1px solid black; width: 450px\"><font color=\"red\"><b>***%s***: </b> %s </font></p>";

    protected final ChatService chatService;
    private JTextPane textView;
    private JTextArea textWrite;

    private Chat currentChat;
    private User targetUser;

    private final Object chatLock;

    private boolean chatting = false;
    private boolean locked = false;

    public ChatView(ChatService chatService) {
        this.chatService = chatService;
        this.chatLock = "lock";
        this.init();
    }

    public void startChatWith(User user) {
        targetUser = user;
        stopChat();
        unlockView();
        cleanChat();
        loadChat();
        startChat();
    }

    public User getTargetUser() {
        return targetUser;
    }

    private void init() {
        textView = new JTextPane();
        textWrite = new JTextArea();
        JButton sendButton = new JButton("Send", Assets.chat());

        textView.setEditable(false);
        textView.setContentType("text/html");
        textView.setText("");

        JScrollPane textViewPanel = new JScrollPane(textView);
        textViewPanel.setPreferredSize(new Dimension(245, 340));
        textViewPanel.setBorder(BorderFactory.createTitledBorder("Chat View"));

        textWrite.setWrapStyleWord(true);
        textWrite.setLineWrap(true);
        textWrite.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    sendText();
                }
            }
        });

        JScrollPane textWritePanel = new JScrollPane(textWrite);
        textWritePanel.setPreferredSize(new Dimension(245, 23));

        sendButton.setPreferredSize(new Dimension(250, 37));
        sendButton.addActionListener(e -> sendText());

        setBorder(createEmptyBorder(2, 2, 2, 2));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 447));

        add(textViewPanel, BorderLayout.NORTH);
        add(textWritePanel, BorderLayout.CENTER);
        add(sendButton, BorderLayout.SOUTH);

        lockView();
    }

    private void sendText() {
        String message = textWrite.getText();

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        textWrite.setText(null);

        synchronized (chatLock) {
            try {
                String formattedMessage = formatMessage(currentUser().nickname, message, MESSAGE_TEMPLATE);

                if (chatService.isAvailableToChat(targetUser, currentUser())) {
                    updateViewText(formattedMessage);
                    currentChat.history.add(new Message(formattedMessage));
                    chatService.sendText(currentChat);
                } else {
                    String errorMessage = formatMessage("ERROR", message, ERROR_MESSAGE_TEMPLATE);
                    updateViewText(errorMessage);
                    chatService.sendTextToQueue(targetUser, currentUser(), formattedMessage);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private void updateViewWithNewMessages() {
            try {
                synchronized (chatLock) {
                    Chat chat = chatService.readChatOrCreateNew(currentUser().nickname, targetUser.nickname);

                    if (chat.history.size() != currentChat.history.size()) {
                        List<Message> newMessages = chat.history.stream()
                                .filter(msg -> !currentChat.history.contains(msg))
                                .collect(toList());
                        newMessages.stream().map(message -> message.text).collect(toList()).forEach(this::updateViewText);
                        currentChat = chat;
                    }
                }

                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void lockView() {
        if (!locked) {
            enableComponents(this, false);
            locked = true;
        }
    }

    private void unlockView() {
        if (locked) {
            enableComponents(this, true);
            locked = false;
        }
    }

    private void cleanChat() {
        textView.setText(null);
    }

    private void loadChat() {
        try {
            currentChat = chatService.readChatOrCreateNew(currentUser().nickname, targetUser.nickname);
            currentChat.history.stream().map(message -> message.text).collect(toList()).forEach(this::updateViewText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopChat() {
        chatting = false;
    }

    private void startChat() {
        chatting = true;
        new Thread(() -> {
            while (chatting) {
                updateViewWithNewMessages();
            }
        }).start();
    }

    private String formatMessage(String playerName, String message, String template) {
        return String.format(template, playerName, message.replace("\n", "br/>"));
    }

    // Recursively disable or enable all components
    // http://stackoverflow.com/questions/10985734/java-swing-enabling-disabling-all-components-in-jpanel
    private void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }
}
