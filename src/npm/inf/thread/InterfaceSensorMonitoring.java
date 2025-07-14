/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.datasource.Datasource;
import npm.inf.init.NodeInfMonitoring;
import npm.inf.pojo.InterfaceBandwidthInfo;
import npm.inf.pojo.InterfaceMonInfo;
import npm.inf.pojo.InterfaceStatusModel;
import npm.inf.pojo.SNMPConfigMaster;
import npm.inf.pojo.SNMPUtilV2;
import npm.inf.pojo.SNMPUtilV3;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;

/**
 *
 * @author NPM
 */
public class InterfaceSensorMonitoring implements Runnable {

    public void updateInterfaceStatus(String device_ip, String interface_name, String interface_type, String admin_status, String operational_status, Timestamp eventTime) {
        try {
            InterfaceStatusModel node = new InterfaceStatusModel();
            node.setDevice_ip(device_ip);
            node.setInterface_name(interface_name);
            node.setInterface_type(interface_type);
            node.setAdmin_status(admin_status);
            node.setOperational_status(operational_status);
            node.setEventTime(eventTime);
            NodeInfMonitoring.statusUpdateInterfaceList.add(node);
        } catch (Exception exp) {
            System.out.println(device_ip + "Exception in adding update icmp status=" + exp);
        }
    }

    // public static Logger logger = Logger.getLogger(InterfaceSensorMonitoring.class.getName());
    ArrayList<InterfaceMonInfo> interfaceMonList = null;
    String device_ip = null;
    String deviceName = null;
    String interface_name = null;
    String interface_id = null;
    String interfaceAdminStatus = null;
    String interfaceOperationalStatus = null;
    String auto_ticketing_param = null;
    String bw_history_param = null;
    double bw_threshold = 0;
    double procured_bw = 0;
    String crc_history_param = null;
    String mail_alert_param = null;
    String sms_alert_param = null;
    String crc_value = null;
    final int MS_DIVIDEND = 1000;
    String deviceInterface = null;
    String Interface_IP_Assign = "";
    String deviceType = null;

    public InterfaceSensorMonitoring(ArrayList<InterfaceMonInfo> interfaceList) {
        this.interfaceMonList = interfaceList;
        // System.out.println("Thread started for inf monitoring....");
    }

    @Override
    public void run() {

        while (true) {

            Iterator<InterfaceMonInfo> iterator = interfaceMonList.iterator();

            while (iterator.hasNext()) {
                InterfaceMonInfo interfaceMonInfo = iterator.next();

                //    logger.log(Level.INFO, "Device IP:{0} Interface Name:{1}", new Object[]{interfaceMonInfo.getDevice_ip(), interfaceMonInfo.getInterface_name()});
                device_ip = interfaceMonInfo.getDevice_ip();
                deviceName = interfaceMonInfo.getDeviceName();
                interface_name = interfaceMonInfo.getInterface_name();
                interface_id = interfaceMonInfo.getInterface_id();
                System.out.println("interface id  = "+interface_id);
                bw_history_param = interfaceMonInfo.getBw_history_param();
                bw_threshold = interfaceMonInfo.getBw_threshold();
                crc_history_param = interfaceMonInfo.getCrc_history_param();
                mail_alert_param = interfaceMonInfo.getMail_alert_param();
                sms_alert_param = interfaceMonInfo.getSms_alert_param();
                procured_bw = interfaceMonInfo.getProcured_bw();
                Interface_IP_Assign = interfaceMonInfo.getInterface_ip();
                deviceType = interfaceMonInfo.getDevice_type();
                //  System.out.println("Interface_IP_Assign:"+);

                deviceInterface = device_ip + "~" + interface_name;

                SNMPUtilV2 snmpv2 = null;
                snmpv2 = new SNMPUtilV2();

                SNMPUtilV3 snmpv3 = null;
                snmpv3 = new SNMPUtilV3();

                SNMPConfigMaster configMaster = null;

                Target target = null;

                /*Closing SNMP Objects Start*/
                try {
                    if (snmpv2 != null) {
                        snmpv2.stop();
                    }
                    if (snmpv3 != null) {
                        snmpv3.stop();
                    }
                } catch (Exception e) {
                    //   logger.log(Level.SEVERE, "Eception in closing snmp objects at start:{0}", e);
                }
                /*Closing SNMP Objects End*/

                String snmp_template = "NA";

                boolean isSnmpNotEnabled = false;
                boolean isSnmpNotEnabled2 = false;

                try {
                    if (!NodeInfMonitoring.device_snmp_map.isEmpty()) {
                        snmp_template = NodeInfMonitoring.device_snmp_map.get(device_ip);

                    }
                } catch (Exception e) {
                    // logger.log(Level.SEVERE, "Exception in get SNMP template", e);
                }

                // System.out.println("interace monitoring online for snmp template :: " + snmp_template);
                if (!"NA".equals(snmp_template) && !"".equals(snmp_template)) {

                    try {
                        if (!NodeInfMonitoring.snmp_config_map.isEmpty()) {
                            configMaster = NodeInfMonitoring.snmp_config_map.get(snmp_template);

                        }
                    } catch (Exception e) {
                        //  logger.log(Level.SEVERE, "Exception in get SNMPConfigMaster", e);
                    }

                    if (configMaster != null) {

                        // logger.log(Level.INFO, "SNMP Template Name:{0} Username:{1}", new Object[]{configMaster.getName(), configMaster.getUsername()});
                        try {

                            if (configMaster.getVersion().equals("Version3")) {
                                snmpv3.start(configMaster.getUsername(), configMaster.getAuthentication_protocol(), configMaster.getAuthentication_password(), configMaster.getEncryption_protocol(), configMaster.getEncryption_password());
                                target = snmpv3.getTargetVersion3("udp:" + device_ip + "/161", configMaster.getUsername(), configMaster.getAuthentication_protocol(), configMaster.getAuthentication_password(), configMaster.getEncryption_protocol(), configMaster.getEncryption_password(), SnmpConstants.version3);

                            } else if (configMaster.getVersion().equals("Version1")) {
                                snmpv2.start();
                                target = snmpv2.getTarget("udp:" + device_ip + "/" + configMaster.getPort(), configMaster.getCommunity(), SnmpConstants.version1);

                            } else {
                                snmpv2.start();
                                if (NodeInfMonitoring.isSimulation) {

                                    target = snmpv2.getTarget("udp:127.0.0.1/161", configMaster.getCommunity(), SnmpConstants.version2c);
                                    System.out.println("Simulation started...");

                                } else {
                                    target = snmpv2.getTarget("udp:" + device_ip + "/" + configMaster.getPort(), configMaster.getCommunity(), SnmpConstants.version2c);

                                }

                            }

                        } catch (Exception e) {
                            //   logger.log(Level.SEVERE, "SNMP Target Exception Device IP{0} Exception:{1}", new Object[]{device_ip, e});
                            System.out.println("SNMP not found " + device_ip);
                        }

                        /* Interface Status Monitoring *Start**/
                        String interface_adminStatus = null;
                        try {

                            if (configMaster.getVersion().equals("Version3")) {
                                interface_adminStatus = snmpv3.BandwidthGetVect(target, "ifadmin", interface_id);
                            } else {
                                interface_adminStatus = snmpv2.BandwidthGetVect(target, "ifadmin", interface_id);
                            }

                               System.out.println("snmp admin status:" + device_ip + ":" + interface_id + ":" + interface_adminStatus);
                            if (interface_adminStatus == null) {
                                interfaceAdminStatus = "down";

                            } else if (interface_adminStatus.equals("1")) {
                                interfaceAdminStatus = "up";
                            } else if (interface_adminStatus.equals("2")) {
                                interfaceAdminStatus = "down";
                            } else if (interface_adminStatus.equals("3")) {
                                interfaceAdminStatus = "testing";
                            } else if (interface_adminStatus.equals("4")) {
                                interfaceAdminStatus = "unknown";
                            } else if (interface_adminStatus.equals("5")) {
                                interfaceAdminStatus = "dormant";
                            } else if (interface_adminStatus.equals("6")) {
                                interfaceAdminStatus = "notPresent";
                            } else if (interface_adminStatus.equals("7")) {
                                interfaceAdminStatus = "lowerLayerDown";
                            } else {
                                interfaceAdminStatus = "none";
                            }

                        } catch (Exception e) {
                            isSnmpNotEnabled = true;
                            System.out.println("Exception SNMP currently not available:" + device_ip + ":" + e);
                            // logger.log(Level.SEVERE, "Exception in get InterfaceAdminStatus DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }
                        //  System.out.println("Interface status:" + device_ip + ":" + interface_name + ":" + interface_adminStatus + ":" + interfaceAdminStatus);
                        if (!isSnmpNotEnabled) {
                            System.out.println("inside oper status");
                            String interface_operaStatus = null;
                            try {

                                if (configMaster.getVersion().equals("Version3")) {
                                    interface_operaStatus = snmpv3.BandwidthGetVect(target, "ifopera", interface_id);
                                } else {
                                    interface_operaStatus = snmpv2.BandwidthGetVect(target, "ifopera", interface_id);
                                    System.out.println("Operational status = "+interface_operaStatus);
                                }

                                if (interface_operaStatus == null) {
                                    interfaceOperationalStatus = "down";
                                } else if (interface_operaStatus.equals("1")) {
                                    interfaceOperationalStatus = "up";
                                } else if (interface_operaStatus.equals("2")) {
                                    interfaceOperationalStatus = "down";
                                } else if (interface_operaStatus.equals("3")) {
                                    interfaceOperationalStatus = "testing";
                                } else if (interface_operaStatus.equals("4")) {
                                    interfaceOperationalStatus = "unknown";
                                } else if (interface_operaStatus.equals("5")) {
                                    interfaceOperationalStatus = "dormant";
                                } else if (interface_operaStatus.equals("6")) {
                                    interfaceOperationalStatus = "notPresent";
                                } else if (interface_operaStatus.equals("7")) {
                                    interfaceOperationalStatus = "lowerLayerDown";
                                } else {
                                    interfaceOperationalStatus = "none";
                                }

                            } catch (Exception e) {
                                isSnmpNotEnabled2 = true;
                                //  logger.log(Level.SEVERE, "Exception in get InterfaceOperationalStatus DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                            }

                        }

                        // logger.log(Level.INFO, "Device IP:{0} Interface Name:{1} AdminStatus:{2} OperationalStatus:{3}", new Object[]{device_ip, interface_name, interfaceAdminStatus, interfaceOperationalStatus});
                        // System.out.println("@Device IP:Interface Name:{1} AdminStatus:{2} OperationalStatus:{3}" + device_ip + ":" + interface_name + ":" + interfaceAdminStatus + ":" + interfaceOperationalStatus);
                        if (!isSnmpNotEnabled) {

                            if (!NodeInfMonitoring.interfaceStatusMap.isEmpty() && NodeInfMonitoring.interfaceStatusMap.containsKey(deviceInterface)) {

                                InterfaceMonInfo interfaceMonInfo1 = null;
                                interfaceMonInfo1 = NodeInfMonitoring.interfaceStatusMap.get(deviceInterface);

                                String admin_status_old = interfaceMonInfo1.getAdmin_status();

                                if (interfaceAdminStatus == null || admin_status_old == null || interfaceAdminStatus.equals(admin_status_old) || interfaceAdminStatus.equals("noSuchInstance") || interfaceAdminStatus.equals("noSuchObject") || interfaceAdminStatus.equals("none")) {

                                } else if (interfaceAdminStatus.length() > 1 && admin_status_old.length() > 1 && !interfaceAdminStatus.equals(admin_status_old)) {
                                    System.out.println("$$ Admin Status Chnage:" + deviceInterface + ":" + admin_status_old + ":" + interfaceAdminStatus);

                                    // DatabaseHelper statusDBOperations = null;
                                    // statusDBOperations = new DatabaseHelper();
                                    Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                                    updateInterfaceStatus(device_ip, interface_name, "admin", interfaceAdminStatus, interfaceOperationalStatus, timestamp);

                                    //statusDBOperations.updateInterfaceStatus(device_ip, interface_name, interfaceAdminStatus, interfaceOperationalStatus, timestamp);
                                    //statusDBOperations.insertInterfaceStatus(device_ip, interface_name, interfaceAdminStatus, "admin", "NA", timestamp);
                                    interfaceMonInfo1.setAdmin_status(interfaceAdminStatus);
                                    NodeInfMonitoring.interfaceStatusMap.replace(deviceInterface, interfaceMonInfo1);

                                }

                                String operational_status_old = interfaceMonInfo1.getOperational_status();
                                String isAffected = "";
                                String problem = "";
                                String serviceId = "interface_status";
                                String eventMsg = null;
                                String netadmin_msg = null;
                                int severity;
                                
                                if (interfaceOperationalStatus == null || operational_status_old == null || interfaceOperationalStatus.equals(operational_status_old) || interfaceOperationalStatus.equals("noSuchInstance") || interfaceOperationalStatus.equals("noSuchObject")) {
                                } else if (interfaceOperationalStatus.length() > 1 && operational_status_old.length() > 1 && !interfaceOperationalStatus.equals("none")) {
                                    System.out.println("@@ OPerational Interface Status changed:" + device_ip + ":" + interface_name + ":" + operational_status_old + ":" + interfaceOperationalStatus);
                                    // DatabaseHelper statusDBOperations = null;
                                    // statusDBOperations = new DatabaseHelper();

                                    Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                                    // statusDBOperations.updateInterfaceStatus(device_ip, interface_name, interfaceAdminStatus, interfaceOperationalStatus, timestamp);
                                    //statusDBOperations.insertInterfaceStatus(device_ip, interface_name, interfaceOperationalStatus, "operational", "NA", timestamp);
                                    updateInterfaceStatus(device_ip, interface_name, "operational", interfaceAdminStatus, interfaceOperationalStatus, timestamp);
                                    severity = interfaceOperationalStatus.equalsIgnoreCase("up") ? 0 : 4;
                                    isAffected = interfaceOperationalStatus.equalsIgnoreCase("up") ? "0" : "1";
                                    problem = interfaceOperationalStatus.equalsIgnoreCase("up") ? "Cleared" : "problem";
                                    eventMsg = "Interface " + interface_name + " is " + interfaceOperationalStatus;
                                    netadmin_msg = "Interface " + interface_name + " is " + interfaceOperationalStatus;
                                            
                                    interfaceMonInfo1.setOperational_status(interfaceOperationalStatus);

                                    insertIntoEventLog(device_ip, deviceName, eventMsg, severity, "Port stat", timestamp, netadmin_msg, isAffected, problem, serviceId, deviceType); //Evrnt log
                                    
                                    NodeInfMonitoring.interfaceStatusMap.replace(deviceInterface, interfaceMonInfo1);

//TO Do - uncomment
//                                    if (interfaceOperationalStatus.equals("up")) {
//                                        InterfaceDownStatusStats downStatusStats = null;;
//                                        downStatusStats = new InterfaceDownStatusStats();
//                                        downStatusStats.insertStatusDiff(device_ip, interface_name, timestamp);
//
//                                    }
                                }

                            }

                        }

                        //Interface In Errors
//                        System.out.println("Started Interface in out errors");
                        String in_error_value = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                // in_error_value = snmpv3.BandwidthGetVect(target, "in_error", interface_id);
                            } else {
                                in_error_value = snmpv2.BandwidthGetVect(target, "ifinerrors", interface_id);
//                                System.out.println("Interface In error : "+in_error_value);

                            }
                        } catch (Exception e) {

                            // logger.log(Level.SEVERE, "Exception in getting Interface In Errors DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        //Interface out Errors
                        String out_error_value = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                // in_error_value = snmpv3.BandwidthGetVect(target, "in_error", interface_id);
                            } else {
                                out_error_value = snmpv2.BandwidthGetVect(target, "ifouterrors", interface_id);
//                                System.out.println("Interface out error : "+out_error_value);
                            }
                        } catch (Exception e) {

                            //  logger.log(Level.SEVERE, "Exception in getting Interface out Errors DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        //Interface In Discards
//                        System.out.println("Started Interface in out Discards");
                        String in_discard_value = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                // in_error_value = snmpv3.BandwidthGetVect(target, "in_error", interface_id);
                            } else {
                                in_discard_value = snmpv2.BandwidthGetVect(target, "ifindiscards", interface_id);
//                                System.out.println("Interface In Discard : "+in_discard_value);
                            }
                        } catch (Exception e) {

                            //  logger.log(Level.SEVERE, "Exception in getting Interface In Discard DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        //Interface out Discard
                        String out_discard_value = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                // in_error_value = snmpv3.BandwidthGetVect(target, "in_error", interface_id);
                            } else {
                                out_discard_value = snmpv2.BandwidthGetVect(target, "ifoutdiscards", interface_id);
//                                System.out.println("Interface out discard error : "+out_discard_value);
                            }
                        } catch (Exception e) {

                            //  logger.log(Level.SEVERE, "Exception in getting Interface out Discard DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        //Interface MTU
                        String mtu_value = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                // in_error_value = snmpv3.BandwidthGetVect(target, "in_error", interface_id);
                            } else {
                                mtu_value = snmpv2.BandwidthGetVect(target, "mtu", interface_id);
//                                System.out.println("Interface out discard error : "+out_discard_value);
                            }
                        } catch (Exception e) {

                            // logger.log(Level.SEVERE, "Exception in getting MTU of interface DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        //Interface/Port Description
                        String portDescription = null;
                        try {
                            if (configMaster.getVersion().equals("Version3")) {
                                portDescription = snmpv3.BandwidthGetVect(target, "portDescription", interface_id);
                            } else {
                                portDescription = snmpv2.BandwidthGetVect(target, "portDescription", interface_id);
                                //System.out.println("Interface Description for interface " + interface_name + ": " + portDescription);
                            }
                        } catch (Exception e) {

                            // logger.log(Level.SEVERE, "Exception in getting Interface/Port Description DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                        }

                        if (configMaster.getVersion().equals("Version3")) {
                            crc_value = snmpv3.BandwidthGetVect(target, "crc", interface_id);
                        } else if (NodeInfMonitoring.device_type_map.get(device_ip).equalsIgnoreCase("DMBS")) {
                            crc_value = snmpv2.BandwidthGetVect(target, "MicrowaveCRC", interface_id);
                        } else {
                            crc_value = snmpv2.BandwidthGetVect(target, "crc", interface_id);
                        }

                        // System.out.println("Model name continous monitoring");
                        //need to change
//                        DatabaseHelper interfaceError = new DatabaseHelper();
//                        interfaceError.updateInterfaceErrorsAndDiscard(device_ip, interface_name, in_error_value, out_error_value, in_discard_value, out_discard_value, new Timestamp(new java.util.Date().getTime()), mtu_value, portDescription);

                        /* Interface Status Monitoring *End**/
                        if (isSnmpNotEnabled == false && isSnmpNotEnabled2 == false) {

                            /* Interface Bandwidth Monitoring *Start**/
                            String outBW = null;
                            String outBW1 = null;
                            Date outBWTime = new Date();
                            Date outBWTime1 = new Date();

                            String inBW = null;
                            String inBW1 = null;
                            Date inBWTime = new Date();
                            Date inBWTime1 = new Date();

                            boolean isBWExcep = false;

                            try {
                                outBWTime = new Date();

                                if (configMaster.getVersion().equals("Version3")) {

                                    outBW = snmpv3.BandwidthGetVect(target, "Out", interface_id);
                                } else if (NodeInfMonitoring.device_type_map.get(device_ip).equalsIgnoreCase("DMBS")) {
                                    outBW = snmpv2.BandwidthGetVect(target, "MicrowaveOutOctet", interface_id);
                                } else {
                                    outBW = snmpv2.BandwidthGetVect(target, "Out", interface_id);
                                }

                                Thread.sleep(5000);

                                if (configMaster.getVersion().equals("Version3")) {
                                    outBW1 = snmpv3.BandwidthGetVect(target, "Out", interface_id);
                                } else if (NodeInfMonitoring.device_type_map.get(device_ip).equalsIgnoreCase("DMBS")) {
                                    outBW1 = snmpv2.BandwidthGetVect(target, "MicrowaveOutOctet", interface_id);
                                } else {
                                    outBW1 = snmpv2.BandwidthGetVect(target, "Out", interface_id);
                                }

                                outBWTime1 = new Date();
                            } catch (Exception e) {
                                isBWExcep = true;
                                System.out.println("Exception in get Out Bandwidth DeviceIP:{0} InterfaceName:{1} ErrorString:{2}\", new Object[]{device_ip, interface_name, e");
                                // logger.log(Level.SEVERE, "Exception in get Out Bandwidth DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                            }

                            try {

                                inBWTime = new Date();

                                if (configMaster.getVersion().equals("Version3")) {
                                    inBW = snmpv3.BandwidthGetVect(target, "In", interface_id);
                                } else if (NodeInfMonitoring.device_type_map.get(device_ip).equalsIgnoreCase("DMBS")) {
                                    inBW = snmpv2.BandwidthGetVect(target, "MicrowaveInOctet", interface_id);
                                } else {
                                    inBW = snmpv2.BandwidthGetVect(target, "In", interface_id);
                                }

                                Thread.sleep(5000);

                                if (configMaster.getVersion().equals("Version3")) {
                                    inBW1 = snmpv3.BandwidthGetVect(target, "In", interface_id);
                                } else if (NodeInfMonitoring.device_type_map.get(device_ip).equalsIgnoreCase("DMBS")) {
                                    inBW1 = snmpv2.BandwidthGetVect(target, "MicrowaveInOctet", interface_id);
                                } else {
                                    inBW1 = snmpv2.BandwidthGetVect(target, "In", interface_id);
                                }

                                inBWTime1 = new Date();

                            } catch (Exception e) {
                                isBWExcep = true;
                                //  logger.log(Level.SEVERE, "Exception in get In Bandwidth DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
                            }

                            if (isBWExcep == true || outBW == null || outBW.contains("noSuchInstance") || outBW.contains("noSuchObject") || inBW == null || inBW.contains("noSuchInstance") || inBW.contains("noSuchObject")) {
                            } else {

                                long inBandwidth = 0;
                                long outBandwidth = 0;
                                long outTimeElapsed = 0;
                                long inTimeElapsed = 0;

                                double sendBW = 0;
                                double receiveBW = 0;

                                int inBWPercent = 0;
                                int outBWPercent = 0;

                                boolean isSkip = false;
                                try {

                                    outTimeElapsed = (long) (outBWTime1.getTime() - outBWTime.getTime()) / MS_DIVIDEND;
                                    inTimeElapsed = (long) (inBWTime1.getTime() - inBWTime.getTime()) / MS_DIVIDEND;

                                    long outBandwidthL = Long.parseLong(outBW);
                                    long outBandwidthL1 = Long.parseLong(outBW1);
                                    long inBandwidthL = Long.parseLong(inBW);
                                    long inBandwidthL1 = Long.parseLong(inBW1);

                                    long Oututilization = (outBandwidthL1 - outBandwidthL) * 8;
                                    long Inutilization = (inBandwidthL1 - inBandwidthL) * 8;

                                    inBandwidth = Inutilization / inTimeElapsed;
                                    outBandwidth = Oututilization / outTimeElapsed;

                                    if (inBandwidth < 0 || outBandwidth < 0) {
                                        isSkip = true;
                                    }

                                    receiveBW = (double) inBandwidth;
                                    sendBW = (double) outBandwidth;

                                    double inPercent = (Math.round((int) ((receiveBW / procured_bw) * 100)));
                                    double outPercent = Math.round((int) ((sendBW / procured_bw) * 100));

                                    inBWPercent = (int) inPercent;
                                    outBWPercent = (int) outPercent;

                                } catch (Exception e) {
                                    //  logger.log(Level.SEVERE, "Exception in Casting Bandwidth:", e);
                                }

                                // logger.log(Level.INFO, "Bandwidth Utilization in BPS DeviceIP:{0} InterfaceName:{1} OutBandwidth:{2} InBandwidth:{3} ProcuredBW:{4} InBWPercent:{5} OutBWPercent:{6}", new Object[]{device_ip, interface_name, outBandwidth, inBandwidth, procured_bw, inBWPercent, outBWPercent});
                                // if (isSkip == false) {
//                                    LocalTime startOfWork = LocalTime.of(9, 0); // 9:00 AM
//                                    LocalTime endOfWork = LocalTime.of(18, 0);  // 6:00 PM
//
//                                    // Get the current time
//                                    LocalTime currentTime = LocalTime.now();
//
//                                    // Check if the current time is within working hours
//                                    boolean isWorkingHour = !currentTime.isBefore(startOfWork) && !currentTime.isAfter(endOfWork);
//                                    int workingHourFlag = 1;
//
//                                    if (isWorkingHour) {
//                                        workingHourFlag = 1;
//                                    } else {
//                                        workingHourFlag = 0;
//                                    }
//                                LocalTime startOfWork = LocalTime.of(7, 0); // 7:00 AM
//                                LocalTime endOfWork = LocalTime.of(21, 0);  // 9:00 PM
//                                
                                LocalTime startOfWork = LocalTime.of(6, 0); // 7:00 AM
                                LocalTime endOfWork = LocalTime.of(22, 0);  // 9:00 PM

                                // Get the current time and day
                                LocalTime currentTime = LocalTime.now();
                                DayOfWeek currentDay = LocalDate.now().getDayOfWeek();

                                // Check if it's a working day (Monday to Friday)
                                boolean isWorkingDay = currentDay != DayOfWeek.SATURDAY && currentDay != DayOfWeek.SUNDAY;

                                // Check if the current time is within working hours
                                boolean isWorkingHour = isWorkingDay && !currentTime.isBefore(startOfWork) && !currentTime.isAfter(endOfWork);

                                // System.out.println("Is it working hour? " + isWorkingHour);
                                int workingHourFlag = 1;

                                if (isWorkingHour) {
                                    workingHourFlag = 1;
                                } else {
                                    workingHourFlag = 0;
                                }

                                LocalDateTime currentDateTime = LocalDateTime.now();
                                long epochTime = System.currentTimeMillis() / 1000;

                                // Format it as a string in 'YYYY-MM-DD HH:MM:SS' format
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String formattedDateTime = currentDateTime.format(formatter);

                                InterfaceBandwidthInfo bandwidthInfo = null;
                                bandwidthInfo = new InterfaceBandwidthInfo();
                                bandwidthInfo.setDevice_ip(device_ip);
                                bandwidthInfo.setInterface_name(interface_name);
                                bandwidthInfo.setInterface_ip("NA");
                                bandwidthInfo.setIn_traffic(inBandwidth);
                                bandwidthInfo.setOut_traffic(outBandwidth);
                                bandwidthInfo.setCrc_value(Double.valueOf(crc_value));
                                bandwidthInfo.setEvent_timestamp(new Timestamp(new java.util.Date().getTime()));
                                bandwidthInfo.setWorkingHourFlag(workingHourFlag);
                                bandwidthInfo.setDatetime(formattedDateTime);
                                bandwidthInfo.setEpochTimeL(epochTime);
                                bandwidthInfo.setInterface_ip(Interface_IP_Assign);
                                bandwidthInfo.setInterface_status(interfaceOperationalStatus);

                                //add
                                bandwidthInfo.setINTERFACE_INPUT_ERROR(in_error_value);
                                bandwidthInfo.setINTERFACE_OUTPUT_ERROR(out_error_value);
                                bandwidthInfo.setDISCARD_INPUT(in_discard_value);
                                bandwidthInfo.setDISCARD_OUTPUT(out_discard_value);
                                bandwidthInfo.setMTU(mtu_value);
                                bandwidthInfo.setALIAS_NAME(portDescription);

                                NodeInfMonitoring.interfaceBandwidthUpdateList.add(bandwidthInfo);

                                // if (bw_history_param.equals("Yes")) {
                                NodeInfMonitoring.interfaceBandwidthInsertList.add(bandwidthInfo);
                                // }

//                                if (bw_threshold > 0) {
//
//                                    String inBandwidthStatus = NodeInfMonitoring.interfaceInBWStatusMap.get(deviceInterface);
//
//                                    if (inBWPercent > bw_threshold && inBandwidthStatus.equals("Normal")) {
//                                        NodeInfMonitoring.interfaceInBWStatusMap.put(deviceInterface, "Full");
//
//                                        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
//
//                                        DatabaseHelper databaseHelper = null;
//                                        databaseHelper = new DatabaseHelper();
//                                        databaseHelper.updateInterfaceBWStatus(device_ip, interface_name, "Full", timestamp, "In");
//                                        databaseHelper.insertInterfaceThresholdBWStatus(device_ip, interface_name, inBWPercent, bw_threshold, "Full", "NA", timestamp, "In");
//
//                                    } else if (inBWPercent < bw_threshold && inBandwidthStatus.equals("Full")) {
//                                        NodeInfMonitoring.interfaceInBWStatusMap.put(deviceInterface, "Normal");
//
//                                        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
//
//                                        DatabaseHelper databaseHelper = null;
//                                        databaseHelper = new DatabaseHelper();
//                                        databaseHelper.updateInterfaceBWStatus(device_ip, interface_name, "Normal", timestamp, "In");
//                                        databaseHelper.insertInterfaceThresholdBWStatus(device_ip, interface_name, inBWPercent, bw_threshold, "Normal", "NA", timestamp, "In");
//
//                                    }
//
//                                    String outBandwidthStatus = NodeInfMonitoring.interfaceOutBWStatusMap.get(deviceInterface);
//                                    if (outBWPercent > bw_threshold && outBandwidthStatus.equals("Normal")) {
//                                        NodeInfMonitoring.interfaceOutBWStatusMap.put(deviceInterface, "Full");
//
//                                        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
//
//                                        DatabaseHelper databaseHelper = null;
//                                        databaseHelper = new DatabaseHelper();
//                                        databaseHelper.updateInterfaceBWStatus(device_ip, interface_name, "Full", timestamp, "Out");
//                                        databaseHelper.insertInterfaceThresholdBWStatus(device_ip, interface_name, outBWPercent, bw_threshold, "Full", "NA", timestamp, "Out");
//                                    } else if (outBWPercent < bw_threshold && outBandwidthStatus.equals("Full")) {
//                                        NodeInfMonitoring.interfaceOutBWStatusMap.put(deviceInterface, "Normal");
//
//                                        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
//
//                                        DatabaseHelper databaseHelper = null;
//                                        databaseHelper = new DatabaseHelper();
//                                        databaseHelper.updateInterfaceBWStatus(device_ip, interface_name, "Normal", timestamp, "Out");
//                                        databaseHelper.insertInterfaceThresholdBWStatus(device_ip, interface_name, outBWPercent, bw_threshold, "Normal", "NA", timestamp, "Out");
//                                    }
//
//                                }
                                // }
                            }

                            /* Interface Bandwidth Monitoring *End**/
 /* Interface CRC Monitoring **Start**/
//                            if (crc_history_param.equals("Yes")) {
//
//                                boolean crcException = false;
//
//                                try {
//                                    if (configMaster.getVersion().equals("Version3")) {
//                                        crc_value = snmpv3.BandwidthGetVect(target, "crc", interface_id);
//                                    } else {
//                                        crc_value = snmpv2.BandwidthGetVect(target, "crc", interface_id);
//                                    }
//                                } catch (Exception e) {
//                                    crcException = true;
//                                    logger.log(Level.SEVERE, "Exception in get CRC Value DeviceIP:{0} InterfaceName:{1} ErrorString:{2}", new Object[]{device_ip, interface_name, e});
//                                }
//                                InterfaceMonInfo interfaceMonInfo1 = null;
//                                interfaceMonInfo1 = NodeInfMonitoring.interfaceStatusMap.get(deviceInterface);
//
//                                String old_crc_value = interfaceMonInfo1.getCrc_error();
//
//                                if (crcException == true || crc_value == null || crc_value.equals("noSuchInstance") || crc_value.equals("noSuchObject") || crc_value.equals(old_crc_value)) {
//                                } else {
//
//                                    interfaceMonInfo1.setCrc_error(crc_value);
//                                    NodeInfMonitoring.interfaceStatusMap.replace(deviceInterface, interfaceMonInfo1);
//
//                                    InterfaceCRCInfo crcInfo = null;
//                                    crcInfo = new InterfaceCRCInfo();
//                                    crcInfo.setDevice_ip(device_ip);
//                                    crcInfo.setInterface_name(interface_name);
//                                    crcInfo.setInterface_ip("NA");
//                                    crcInfo.setNew_crc_value(Integer.parseInt(crc_value));
//                                    crcInfo.setOld_crc_value(Integer.parseInt(old_crc_value));
//                                    crcInfo.setEvent_timestamp(new Timestamp(new java.util.Date().getTime()));
//
//                                    NodeInfMonitoring.interfaceCRCUpdateList.add(crcInfo);
//                                    NodeInfMonitoring.interfaceCRCInsertList.add(crcInfo);
//
//                                }
//
                            //                           }

                            /* Interface CRC Monitoring **End**/
                        }

                        /*Closing SNMP Objects Start*/
                        try {
                            if (snmpv2 != null) {
                                snmpv2.stop();
                            }
                            if (snmpv3 != null) {
                                snmpv3.stop();
                            }
                        } catch (Exception e) {
                            // logger.log(Level.SEVERE, "Eception in closing snmp objects at end:{0}", e);
                        }
                        /*Closing SNMP Objects End*/

                    } else {
                        // logger.log(Level.INFO, "SNMP Configuration is Empty Hence Skip... Template Name:{0} Device IP:{1}", new Object[]{snmp_template, device_ip});
                        System.out.println("SNMP Configuration is Empty Hence Skip... " + device_ip + ":" + snmp_template);
                    }
                } else {
                    //logger.log(Level.INFO, "SNMP Template is Empty Hence Skip... Template Name:{0} Device IP:{1}", new Object[]{snmp_template, device_ip});
                    System.out.println("SNMP Template is Empty Hence Skip... Template Name:" + device_ip + ":" + snmp_template);
                }

            }

            try {
                Thread.sleep(40000);
            } catch (InterruptedException ex) {
                Logger.getLogger(InterfaceSensorMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void insertIntoEventLog(String device_ip, String interface_name, String eventMsg, int severity, String service_name, Timestamp logtime, String netadmin_msg, String isAffected, String problem, String serviceId, String deviceType) {
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        Connection connection = null;
        try {
            connection = Datasource.getConnection();
            preparedStatement1 = connection.prepareStatement("INSERT INTO event_log (device_id, device_name, service_name, event_msg, netadmin_msg, severity,"
                    + " event_timestamp, acknowledgement_status, isAffected, Problem_Clear, Service_ID, Device_Type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement1.setString(1, device_ip);
            preparedStatement1.setString(2, interface_name);
            preparedStatement1.setString(3, service_name);
            preparedStatement1.setString(4, eventMsg);
            preparedStatement1.setString(5, netadmin_msg);
            preparedStatement1.setInt(6, severity);
            preparedStatement1.setTimestamp(7, logtime);
            preparedStatement1.setBoolean(8, false);
            preparedStatement1.setString(9, isAffected);
            preparedStatement1.setString(10, problem);
            preparedStatement1.setString(11, serviceId);
            preparedStatement1.setString(12, deviceType);

            preparedStatement1.executeUpdate();

        } catch (Exception e) {
            System.out.println(device_ip + "inserting in event log Exception:" + e);
        } finally {
            try {
                if (preparedStatement1 != null) {
                    preparedStatement1.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exp) {
                System.out.println("excep:" + exp);
            }
        }

        try {
            if ("Cleared".equalsIgnoreCase(problem)) {

                String updateQuery = "UPDATE event_log\n"
                        + "SET\n"
                        + "    Cleared_event_timestamp = ?,\n"
                        // + "    netadmin_msg = ?,\n"
                        + "netadmin_msg = CONCAT(netadmin_msg, ' => ', ?),\n"
                        + "    isAffected = ?\n"
                        + "WHERE\n"
                        + "    ID = (\n"
                        + "        SELECT id_alias.ID\n"
                        + "        FROM (\n"
                        + "            SELECT ID\n"
                        + "            FROM event_log\n"
                        + "            WHERE service_id = ?\n"
                        + "              AND device_id = ?\n"
                        + "            AND isaffected = '1' ORDER BY ID DESC\n"
                        + "            LIMIT 1\n"
                        + "        ) AS id_alias\n"
                        + "    )\n"
                        + ";";

                connection = Datasource.getConnection();

                preparedStatement2 = connection.prepareStatement(updateQuery);
                preparedStatement2.setTimestamp(1, logtime);

                preparedStatement2.setString(2, netadmin_msg); // To Do
                preparedStatement2.setString(3, "0");
                preparedStatement2.setString(4, serviceId);
                preparedStatement2.setString(5, device_ip);

                preparedStatement2.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Exception in update event log = " + e);
        } finally {
            try {
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exp) {
                System.out.println("excep:" + exp);
            }
        }
    }

}
