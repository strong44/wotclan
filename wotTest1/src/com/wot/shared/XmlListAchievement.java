//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.06 at 01:20:04 PM CEST 
//


package com.wot.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XmlListAchievement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XmlListAchievement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SRC_IMG" type="{http://wotachievement.appspot.com/}XmlListSrcImg"/>
 *         &lt;element name="DESCRIPTION" type="{http://wotachievement.appspot.com/}XmlDescription"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XmlListAchievement", propOrder = {
    "srcimg",
    "description"
})
public class XmlListAchievement {

    @XmlElement(name = "SRC_IMG", required = true)
    protected XmlListSrcImg srcimg;
    @XmlElement(name = "DESCRIPTION", required = true)
    protected XmlDescription description;
    @XmlAttribute(name = "NAME", required = true)
    protected String name;

    /**
     * Gets the value of the srcimg property.
     * 
     * @return
     *     possible object is
     *     {@link XmlListSrcImg }
     *     
     */
    public XmlListSrcImg getSRCIMG() {
        return srcimg;
    }

    /**
     * Sets the value of the srcimg property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlListSrcImg }
     *     
     */
    public void setSRCIMG(XmlListSrcImg value) {
        this.srcimg = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link XmlDescription }
     *     
     */
    public XmlDescription getDESCRIPTION() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlDescription }
     *     
     */
    public void setDESCRIPTION(XmlDescription value) {
        this.description = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAME() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAME(String value) {
        this.name = value;
    }

}
