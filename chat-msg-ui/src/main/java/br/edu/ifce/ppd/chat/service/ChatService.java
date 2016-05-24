package br.edu.ifce.ppd.chat.service;

import br.edu.ifce.ppd.chat.models.Message;
import br.edu.ifce.ppd.chat.models.UserWrapper;
import br.edu.ifce.ppd.chat.service.jms.JmsService;
import br.edu.ifce.ppd.chat.models.Chat;
import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.repository.ChatRepository;

import javax.jms.JMSException;
import java.io.Serializable;
import java.util.List;

import static br.edu.ifce.ppd.chat.helper.UserLocationHelper.isOnlineRelativeToCurrentUser;
import static net.jini.space.JavaSpace.NO_WAIT;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class ChatService implements Serializable {

    private UserService userService;
    private ChatRepository chatRepository;
    private JmsService jmsService;

    public ChatService(ChatRepository chatRepository, UserService userService, JmsService jmsService) {
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.jmsService = jmsService;
    }

    public Chat readChatOrCreateNew(String user1, String user2) throws Exception {
        return chatRepository.readChatOrCreateNew(user1, user2);
    }

    public Integer countMessages(String user1, String user2) throws Exception {
        return chatRepository.countMessages(user1, user2);
    }

    public boolean isAvailableToChat(User targetUser, User currentUser) throws Exception {
        User user = null;
        int tries = 0;

        while (user == null && tries < 10) {
            user = userService.readWaiting(targetUser, NO_WAIT);
            tries++;
        }

        return user != null && isOnlineRelativeToCurrentUser(user, currentUser);
    }

    public void sendText(Chat chat) throws Exception {
        chatRepository.update(chat);
    }

    public void sendTextToQueue(User targetUser, User currentUser, String message) throws Exception {
        String queueName = getQueueName(targetUser.nickname, currentUser.nickname);

        if (jmsService.createQueueIfNotExists(queueName)) {
            jmsService.sendMessage(queueName, message);
        }
    }

    public void readAllChatFromQueueOf(User user) throws Exception {
        List<UserWrapper> userWrappers = userService.usersNearBy(user);

        for (UserWrapper userWrapper: userWrappers) {
            consumeMessagesOfQueue(user.nickname, userWrapper.getUser().nickname);
        }
    }

    private void consumeMessagesOfQueue(String user1, String user2) throws Exception {
        String queueName = getQueueName(user1, user2);

        if (jmsService.destinationExists(queueName)) {
            Chat chat = chatRepository.readChatOrCreateNew(user1, user2);

            while (jmsService.numberOfMessagesIn(queueName) != 0) {
                String message = (String) jmsService.readMessage(queueName);
                chat.history.add(new Message(message));
            }

            chatRepository.update(chat);
        }

    }

    private String getQueueName(String user1, String user2) throws JMSException {
        String queueName = buildQueueName(user1, user2);
        if (jmsService.destinationExists(queueName)) {
            return queueName;
        } else {
            return buildQueueName(user2, user1);
        }
    }

    private String buildQueueName(String user1, String user2) {
        return user1 + "_" + user2;
    }
}
