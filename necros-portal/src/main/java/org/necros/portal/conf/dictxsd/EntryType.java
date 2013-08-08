
package org.necros.portal.conf.dictxsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="categoryId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="displayText" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="displayOrder" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryType", namespace = "http://portal.necros.org/dictionary")
public class EntryType {

    @XmlAttribute(name = "categoryId")
    protected String categoryId;
    @XmlAttribute(name = "key")
    protected String key;
    @XmlAttribute(name = "displayText")
    protected String displayText;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "displayOrder")
    protected Integer displayOrder;

    /**
     * Gets the value of the categoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the value of the categoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryId(String value) {
        this.categoryId = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the displayText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Sets the value of the displayText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayText(String value) {
        this.displayText = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the displayOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the value of the displayOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDisplayOrder(Integer value) {
        this.displayOrder = value;
    }

}
