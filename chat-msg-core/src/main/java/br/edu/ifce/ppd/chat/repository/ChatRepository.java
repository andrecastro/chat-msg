package br.edu.ifce.ppd.chat.repository;

import br.edu.ifce.ppd.chat.helper.SpaceWrapper;
import br.edu.ifce.ppd.chat.models.Chat;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by andrecoelho on 5/21/16.
 */
public class ChatRepository {

    private SpaceWrapper<Chat> spaceWrapper;

    public ChatRepository(JavaSpace javaSpace) {
        this.spaceWrapper = new SpaceWrapper<>(javaSpace);
    }

    public Chat readChatOrCreateNew(String user1, String user2) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {

        Chat chat = readChat(user1, user2);

        // if not found, create new one
        if (chat == null) {
            chat = new Chat(user1, user2);
            chat.history = new ArrayList<>();
        }

        return chat;
    }

    public Integer countMessages(String user1, String user2) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {
        Chat chat = readChat(user1, user2);

        if (chat == null) {
            return 0;
        }

        return chat.history.size();
    }

    public Chat readChat(String user1, String user2) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {
        // try to get in the order user1, user2
        Chat template = new Chat(user1, user2);
        Chat chat =  spaceWrapper.read(template);

        // if not found, try to get from the order user2, user1
        if (chat == null) {
            template = new Chat(user2, user1);
            chat = spaceWrapper.read(template);
        }

        return chat;
    }

    public synchronized Chat takeChat(String user1, String user2) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {

        // try to get in the order user1, user2
        Chat template = new Chat(user1, user2);
        Chat chat =  spaceWrapper.take(template);

        // if not found, try to get from the order user2, user1
        if (chat == null) {
            template = new Chat(user2, user1);
            chat = spaceWrapper.take(template);
        }

        return chat;
    }

    public synchronized void update(Chat chat) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {

        // take an old chat to override it
        Chat oldChat = takeChat(chat.user1, chat.user2);

        if (oldChat != null) {
            oldChat.history = chat.history;
            spaceWrapper.write(oldChat);
        } else {
            spaceWrapper.write(chat);
        }
    }

}
