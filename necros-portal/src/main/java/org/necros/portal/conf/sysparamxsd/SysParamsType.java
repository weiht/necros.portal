
package org.necros.portal.conf.sysparamxsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SysParamsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SysParamsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sysParam" type="{http://portal.necros.org/sysparams}SysParamType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SysParamsType", namespace = "http://portal.necros.org/sysparams", propOrder = {
    "sysParam"
})
public class SysParamsType {

    @XmlElement(namespace = "http://portal.necros.org/sysparams")
    protected List<SysParamType> sysParam;

    /**
     * Gets the value of the sysParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sysParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSysParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SysParamType }
     * 
     * 
     */
    public List<SysParamType> getSysParam() {
        if (sysParam == null) {
            sysParam = new ArrayList<SysParamType>();
        }
        return this.sysParam;
    }

}
