package br.edu.ifce.ppd.chat.service;

import br.edu.ifce.ppd.chat.models.Chat;
import br.edu.ifce.ppd.chat.models.User;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by andrecoelho on 5/22/16.
 */
@WebService
public interface ChatService {

    @WebMethod
    Chat retrieveChat(User user1, User user2);

}
