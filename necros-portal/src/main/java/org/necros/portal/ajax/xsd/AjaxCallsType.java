
package org.necros.portal.ajax.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AjaxCallsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AjaxCallsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ajaxCall" type="{http://portal.necros.org/ajaxcalls}AjaxCallType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AjaxCallsType", namespace = "http://portal.necros.org/ajaxcalls", propOrder = {
    "ajaxCall"
})
public class AjaxCallsType {

    @XmlElement(namespace = "http://portal.necros.org/ajaxcalls")
    protected List<AjaxCallType> ajaxCall;

    /**
     * Gets the value of the ajaxCall property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ajaxCall property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAjaxCall().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AjaxCallType }
     * 
     * 
     */
    public List<AjaxCallType> getAjaxCall() {
        if (ajaxCall == null) {
            ajaxCall = new ArrayList<AjaxCallType>();
        }
        return this.ajaxCall;
    }

}
