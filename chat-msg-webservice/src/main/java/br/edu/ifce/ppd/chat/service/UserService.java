package br.edu.ifce.ppd.chat.service;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.models.UserWrapper;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by andrecoelho on 5/22/16.
 */
@WebService
public interface UserService {

    @WebMethod
    List<UserWrapper> listAllUsers();

    @WebMethod
    List<UserWrapper> listAllUsersNearBy(User user);

}
