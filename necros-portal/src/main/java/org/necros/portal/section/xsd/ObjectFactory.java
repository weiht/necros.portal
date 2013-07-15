
package org.necros.portal.section.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.necros.portal.section.xsd package. 
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

    private final static QName _Sections_QNAME = new QName("http://portal.necros.org/sections", "sections");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.necros.portal.section.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SectionsType }
     * 
     */
    public SectionsType createSectionsType() {
        return new SectionsType();
    }

    /**
     * Create an instance of {@link SectionType }
     * 
     */
    public SectionType createSectionType() {
        return new SectionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SectionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.necros.org/sections", name = "sections")
    public JAXBElement<SectionsType> createSections(SectionsType value) {
        return new JAXBElement<SectionsType>(_Sections_QNAME, SectionsType.class, null, value);
    }

}
