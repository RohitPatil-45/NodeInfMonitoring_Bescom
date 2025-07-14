/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

public class OIDHolder {
    // -----------------------------------------
    // -----------------------

    public static enum SNMPDataType {

        STRING, INT, LONG, FLOAT, TICKS, OCTIN, OCTOUT, COUNT, OTHER
    };

    public static String toString(SNMPDataType val) {
        return val.toString();
    }

    public static SNMPDataType toSNMPDataResponseType(String val) {
        SNMPDataType retVal = SNMPDataType.STRING;
        if ("INT".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.INT;
        } else if ("LONG".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.LONG;
        } else if ("FLOAT".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.FLOAT;
        } else if ("TICKS".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.TICKS;
        } else if ("OCTIN".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.OCTIN;
        } else if ("OCTOUT".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.OCTOUT;
        } else if ("COUNT".equalsIgnoreCase(val)) {
            retVal = SNMPDataType.COUNT;
        } else {
            System.out.println("Invalid SNMPDataResponseType[" + val + "], defaulting to String");
        }
        return retVal;
    }
    // -----------------------------------------
    public String name;
    public String oidString;
    public OIDHolder descriptionHolder = null;
    public OID oid;
    public String description;
    public String resolvedDescription;
    public boolean subIFNeeded = false;
    public int vectorIndex = -1;
    public SNMPDataType dataType;
    protected int mpyFactor = 1;
    protected int divFactor = 1;
    // -------------------------------

    public int getMpyFactor() {
        return mpyFactor;
    }

    public void setMpyFactor(int mpyFactor) {
        this.mpyFactor = mpyFactor;
    }

    // -------------------------------
    public int getDivFactor() {
        return divFactor;
    }

    public void setDivFactor(int divFactor) {
        this.divFactor = divFactor;
    }
    // ------------------------------------------

    public SNMPDataType getDataType() {
        return dataType;
    }
    // ----------------------------------

    public void setDataType(SNMPDataType dataType) {
        this.dataType = dataType;
    }

    // ------------------------------------------
    public boolean isSubIFNeeded() {
        return subIFNeeded;
    }
    // ------------------------------------------

    public void setSubIFNeeded(boolean subIFNeeded) {
        this.subIFNeeded = subIFNeeded;
    }
    // ------------------------------------------

    public String getName() {
        return name;
    }
    // ----------------------------------

    public void setName(String name) {
        this.name = name;
        setSubIFNeeded(isSubIFNeeded() || name.contains("%"));
    }
    // ----------------------------------

    public String getOidString() {
        return oidString;
    }
    // ----------------------------------

    public void setOidString(String oidString) {
        this.oidString = oidString;
    }
    // ----------------------------------

    public OID getOid() {
        return oid;
    }
    // ----------------------------------

    public void setOid(OID oid) {
        this.oid = oid;
    }
    // ----------------------------------

    public OIDHolder getDescriptionHolder() {
        return descriptionHolder;
    }
    // ----------------------------------

    public void setDescriptionHolder(OIDHolder descriptionHolder) {
        this.descriptionHolder = descriptionHolder;
    }
    // ----------------------------------

    public String getDescription() {
        return description;
    }
    // ----------------------------------

    public void setDescription(String description) {
        this.description = description;
        setSubIFNeeded(isSubIFNeeded() || description.contains("%"));
    }
    // ------------------------------------------

    public String getResolvedDescription() {
        return resolvedDescription;
    }
    // ------------------------------------------

    public void setResolvedDescription(String resolvedDescription) {
        this.resolvedDescription = resolvedDescription;
    }
    // ------------------------------------------

    public int getVectorIndex() {
        return vectorIndex;
    }
    // ------------------------------------------

    public void setVectorIndex(int vectorIndex) {
        this.vectorIndex = vectorIndex;
    }
    // ------------------------------------------

    public OIDHolder(SNMPDataType type, String name, String oidString, String description) {
        setDataType(type);
        setName(name);
        setOidString(oidString);
        setOid(new OID(oidString));
        setDescription(description);
    }
    // ------------------------------------------

    public OIDHolder(SNMPDataType type, String name, OID oid, String description) {
        setDataType(type);
        setName(name);
        setOid(oid);
        setOidString(oid.toString());
        setDescription(description);
    }

    // ------------------------------------------
    public String toString() {
        StringBuilder retVal = new StringBuilder(name);
        retVal.append("[").append(oid);
        retVal.append('\t').append(oid).append("]");
        return retVal.toString();
    }

    // ------------------------------------------
    public VariableBinding getVariableBinding() {
        return new VariableBinding(oid);
    }
}
