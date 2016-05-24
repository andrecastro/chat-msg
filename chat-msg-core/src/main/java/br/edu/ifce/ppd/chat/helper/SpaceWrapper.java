package br.edu.ifce.ppd.chat.helper;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static net.jini.space.JavaSpace.NO_WAIT;
import static sun.net.InetAddressCachePolicy.FOREVER;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class SpaceWrapper<T extends Entry> implements Serializable {

    private JavaSpace space;

    public SpaceWrapper(JavaSpace space) {
        this.space = space;
    }

    public List<T> readAll(T template) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        List<T> entries = new ArrayList<>();

        while (true) {
            T entry = (T) space.take(template, null, NO_WAIT);

            if (entry == null) {
                break;
            }

            entries.add(entry);
        }

        for (T t: entries) {
            space.write(t, null, FOREVER);
        }

        return entries;
    }

    public T read(T template) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        return (T) space.read(template, null, NO_WAIT);
    }

    public T read(T template, long time) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        return (T) space.read(template, null, time);
    }

    public T take(T template, int timeToWait) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        return (T) space.take(template, null, timeToWait);
    }

    public T take(T template) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        return (T) space.take(template, null, NO_WAIT);
    }

    public void write(T entry) throws RemoteException, TransactionException {
        space.write(entry, null, FOREVER);
    }
}
