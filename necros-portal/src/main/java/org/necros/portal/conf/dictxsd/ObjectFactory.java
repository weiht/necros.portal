
package org.necros.portal.conf.dictxsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.necros.portal.conf.dictxsd package. 
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

    private final static QName _Dictionary_QNAME = new QName("http://portal.necros.org/dictionary", "Dictionary");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.necros.portal.conf.dictxsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DictionaryType }
     * 
     */
    public DictionaryType createDictionaryType() {
        return new DictionaryType();
    }

    /**
     * Create an instance of {@link EntryType }
     * 
     */
    public EntryType createEntryType() {
        return new EntryType();
    }

    /**
     * Create an instance of {@link CategoryType }
     * 
     */
    public CategoryType createCategoryType() {
        return new CategoryType();
    }

    /**
     * Create an instance of {@link EntriesType }
     * 
     */
    public EntriesType createEntriesType() {
        return new EntriesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DictionaryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.necros.org/dictionary", name = "Dictionary")
    public JAXBElement<DictionaryType> createDictionary(DictionaryType value) {
        return new JAXBElement<DictionaryType>(_Dictionary_QNAME, DictionaryType.class, null, value);
    }

}
