package br.edu.ifce.ppd.chat.service.impl;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.models.UserWrapper;
import br.edu.ifce.ppd.chat.repository.UserRepository;
import br.edu.ifce.ppd.chat.service.UserService;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import javax.jws.WebService;
import java.rmi.RemoteException;
import java.util.List;

import static br.edu.ifce.ppd.chat.helper.UserLocationHelper.isOnlineRelativeToCurrentUser;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by andrecoelho on 5/22/16.
 */
@WebService(endpointInterface = "br.edu.ifce.ppd.chat.service.UserService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserServiceImpl() {
    }

    @Override
    public List<UserWrapper> listAllUsers() {
        try {
             return userRepository.listAllUsers().stream().map(UserWrapper::new).collect(toList());
        } catch (TransactionException | UnusableEntryException | InterruptedException | RemoteException e) {
            e.printStackTrace();
        }

        return emptyList();
    }

    @Override
    public List<UserWrapper> listAllUsersNearBy(User user) {
        try {
            List<User> allUsers = userRepository.listAllUsers();
            allUsers.remove(user);

            return allUsers.stream()
                    .map(u -> {
                        UserWrapper userWrapper = new UserWrapper(u);
                        userWrapper.setOnline(isOnlineRelativeToCurrentUser(u, user));
                        return userWrapper;
                    }).collect(toList());
        } catch (TransactionException | UnusableEntryException | InterruptedException | RemoteException e) {
            e.printStackTrace();
        }

        return emptyList();
    }
}
