package br.edu.ifce.ppd.chat.repository;

import br.edu.ifce.ppd.chat.helper.SpaceWrapper;
import br.edu.ifce.ppd.chat.models.User;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by andrecoelho on 5/21/16.
 */
public class UserRepository {

    private SpaceWrapper<User> spaceWrapper;

    public UserRepository(JavaSpace space) {
        this.spaceWrapper = new SpaceWrapper<>(space);
    }

    public synchronized List<User> listAllUsers() throws TransactionException, UnusableEntryException,
            RemoteException, InterruptedException {
        return spaceWrapper.readAll(new User());
    }

    public synchronized List<User> listAllUsersOn() throws TransactionException, UnusableEntryException,
            RemoteException, InterruptedException {
        return listAllUsers().stream().filter(user -> user.on).collect(toList());
    }

    public synchronized void write(User user) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {
        spaceWrapper.write(user);
    }

    public synchronized User take(User user, int timeToWait) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {
        return spaceWrapper.take(user, timeToWait);
    }

    public synchronized User read(User user, long time) throws RemoteException, TransactionException,
            InterruptedException, UnusableEntryException {
        return spaceWrapper.read(user, time);
    }

    public synchronized User updateOrCreate(User userToUpdate) throws RemoteException,
            TransactionException, InterruptedException, UnusableEntryException {

        // try to take an existent or create new
        User user = take(new User(userToUpdate.nickname), 2000);

        if (user == null) {
            user = userToUpdate;
        }

        user.x = userToUpdate.x;
        user.y = userToUpdate.y;
        user.on = userToUpdate.on;

        write(user);
        return user;
    }
}
