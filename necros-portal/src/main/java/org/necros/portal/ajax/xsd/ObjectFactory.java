
package org.necros.portal.ajax.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.necros.portal.ajax.xsd package. 
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

    private final static QName _AjaxCalls_QNAME = new QName("http://portal.necros.org/ajaxcalls", "ajaxCalls");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.necros.portal.ajax.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AjaxCallsType }
     * 
     */
    public AjaxCallsType createAjaxCallsType() {
        return new AjaxCallsType();
    }

    /**
     * Create an instance of {@link AjaxCallType }
     * 
     */
    public AjaxCallType createAjaxCallType() {
        return new AjaxCallType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjaxCallsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.necros.org/ajaxcalls", name = "ajaxCalls")
    public JAXBElement<AjaxCallsType> createAjaxCalls(AjaxCallsType value) {
        return new JAXBElement<AjaxCallsType>(_AjaxCalls_QNAME, AjaxCallsType.class, null, value);
    }

}
