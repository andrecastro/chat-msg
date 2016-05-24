package br.edu.ifce.ppd.chat;

import br.edu.ifce.ppd.chat.helper.Lookup;
import br.edu.ifce.ppd.chat.repository.ChatRepository;
import br.edu.ifce.ppd.chat.repository.UserRepository;
import br.edu.ifce.ppd.chat.service.impl.ChatServiceImpl;
import br.edu.ifce.ppd.chat.service.impl.UserServiceImpl;
import net.jini.space.JavaSpace;

import javax.swing.JOptionPane;
import javax.xml.ws.Endpoint;

/**
 * Created by andrecoelho on 5/22/16.
 */
public class App {

    public static final String CHAT_WEBSERVICE_URL_PROPERTY_NAME = "chat.webservice.url";
    public static final String SECURITY_POLICY_PROPERTY_NAME = "java.security.policy";

    public static void main(String[] args) {
        JavaSpace space = getSpace();

        UserRepository userRepository = new UserRepository(space);
        ChatRepository chatRepository = new ChatRepository(space);

        String url = System.getProperty(CHAT_WEBSERVICE_URL_PROPERTY_NAME);

        if (url == null || url.trim().isEmpty()) {
            url = "http://localhost:9990";
        }

        System.out.println("Starting server");
        Endpoint.publish(url + "/users", new UserServiceImpl(userRepository));
        Endpoint.publish(url + "/chats", new ChatServiceImpl(chatRepository));
        System.out.println("Server started on: " + url);
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

    private static void configureSystemForJavaSpace() {
        System.setProperty(SECURITY_POLICY_PROPERTY_NAME, App.class.getClassLoader().getResource("policy.all").getPath());
        System.setSecurityManager(new SecurityManager());
    }
}
