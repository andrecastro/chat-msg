
package br.edu.ifce.ppd.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="user2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="history" type="{http://service.chat.ppd.ifce.edu.br/}message" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chat", propOrder = {
    "user1",
    "user2",
    "history"
})
public class Chat {

    protected String user1;
    protected String user2;
    @XmlElement(nillable = true)
    protected List<Message> history;

    /**
     * Gets the value of the user1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser1() {
        return user1;
    }

    /**
     * Sets the value of the user1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser1(String value) {
        this.user1 = value;
    }

    /**
     * Gets the value of the user2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser2() {
        return user2;
    }

    /**
     * Sets the value of the user2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser2(String value) {
        this.user2 = value;
    }

    /**
     * Gets the value of the history property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the history property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Message }
     * 
     * 
     */
    public List<Message> getHistory() {
        if (history == null) {
            history = new ArrayList<Message>();
        }
        return this.history;
    }

}
