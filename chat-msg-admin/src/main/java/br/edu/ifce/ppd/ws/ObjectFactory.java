
package br.edu.ifce.ppd.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.edu.ifce.ppd.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RetrieveChat_QNAME = new QName("http://service.chat.ppd.ifce.edu.br/", "retrieveChat");
    private final static QName _RetrieveChatResponse_QNAME = new QName("http://service.chat.ppd.ifce.edu.br/", "retrieveChatResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.edu.ifce.ppd.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetrieveChatResponse }
     * 
     */
    public RetrieveChatResponse createRetrieveChatResponse() {
        return new RetrieveChatResponse();
    }

    /**
     * Create an instance of {@link RetrieveChat }
     * 
     */
    public RetrieveChat createRetrieveChat() {
        return new RetrieveChat();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link Chat }
     * 
     */
    public Chat createChat() {
        return new Chat();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveChat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.chat.ppd.ifce.edu.br/", name = "retrieveChat")
    public JAXBElement<RetrieveChat> createRetrieveChat(RetrieveChat value) {
        return new JAXBElement<RetrieveChat>(_RetrieveChat_QNAME, RetrieveChat.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveChatResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.chat.ppd.ifce.edu.br/", name = "retrieveChatResponse")
    public JAXBElement<RetrieveChatResponse> createRetrieveChatResponse(RetrieveChatResponse value) {
        return new JAXBElement<RetrieveChatResponse>(_RetrieveChatResponse_QNAME, RetrieveChatResponse.class, null, value);
    }

}
