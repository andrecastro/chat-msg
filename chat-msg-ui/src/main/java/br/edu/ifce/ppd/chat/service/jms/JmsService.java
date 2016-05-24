package br.edu.ifce.ppd.chat.service.jms;

import org.exolab.jms.administration.JmsAdminServerIfc;
import javax.jms.ConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.Destination;
import java.io.Serializable;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * Created by andrecoelho on 5/22/16.
 */
public class JmsService {

    private JmsAdminServerIfc jmsAdminServer;
    private Connection connection;
    private Context context;

    public JmsService(JmsAdminServerIfc jmsAdminServer, Context context) throws JMSException, NamingException {
        this.jmsAdminServer = jmsAdminServer;
        this.context = context;
        this.connection = ((ConnectionFactory) context.lookup("ConnectionFactory")).createConnection();
    }

    public void close() throws JMSException {
        jmsAdminServer.close();
        connection.close();
    }

    public boolean createQueueIfNotExists(String queueName) throws JMSException {
        return destinationExists(queueName) || jmsAdminServer.addDestination(queueName, true);

    }

    public boolean createTopicIfNotExists(String queueName) throws JMSException {
        return destinationExists(queueName) || jmsAdminServer.addDestination(queueName, false);
    }

    public boolean destinationExists(String destinationName) throws JMSException {
        return jmsAdminServer.destinationExists(destinationName);
    }

    public void sendMessage(String destinationName, Serializable message) throws JMSException, NamingException {
        connection.start(); // just to ensure the connection is started

        Destination destination = getDestination(destinationName);
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageProducer sender = session.createProducer(destination);
        ObjectMessage objectMessage = session.createObjectMessage(message);
        sender.send(objectMessage);
        session.close();
    }

    public Serializable readMessage(String destinationName) throws JMSException, NamingException {
        Destination destination = getDestination(destinationName);
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageConsumer receiver = session.createConsumer(destination);
        connection.start();

        ObjectMessage objectMessage = (ObjectMessage) receiver.receiveNoWait();
        return objectMessage.getObject();
    }

    public Destination getDestination(String destinationName) throws NamingException {
        return (Destination) context.lookup(destinationName);
    }

    public int numberOfMessagesIn(String queueName) throws JMSException {
        return jmsAdminServer.getQueueMessageCount(queueName);
    }
}
