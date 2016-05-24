package br.edu.ifce.ppd.chat.helper;

import br.edu.ifce.ppd.chat.models.User;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by andrecoelho on 5/21/16.
 */
public class UserLocationHelper {

    public synchronized static boolean isOnlineRelativeToCurrentUser(User user, User currentUser) {
        double xr = currentUser.x - user.x;
        double yr = currentUser.y - user.y;
        double result = sqrt((pow(xr, 2) + pow(yr, 2)));

        return result <= 200.0;
    }
}
