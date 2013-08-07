
package org.necros.portal.conf.sysparamxsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.necros.portal.conf.xsd package. 
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

    private final static QName _SysParams_QNAME = new QName("http://portal.necros.org/sysparams", "sysParams");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.necros.portal.conf.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SysParamsType }
     * 
     */
    public SysParamsType createSysParamsType() {
        return new SysParamsType();
    }

    /**
     * Create an instance of {@link SysParamType }
     * 
     */
    public SysParamType createSysParamType() {
        return new SysParamType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SysParamsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.necros.org/sysparams", name = "sysParams")
    public JAXBElement<SysParamsType> createSysParams(SysParamsType value) {
        return new JAXBElement<SysParamsType>(_SysParams_QNAME, SysParamsType.class, null, value);
    }

}
