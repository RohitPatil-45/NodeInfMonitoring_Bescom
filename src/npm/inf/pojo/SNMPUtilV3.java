/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import org.snmp4j.ScopedPDU;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.AuthHMAC192SHA256;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;

/**
 *
 * @author NPM
 */
public class SNMPUtilV3 {

    public enum SNMPCounterType {

        Counter32bit, Counter64bit
    };
    public final String STARTING_OID = "1.3.6.1.2.1.2.1.0";
    private TransportMapping<?> transport = null;
    private Snmp snmp = null;
    private UdpAddress localUDPAddress = null;

    public UdpAddress getUdpAddress() {
        return localUDPAddress;
    }

    public void setUdpAddress(UdpAddress udpaddr) {
        localUDPAddress = udpaddr;
    }

    /**
     * Source and return address of the form "<ip>/<port>"
     */
    public void setUdpAddress(String udpaddr) {
        setUdpAddress(new UdpAddress(udpaddr));
    }
    // -----------------------------------------------------
    // 1.3.6.1.2.1.1.1.0	Value: Linux TS-870 4.1.2
    public final String SYSNAME_ID = "hostName";
    public final String SYSNAME_OID = "1.3.6.1.4.1.9.2.1.3.0";
    public final String SYSNAME_STRING = "Host Name";
    public final String IFnNAME_ID = "if[%d] ";
    public final String IFnNAME_OID = "1.3.6.1.2.1.31.1.1.1.1";
    public final String IFnNAME_STRING = "if[%d] ";
    public final String IFnMAC_ID = "ifMAC";
    public final String IFnMAC_OID = "3.6.1.2.1.2.2.1.6";
    public final String IFnMAC_STRING = "if[%d] MAC Address";
    public final String IFnDESCR_ID = "Descr";
//	public  final String	IFnDESCR_OID 	= "1.3.6.1.2.1.2.2.1.2";
    public final String IFnDESCR_OID = "1.3.6.1.4.1.9.2.2.1.1.28"; // Cisco
    public final String IFnDESCR_STRING = "description";
//	public  final String	IFnDESCR_OID 	= "1.3.6.1.2.1.2.2.1.2";
//	public  final String	IFnDESCR_STRING	= "if[%d]";
    // -----------------------------------------------------
//	public  final String	UPTIME_ID 		= "sysORDUpTime";
//	public  final String	UPTIME_OID 		= "1.3.6.1.2.1.1.9.1.4";
    public final String UPTIME_IDX = "sysUpTime";
    public final String UPTIME_OIDX = "1.3.6.1.2.1.1.3.0";
    public final String UPTIME_STRINGX = "System Uptime";
    public final String UPTIME_ID = "hrSystemUptime.0";
    public final String UPTIME_OID = "1.3.6.1.2.1.25.1.1.0";
    public final String UPTIME_STRING = "System Uptime";
    public final String IFHCINOCT_OID = "1.3.6.1.2.1.31.1.1.1.6";
    public final String IFHCINOCT_ID = "HCInOctets";
    public final String IFHCINOCT_STRING = "HC Octets In";
    public final String IFINOCT_OID = "1.3.6.1.2.1.2.2.1.10";
    public final String IFINOCT_ID = "InOctets";
    public final String IFINOCT_STRING = "Octets In";
    public final String IFHCOUTOCT_OID = "1.3.6.1.2.1.31.1.1.1.10";
    public final String IFHCOUTOCT_ID = "OutOctets";
    public final String IFHCOUTOCT_STRING = "HC Octets Out";
    public final String IFOUTOCT_OID = "1.3.6.1.2.1.2.2.1.16";
    public final String IFOUTOCT_ID = "OutOctets";
    public final String IFOUTOCT_STRING = "Octets Out";
    public final String IFERRORS_ID = "InErrors";
    public final String IFERRORS_OID = "1.3.6.1.2.1.2.2.1.14";
    public final String IFERRORS_STRING = "Error Packets";
    public String[] bandwidth = new String[2];
    // --------------------------------------------------------------------

    public void stop() {
        try {
            transport.close();
        } catch (Exception e) {
        }
        try {
            transport.removeTransportListener(null);
        } catch (Exception e) {
        }
        try {
            snmp.close();
        } catch (Exception e) {
        }

    }

    public OID makeOID(String oidstr, int index) {
        OID retVal = new OID(oidstr);
        if (index > 0) {
            retVal.append(index);
        }
        return retVal;
    }
    // ------------------------------------------

    public OIDHolder makeOIDHolder(OIDHolder.SNMPDataType type, String oidstr, String id, String desc, int index) {
        OIDHolder retVal = null;
        OID oid = new OID(oidstr);
        String nametmp = id;
        if (index > -1) {
            oid.append(index);
            if (nametmp.contains("%")) {
                nametmp = String.format(id, index);
            }
            if (desc.contains("%")) {
                desc = String.format(desc, index);
            }
        }
        retVal = new OIDHolder(type, nametmp, oid, desc);
        return retVal;
    }
    // ------------------------------------------

    public OIDHolder makeOIDHolder(OIDHolder.SNMPDataType type, String oidstr, String id, String desc) {
        return makeOIDHolder(type, oidstr, id, desc, -1);
    }
    // ------------------------------------------
    // ------------------------------------------
    private Map<String, OIDHolder> oidMap;
    // ------------------------------------------

    protected boolean addEntry(OIDHolder entry) {
        oidMap.put(entry.getName(), entry);
        oidMap.put(entry.getOidString(), entry);
        return true;
    }
    // ------------------------------------------

    protected OIDHolder addEntry(String key) {
        OIDHolder retVal = null;
        if (key != null) {
            retVal = oidMap.get(key);
        }
        return retVal;
    }
    // ------------------------------------------

    public OIDHolder getOIDHolder(String key) {
        OIDHolder retVal = null;
        if (key != null) {
            retVal = oidMap.get(key);
        }
        return retVal;
    }

    // ------------------------------------------
    public String getStringVar(Variable var) {
        String retVal = null;
        if (!(var instanceof Null)) {
            retVal = var.toString();
        }
        return retVal;
    }
    // ------------------------------------------

    public Variable getVariableBinding(PDU pdu, OID varoid) {
        Variable retVal = null;
        if (pdu != null && varoid != null) {
            retVal = pdu.getVariable(varoid);
        }
        return retVal;
    }
    // ------------------------------------------

    public String getVariableValue(PDU pdu, OID varoid) {
        String retVal = null;
        Variable var = getVariableBinding(pdu, varoid);
        if (var != null) {
            retVal = var.toString();
        }
        return retVal;
    }
    // ------------------------------------------

    public Variable getVariableBinding(PDU pdu, String varname) {
        Variable retVal = null;
        OIDHolder oidh = getOIDHolder(varname);
        if (pdu != null) {
            if (oidh != null) {
                retVal = getVariableBinding(pdu, oidh.getOid());
            } else {
                retVal = getVariableBinding(pdu, new OID(varname));
            }
        }
        return retVal;
    }
    // ------------------------------------------

    public String getVariableValue(PDU pdu, String varname) {
        String retVal = null;
        Variable var = getVariableBinding(pdu, varname);
        if (var != null) {
            retVal = var.toString();
        }
        return retVal;
    }

    // ------------------------------------------
    // ------------------------------------------
    {
        oidMap = new LinkedHashMap<String, OIDHolder>();
//		addEntry(getOIDHolder(String oidstr, String id, String desc, int index));
        // -------------------------------------------------------------------
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.STRING, SYSNAME_OID, SYSNAME_ID, SYSNAME_STRING));
        OIDHolder tmph = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID, IFnDESCR_ID, IFnDESCR_STRING);
        addEntry(tmph);
        tmph.setDescriptionHolder(makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, IFnNAME_ID, IFnNAME_STRING));
        addEntry(tmph.getDescriptionHolder());

        // -------------------------------------------------------------------
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.TICKS, UPTIME_OID, UPTIME_ID, UPTIME_STRING));
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFINOCT_OID, IFINOCT_ID, IFINOCT_STRING));
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFHCINOCT_OID, IFHCINOCT_ID, IFHCINOCT_STRING));
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID, IFOUTOCT_ID, IFOUTOCT_STRING));
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFHCOUTOCT_OID, IFHCOUTOCT_ID, IFHCOUTOCT_STRING));
        addEntry(makeOIDHolder(OIDHolder.SNMPDataType.COUNT, IFERRORS_OID, IFERRORS_ID, IFERRORS_STRING));
        // -------------------------------------------------------------------
    }
    // -----------------------------------------------------

    public List<OIDHolder> getSYSOIDHolder(List<OIDHolder> retVal) {
        retVal.add(getOIDHolder(UPTIME_OID));
        return retVal;
    }
    // -----------------------------------------------------

    public OIDHolder getIFOIDHolder(int index, SNMPCounterType type) {
        OIDHolder retVal = null;
        OIDHolder meta = null;

        switch (type) {
            case Counter32bit:
                retVal = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, IFnNAME_ID, IFnNAME_STRING, index);
                meta = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID, IFnDESCR_ID, IFnDESCR_STRING, index);
                break;
            case Counter64bit:
                retVal = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, IFnNAME_ID, IFnNAME_STRING, index);
                meta = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID, IFnDESCR_ID, IFnDESCR_STRING, index);
                break;
        }
        retVal.setDescriptionHolder(meta);
        return retVal;
    }
    // -----------------------------------------------------

    public List<OIDHolder> getIFOIDHolder(List<OIDHolder> retVal, int index) {
        retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFINOCT_OID, IFINOCT_ID, IFINOCT_STRING, index));
        retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFHCINOCT_OID, IFHCINOCT_ID, IFHCINOCT_STRING, index));
        retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID, IFOUTOCT_ID, IFOUTOCT_STRING, index));
        retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFHCOUTOCT_OID, IFHCOUTOCT_ID, IFHCOUTOCT_STRING, index));
        retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.COUNT, IFERRORS_OID, IFERRORS_ID, IFERRORS_STRING, index));
        return retVal;
    }

    // -----------------------------------------------------
    public List<OIDHolder> getIFOIDHolderInst(List<OIDHolder> retVal, int index, SNMPCounterType type, boolean inoutonly) {
        if (!inoutonly) {
            retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFINOCT_OID, IFINOCT_ID, IFINOCT_STRING, index));
            retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID, IFOUTOCT_ID, IFOUTOCT_STRING, index));
            retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.COUNT, IFERRORS_OID, IFERRORS_ID, IFERRORS_STRING, index));
        }

        switch (type) {
            case Counter32bit:
                retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFINOCT_OID, IFINOCT_ID, IFINOCT_STRING, index));
                retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID, IFOUTOCT_ID, IFOUTOCT_STRING, index));
                break;
            case Counter64bit:
                retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, IFHCINOCT_OID, IFHCINOCT_ID, IFHCINOCT_STRING, index));
                retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFHCOUTOCT_OID, IFHCOUTOCT_ID, IFHCOUTOCT_STRING, index));
                break;
        }

        return retVal;
    }

    // ------------------------------------------
    public VariableBinding getOIDVar(String oidstr, String name, int index) {
        VariableBinding retVal = null;
        OID oid = new OID(oidstr);
        String nametmp = name;
        if (name.contains("%")) {
            oid.append(index);
            nametmp = String.format(name, index);
        }
        retVal = new VariableBinding(oid);
        return retVal;
    }
    // -----------------------------------------------------

    public PDU getMetaInfoOIDs(PDU retVal, int index) {
        retVal.add(getOIDVar(SYSNAME_OID, SYSNAME_STRING, index));
        retVal.add(getOIDVar(IFnNAME_OID, IFnNAME_STRING, index));
        retVal.add(getOIDVar(IFnDESCR_OID, IFnDESCR_STRING, index));
        return retVal;
    }
    // -----------------------------------------------------

    public PDU getMetaInfoOIDs(int index) {
        PDU retVal = new PDU();
        return getMetaInfoOIDs(retVal, index);
    }
    // -----------------------------------------------------

    public PDU getIFInfoOIDs(PDU retVal, int index) {
        retVal.add(getOIDVar(UPTIME_OID, UPTIME_STRING, index));
        retVal.add(getOIDVar(IFINOCT_OID, IFINOCT_STRING, index));
        retVal.add(getOIDVar(IFHCINOCT_OID, IFINOCT_STRING, index));
        retVal.add(getOIDVar(IFOUTOCT_OID, IFOUTOCT_STRING, index));
        retVal.add(getOIDVar(IFHCOUTOCT_OID, IFHCOUTOCT_STRING, index));
        retVal.add(getOIDVar(IFERRORS_OID, IFERRORS_STRING, index));
        return retVal;
    }
    // -----------------------------------------------------

    public List<VariableBinding> getIFInfoOIDs(List<VariableBinding> retVal, int index) {
        retVal.add(getOIDVar(UPTIME_OID, UPTIME_STRING, index));
        retVal.add(getOIDVar(IFINOCT_OID, IFINOCT_STRING, index));
        retVal.add(getOIDVar(IFHCINOCT_OID, IFINOCT_STRING, index));
        retVal.add(getOIDVar(IFOUTOCT_OID, IFOUTOCT_STRING, index));
        retVal.add(getOIDVar(IFHCOUTOCT_OID, IFHCOUTOCT_STRING, index));
        retVal.add(getOIDVar(IFERRORS_OID, IFERRORS_STRING, index));
        return retVal;
    }
    // -----------------------------------------------------

    public PDU getIFInfoOIDs(int index) {
        PDU retVal = new ScopedPDU();
        return getIFInfoOIDs(retVal, index);
    }
    // -----------------------------------------------------
    //

    public boolean isListning() {
        return transport != null;
    }

    protected TransportMapping<?> getTransportMapping()
            throws IOException {
        TransportMapping<?> retVal = transport;
        if (retVal == null) {
            try {
                if (getUdpAddress() != null) {
                    retVal = new DefaultUdpTransportMapping(getUdpAddress());
                } else {
                    retVal = new DefaultUdpTransportMapping();
                }
            } catch (IOException e) {
                System.out.println("SNMPUtil.getTransportMapping(): Failed to create Transport Mapping " + getUdpAddress());
                // throw e;
            }
            transport = retVal;
        }
        return retVal;
    }
    // ---------------------------------------------------------
//	public 

    public void startSNMPListen() {
        if (!isListning()) {
            try {
                transport = getTransportMapping();
                snmp = new Snmp(transport);
                // Do not forget this line!
                transport.listen();
            } catch (IOException e) {
                System.out.println("SNMPUtil.startSNMPListen(): Failed to create Transport Mapping " + getUdpAddress());
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }
        }
    }
    // -----------------------------------------------------

    public void stopSNMPListen() {
        if (isListning()) {
            try {
                snmp.close();
                transport.close();
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    System.out.println("Error:" + ex);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("SNMP Closed exception" + e);

            }
            snmp = null;
            transport = null;
        }
    }

    // -----------------------------------------------------
    public boolean init() {
        startSNMPListen();
        return isListning();
    }
    // -----------------------------------------------------

    public int getVersion(String versionstring) {
        int retVal = SnmpConstants.version2c;
        if (versionstring != null) {
            versionstring = versionstring.trim();
            if ("1".equals(versionstring)) {
                retVal = SnmpConstants.version1;
            } else if ("3".equals(versionstring)) {
                retVal = SnmpConstants.version3;
            }
        }
        return retVal;
    }
    // -----------------------------------------------------

    public Target getTarget(String address, String community, int version) {

        CommunityTarget target = null;
        try {

            Address targetAddress = GenericAddress.parse(address);
            target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(17000);
            target.setVersion(version);

        } catch (Exception ee) {
            System.out.println("security exception:" + ee);
        }

        return target;
    }

    public void start(String user, String auth, String authpass, String priv, String privpass) {

        try {
            stopSNMPListen();
        } catch (Exception e2) {
            System.out.println("stopSNMPListen Exception:" + e2);
        }

        try {

            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);

            USM usm = null;
            usm = new USM(SecurityProtocols.getInstance(), new OctetString(
                    MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            transport.listen();

            if (auth.equals("SHA") && (priv == null || priv.equals("NA"))) {
                snmp.getUSM().addUser(
                        new OctetString("SHADES"),
                        new UsmUser(new OctetString(user), AuthSHA.ID, new OctetString(authpass), null, null));

            } else if (auth.equals("MD5") && (priv == null || priv.equals("NA"))) {
                snmp.getUSM().addUser(
                        new OctetString("MD5DES"),
                        new UsmUser(new OctetString(user), AuthMD5.ID, new OctetString(authpass), null, null));

            } else if (auth.equals("MD5") && priv.equals("DES")) {
                snmp.getUSM().addUser(
                        new OctetString("MD5DES"),
                        new UsmUser(new OctetString(user), AuthMD5.ID, new OctetString(authpass), PrivDES.ID, new OctetString(privpass)));
            } else if (auth.equals("SHA") && priv.equals("DES")) {
                snmp.getUSM().addUser(
                        new OctetString("SHADES"),
                        new UsmUser(new OctetString(user), AuthSHA.ID, new OctetString(authpass), PrivDES.ID, new OctetString(privpass)));

            } else if (auth.equals("MD5") && priv.equals("3DES")) {
                snmp.getUSM().addUser(
                        new OctetString("MD53DES"),
                        new UsmUser(new OctetString(user), AuthMD5.ID, new OctetString(authpass), Priv3DES.ID, new OctetString(privpass)));
            } else if (auth.equals("SHA") && priv.equals("3DES")) {
                snmp.getUSM().addUser(
                        new OctetString("SHA3DES"),
                        new UsmUser(new OctetString(user), AuthSHA.ID, new OctetString(authpass), Priv3DES.ID, new OctetString(privpass)));

            } else if (auth.equals("SHA256") && priv.equals("AES256")) {
                snmp.getUSM().addUser(
                        new OctetString("SHAAES"),
                        new UsmUser(new OctetString(user), AuthHMAC192SHA256.ID, new OctetString(authpass), PrivAES256.ID, new OctetString(privpass)));

            } else if (auth.equals("NA") && priv.equals("NA")) {
                snmp.getUSM().addUser(
                        new OctetString(),
                        new UsmUser(new OctetString(user), null, null, null, null));
            }

        } catch (Exception e) {
            System.out.println(isListning() + "start method error Exception" + e);
        }
    }

    public Target getTarget(String protocol, String host, String port, String community, int version) {
        StringBuilder targetaddress = new StringBuilder();
        targetaddress.append((protocol != null && !protocol.isEmpty() ? protocol : "udp"));
        targetaddress.append(":");
        targetaddress.append(host);
        targetaddress.append("/");
        targetaddress.append(port);
        return getTarget(targetaddress.toString(), community, version);
    }
    // -----------------------------------------------------

    public ResponseEvent get(Target target, PDU pdu) throws IOException {
        ResponseEvent retVal = null;
        if (pdu != null) {
            pdu.setType(PDU.GET);
            retVal = snmp.send(pdu, target, null);
            if (retVal == null) {
                throw new RuntimeException("GET timed out");
            }
        }
        return retVal;
    }
    // -----------------------------------------------------

    public ResponseEvent getNoThrow(Target target, PDU pdu) {
        ResponseEvent retVal = null;
        try {
            retVal = get(target, pdu);
        } catch (Exception e) {
        }
        return retVal;
    }
    // -----------------------------------------------------

    public ResponseEvent get(Target target, OID oid) throws IOException {
        ResponseEvent retVal = null;
        PDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(oid));
        return get(target, pdu);
    }

    // -----------------------------------------------------
    public List<VariableBinding> walk(Target target, OID oid) {
        List<VariableBinding> ret = new ArrayList<VariableBinding>();

        PDU requestPDU = new ScopedPDU();
        requestPDU.add(new VariableBinding(oid));
        requestPDU.setType(PDU.GETNEXT);
        boolean finished = false;
        try {
            while (!finished) {
                VariableBinding vb = null;

                ResponseEvent respEvt = snmp.send(requestPDU, target);
                PDU responsePDU = respEvt.getResponse();
                if (responsePDU != null) {
                    vb = responsePDU.get(0);
                }

                if (responsePDU == null) {
                    finished = true;
                } else if (responsePDU.getErrorStatus() != 0) {
                    finished = true;
                } else if (vb.getOid() == null) {
                    finished = true;
                } else if (vb.getOid().size() < oid.size()) {
                    finished = true;
                } else if (oid.leftMostCompare(oid.size(), vb.getOid()) != 0) {
                    finished = true;
                } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
                    finished = true;
                } else if (vb.getOid().compareTo(oid) <= 0) {
                    finished = true;
                } else {
                    ret.add(vb);

                    // Set up the variable binding for the next entry.
                    requestPDU.setRequestID(new Integer32(0));
                    requestPDU.set(0, vb);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Exception:" + e);
        }
        return ret;
    }
    // -----------------------------------------------------

    // -----------------------------------------------------
    // -----------------------------------------------------
    public OIDHolder.SNMPDataType getDataType(Variable var) {
        OIDHolder.SNMPDataType retVal = OIDHolder.SNMPDataType.INT;
        /*
         * org.snmp4j.smi.Variable implementations AbstractVariable, BitString,
         * Counter32, Counter64, Gauge32, Integer32, UnsignedInteger32,
         * TimeTicks, OctetString, GenericAddress, IpAddress, Null, OID, Opaque,
         * SMIAddress, SshAddress, TcpAddress, TlsAddress, TransportIpAddress,
         * TsmSecurityParameters, UdpAddress, VariantVariable SNMPDataType:
         * STRING,INT,LONG,FLOAT,TICKS,OCTIN,OCTOUT,COUNT, OTHER
         */
        if (var instanceof TimeTicks) {
            retVal = OIDHolder.SNMPDataType.TICKS;
        } else if (var instanceof Counter64 || var instanceof Counter32) {
            retVal = OIDHolder.SNMPDataType.COUNT;
        } else if (var instanceof Integer32 || var instanceof UnsignedInteger32) {
            retVal = OIDHolder.SNMPDataType.COUNT;
        } else if (var instanceof Gauge32) {
            retVal = OIDHolder.SNMPDataType.COUNT;
        } else if (var instanceof OctetString) {
            retVal = OIDHolder.SNMPDataType.STRING;
        }
        return retVal;
    }
    // -----------------------------------------------------

    public Map<OID, Variable> convertToMap(PDU pdu) {
        Map<OID, Variable> retVal = null;
        if (pdu != null) {
            retVal = new HashMap<OID, Variable>();
            Vector<? extends VariableBinding> vect = pdu.getVariableBindings();
            for (VariableBinding curr : vect) {
                retVal.put(curr.getOid(), curr.getVariable());
            }
        }
        return retVal;
    }
    // -----------------------------------------------------

    public String dump(PDU pdu) {
        if (pdu != null) {
            return dump(pdu.getVariableBindings());
        } else {
            return null;
        }
    }
    // -----------------------------------------------------

    public String dump(ResponseEvent resp) {
        ((Snmp) resp.getSource()).cancel(resp.getRequest(), null);
        return dump(resp.getResponse());
    }

    public String dump(List<? extends VariableBinding> list) {

        int idx = 0;
        String rst = null;
        try {
            if (list.size() == 2) {
                int i = 0;
                for (VariableBinding var : list) {
                    rst = var.getVariable().toString();
                    i++;
                }
            } else {
                for (VariableBinding var : list) {
                    rst = var.getVariable().toString();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
        return rst;
    }

    // -----------------------------------------------------
    public String BandwidthGetVect(Target target, String oidStr, String i) {
        ResponseEvent retVal = null;

        OID oids[] = {new OID("1.3.6.1.2.1.2.2.1.10" + "." + i), new OID("1.3.6.1.2.1.2.2.1.16" + "." + i), new OID("1.3.6.1.2.1.2.2.1.7" + "." + i), new OID("1.3.6.1.2.1.2.2.1.8" + "." + i), new OID("1.3.6.1.4.1.9.2.2.1.1.12" + "." + i), new OID("1.3.6.1.4.1.9.9.276.1.1.1.1.10" + "." + i), new OID("1.3.6.1.4.1.9.9.276.1.1.1.1.11" + "." + i), new OID("1.3.6.1.4.1.9.2.2.1.1.22" + "." + i), new OID("")};

        try {
            int j;
            if (oidStr.contentEquals("In")) {
                j = 0;
            } else if (oidStr.contentEquals("Out")) {
                j = 1;
            } else if (oidStr.contentEquals("ifadmin")) {
                j = 2;
            } else if (oidStr.contentEquals("ifopera")) {
                j = 3;
            } else if (oidStr.contentEquals("crc")) {
                j = 4;
            } else if (oidStr.contentEquals("inputdrop")) {
                j = 5;
            } else if (oidStr.contentEquals("outputdrop")) {
                j = 6;
            } else if (oidStr.contentEquals("reliability")) {
                j = 7;
            } else {
                return null;
            }

            retVal = get(target, oids[j]);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(target + ":Exception:" + e);
        }
        return dump(retVal);
    }

    // -----------------------------------------------------
    public String getOIDValue(Target target, OID oid) {
        ResponseEvent retVal = null;
        try {
            retVal = get(target, oid);
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return dump(retVal);
    }

    public ResponseEvent testGetMeta(Target target, int index) {
        ResponseEvent retVal = null;
        PDU pdu = getMetaInfoOIDs(index);
        try {
            retVal = get(target, pdu);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Exception:" + e);
        }
        dump(retVal);
        return retVal;
    }
    // -----------------------------------------------------

    public ResponseEvent testGetVect(Target target, int index) {
        ResponseEvent retVal = null;
        PDU pdu = getIFInfoOIDs(index);
        try {
            retVal = get(target, pdu);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Exception:" + e);
        }
        dump(retVal);
        return retVal;
    }
    // -----------------------------------------------------

    public void testWalk(Target target) {
        List<? extends VariableBinding> ret = walk(target, new OID("1.3.6.1.2.1.2.2.1.10"));
        dump(ret);
    }

    public void getInoctet(Target target) {
        List<? extends VariableBinding> ret = walk(target, new OID("1.3.6.1.2.1.2.2.1.10"));
        dump(ret);
    }

    public Target getTargetVersion3(String address, String user_name, String auth, String auth_pass, String privacy, String priv_pass, int version) {

        UserTarget target = null;
        try {

            Address targetAddress = GenericAddress.parse(address);
            target = new UserTarget();
            target.setAddress(targetAddress);
            target.setRetries(1);
            target.setTimeout(11500);
            target.setVersion(SnmpConstants.version3);

            if ((auth == null || auth.equals("NA")) && (privacy == null || privacy.equals("NA"))) {
                target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
            } else if ((privacy == null || privacy.equals("NA")) && (auth != null || !auth.equals("NA"))) {
                target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
            } else {
                target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            }

            target.setSecurityName(new OctetString(user_name));

        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return target;
    }
}
