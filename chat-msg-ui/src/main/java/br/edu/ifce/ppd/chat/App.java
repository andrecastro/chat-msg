package br.edu.ifce.ppd.chat;

import br.edu.ifce.ppd.chat.helper.Lookup;
import br.edu.ifce.ppd.chat.service.jms.JmsService;
import br.edu.ifce.ppd.chat.models.User;
import br.edu.ifce.ppd.chat.repository.ChatRepository;
import br.edu.ifce.ppd.chat.repository.UserRepository;
import br.edu.ifce.ppd.chat.service.ChatService;
import br.edu.ifce.ppd.chat.service.UserService;
import br.edu.ifce.ppd.chat.view.MainView;
import br.edu.ifce.ppd.chat.view.UserInformationControl;
import net.jini.space.JavaSpace;
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.net.MalformedURLException;
import java.util.Hashtable;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class App {

    private static User currentUser;

    private static UserInformationControl userInformationControl;

    public static void main(String... args) {

        JavaSpace space = getSpace();
        JmsService jmsService;
        try {
            jmsService = getJmsService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserRepository userRepository = new UserRepository(space);
        ChatRepository chatRepository = new ChatRepository(space);

        UserService userService = new UserService(userRepository);
        ChatService chatService = new ChatService(chatRepository, userService, jmsService);

        userInformationControl = new UserInformationControl(userService, chatService);

        try {
            if (!handleUserInformation(null, "User Information")) {
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                currentUser.on = false;
                userService.updateOrCreate(currentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        new MainView(chatService, userService);
    }

    public static boolean handleUserInformation(String nickname, String title) throws Exception {
        User user = userInformationControl.changeUserInformation(nickname, title);

        if (user == null) {
            return false;
        }

        setCurrentUser(user);
        return true;
    }

    private static JavaSpace getSpace() {
        configureSystemForJavaSpace();
        Lookup lookup = new Lookup(JavaSpace.class);
        JavaSpace space = (JavaSpace) lookup.getService();

        if (space == null) {
            JOptionPane.showMessageDialog(null, "Space not found");
            return null;
        }

        return space;
    }

    private static JmsService getJmsService() throws MalformedURLException, JMSException, NamingException {
        String jmsProviderUrlService = System.getProperty("java.naming.provider.url.service");
        String jmsProviderUrlAdmin = System.getProperty("java.naming.provider.url.admin");

        if (jmsProviderUrlService == null || jmsProviderUrlService.trim().isEmpty()) {
            jmsProviderUrlService = "tcp://localhost:3035/";
        }

        if (jmsProviderUrlAdmin == null || jmsProviderUrlAdmin.trim().isEmpty()) {
            jmsProviderUrlAdmin = "tcp://localhost:3035/";
        }

        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, jmsProviderUrlService);

        Context context = new InitialContext(properties);
        JmsAdminServerIfc jmsAdminServer = AdminConnectionFactory.create(jmsProviderUrlAdmin);

        return new JmsService(jmsAdminServer, context);
    }

    private static void configureSystemForJavaSpace() {
        System.setProperty("java.security.policy", App.class.getClassLoader().getResource("policy.all").getPath());
        System.setSecurityManager(new SecurityManager());
    }

    public static User currentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
