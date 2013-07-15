
package org.necros.portal.fragment.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FragmentsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FragmentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fragment" type="{http://portal.necros.org/fragments}FragmentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FragmentsType", namespace = "http://portal.necros.org/fragments", propOrder = {
    "fragment"
})
public class FragmentsType {

    @XmlElement(namespace = "http://portal.necros.org/fragments")
    protected List<FragmentType> fragment;

    /**
     * Gets the value of the fragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FragmentType }
     * 
     * 
     */
    public List<FragmentType> getFragment() {
        if (fragment == null) {
            fragment = new ArrayList<FragmentType>();
        }
        return this.fragment;
    }

}
