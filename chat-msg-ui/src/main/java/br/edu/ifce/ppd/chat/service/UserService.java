package br.edu.ifce.ppd.chat.service;

import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.models.UserWrapper;
import br.edu.ifce.ppd.chat.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

import static br.edu.ifce.ppd.chat.helper.UserLocationHelper.isOnlineRelativeToCurrentUser;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public synchronized List<UserWrapper> usersNearBy(User currentUser) throws Exception {

        List<User> users = userRepository.listAllUsersOn();
        users.remove(currentUser);

        List<UserWrapper> userWrappers = new ArrayList<>();

        for (User user: users) {
            UserWrapper userWrapper = new UserWrapper(user);
            userWrapper.setOnline(isOnlineRelativeToCurrentUser(user, currentUser));
            userWrappers.add(userWrapper);
        }

        return userWrappers;
    }

    public synchronized User updateOrCreate(User user) throws Exception{
        return userRepository.updateOrCreate(user);
    }

    public User readWaiting(User targetUser, long timeToWait) throws Exception {
        return userRepository.read(targetUser, timeToWait);
    }
}
