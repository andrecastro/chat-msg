package br.edu.ifce.ppd.chat.service.impl;

import br.edu.ifce.ppd.chat.models.Chat;
import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.repository.ChatRepository;
import br.edu.ifce.ppd.chat.service.ChatService;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import javax.jws.WebService;
import java.rmi.RemoteException;

/**
 * Created by andrecoelho on 5/22/16.
 */
@WebService(endpointInterface = "br.edu.ifce.ppd.chat.service.ChatService")
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatServiceImpl() {
    }

    @Override
    public Chat retrieveChat(User user1, User user2) {
        try {
            return chatRepository.readChat(user1.nickname, user2.nickname);
        } catch (RemoteException | TransactionException | UnusableEntryException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
