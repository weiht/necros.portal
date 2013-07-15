
package org.necros.portal.fragment.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.necros.portal.fragment.xsd package. 
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

    private final static QName _Fragments_QNAME = new QName("http://portal.necros.org/fragments", "fragments");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.necros.portal.fragment.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FragmentsType }
     * 
     */
    public FragmentsType createFragmentsType() {
        return new FragmentsType();
    }

    /**
     * Create an instance of {@link FragmentType }
     * 
     */
    public FragmentType createFragmentType() {
        return new FragmentType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FragmentsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.necros.org/fragments", name = "fragments")
    public JAXBElement<FragmentsType> createFragments(FragmentsType value) {
        return new JAXBElement<FragmentsType>(_Fragments_QNAME, FragmentsType.class, null, value);
    }

}
