/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.datasource.Datasource;
import npm.inf.pojo.InterfaceBandwidthInfo;
import npm.inf.pojo.InterfaceCRCInfo;
import npm.inf.pojo.InterfaceMonInfo;
import npm.inf.pojo.InterfaceStatusModel;
import npm.inf.pojo.SNMPConfigMaster;
import npm.inf.thread.BandwidthBatchProcessingSensor;
import npm.inf.thread.CRCBatchProcessingSensor;
import npm.inf.thread.InterfaceMonRequester;
import npm.inf.thread.InterfaceStatusUpdateBatch;

/**
 *
 * @author NPM
 */
public class NodeInfMonitoring {

    public static Logger logger = Logger.getLogger(NodeInfMonitoring.class.getName());
    public static HashMap<String, String> device_snmp_map = null;
    public static HashMap<String, String> device_type_map = null;
    public static HashMap<String, SNMPConfigMaster> snmp_config_map = null;
    public static ArrayList<InterfaceMonInfo> interface_mon_list = null;
    public static HashMap<String, InterfaceMonInfo> interfaceStatusMap = null;
    public static ArrayList<InterfaceBandwidthInfo> interfaceBandwidthUpdateList = null;
    public static ArrayList<InterfaceBandwidthInfo> interfaceBandwidthUpdateListTemp = null;
    public static ArrayList<InterfaceBandwidthInfo> interfaceBandwidthInsertList = null;
    public static ArrayList<InterfaceBandwidthInfo> interfaceBandwidthInsertListTemp = null;
    public static ArrayList<InterfaceCRCInfo> interfaceCRCUpdateList = null;
    public static ArrayList<InterfaceCRCInfo> interfaceCRCUpdateListTemp = null;
    public static ArrayList<InterfaceCRCInfo> interfaceCRCInsertList = null;
    public static ArrayList<InterfaceCRCInfo> interfaceCRCInsertListTemp = null;
    public static HashMap<String, String> interfaceInBWStatusMap = null;
    public static HashMap<String, String> interfaceOutBWStatusMap = null;

    public static CopyOnWriteArrayList<InterfaceStatusModel> statusUpdateInterfaceList = null;
    public static CopyOnWriteArrayList<InterfaceStatusModel> tempStatusUpdateInterfaceList = null;

    public static boolean isSimulation = true;

    public static void main(String[] args) {

        System.out.println("Interface monitoring 27MAR25  timing 6 to 22 ");
        statusUpdateInterfaceList = new CopyOnWriteArrayList<>();
        tempStatusUpdateInterfaceList = new CopyOnWriteArrayList<>();
        device_snmp_map = new HashMap<>();
        device_type_map = new HashMap<>();
        snmp_config_map = new HashMap<>();
        interface_mon_list = new ArrayList<>();
        interfaceStatusMap = new HashMap<>();
        interfaceBandwidthUpdateList = new ArrayList<>();
        interfaceBandwidthUpdateListTemp = new ArrayList<>();
        interfaceBandwidthInsertList = new ArrayList<>();
        interfaceBandwidthInsertListTemp = new ArrayList<>();
        interfaceCRCUpdateList = new ArrayList<>();
        interfaceCRCUpdateListTemp = new ArrayList<>();
        interfaceCRCInsertList = new ArrayList<>();
        interfaceCRCInsertListTemp = new ArrayList<>();
        interfaceInBWStatusMap = new HashMap<>();
        interfaceOutBWStatusMap = new HashMap<>();

        try {

            NodeInfMonitoring monitoring = null;
            monitoring = new NodeInfMonitoring();

            monitoring.initializeDeviceSNNMP();
            monitoring.initializeSNNMPConfig();
            monitoring.initializeInterfaceMonitor();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in Parameter Initialization", e);
        }

        try {

            Thread thread = null;
            thread = new Thread(new InterfaceMonRequester());
            thread.start();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in InterfaceMonRequester Thread", e);
        }

        try {
            Thread t2 = null;
            t2 = new Thread(new InterfaceStatusUpdateBatch());
            t2.start();
        } catch (Exception e) {
            System.out.println("Exception LatencyUpdate:" + e);
        }

        try {

            Thread thread = null;
            thread = new Thread(new BandwidthBatchProcessingSensor());
            thread.start();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in BandwidthBatchProcessingSensor", e);
        }

//        try {
//
//            Thread thread = null;
//            thread = new Thread(new CRCBatchProcessingSensor());
//            thread.start();
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Exception in CRCBatchProcessingSensor", e);
//        }
    }

    private void initializeDeviceSNNMP() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT DEVICE_IP, SNMP, DEVICE_TYPE FROM add_node GROUP BY DEVICE_IP, SNMP, DEVICE_TYPE");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String device_ip = resultSet.getString(1).trim();
                String snmp_name = resultSet.getString(2).trim();
                device_snmp_map.put(device_ip, snmp_name);
                device_type_map.put(device_ip, resultSet.getString(3).trim());

            }

            logger.log(Level.INFO, "Device SNMP Map:{0}", device_snmp_map);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in initializeDeviceSNNMP", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private void initializeSNNMPConfig() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        SNMPConfigMaster configMaster = null;
        configMaster = new SNMPConfigMaster();

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT NAME,DESCRIPTION,PORT,RETRIES,TIMEOUT,VERSION,COMMUNITY,CONTEXT,USERNAME,AUTH_PROTOCOL,AUTH_PASS,ENCRYPT_PROTOCOL,ENCRYPT_PASS FROM SNMP_CONFIG_MASTER");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                configMaster = null;
                configMaster = new SNMPConfigMaster();

                String snmp_name = resultSet.getString(1).trim();
                configMaster.setName(snmp_name);
                configMaster.setDescription(resultSet.getString(2).trim());
                configMaster.setPort(resultSet.getInt(3));
                configMaster.setRetries(resultSet.getInt(4));
                configMaster.setTimeout(resultSet.getInt(5));
                configMaster.setVersion(resultSet.getString(6).trim());
                configMaster.setCommunity(resultSet.getString(7).trim());
                configMaster.setContext(resultSet.getString(8).trim());
                configMaster.setUsername(resultSet.getString(9).trim());
                configMaster.setAuthentication_protocol(resultSet.getString(10).trim());
                configMaster.setAuthentication_password(resultSet.getString(11).trim());
                configMaster.setEncryption_protocol(resultSet.getString(12).trim());
                configMaster.setEncryption_password(resultSet.getString(13).trim());

                snmp_config_map.put(snmp_name, configMaster);
            }

            logger.log(Level.INFO, "SNMP Config Map:{0}", snmp_config_map);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in initializeSNNMPConfig", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private void initializeInterfaceMonitor() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InterfaceMonInfo interfaceMonInfo = null;
        interfaceMonInfo = new InterfaceMonInfo();

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT im.NODE_IP,im.INTERFACE_NAME,im.INTERFACE_ID,im.ALIAS_NAME,im.ADMIN_STATUS,im.OPER_STATUS,\n"
                    + "im.IN_TRAFFIC,im.OUT_TRAFFIC,im.CRC_ERROR,im.DISCARD_INPUT,im.DISCARD_OUTPUT,im.INTERFACE_ERROR,\n"
                    + "im.AUTO_TICKETING,im.BW_HISTORY_PARAM,im.BW_THRESHOLD,im.CRC_HISTORY_PARAM,im.MAIL_ALERT,im.SMS_ALERT,\n"
                    + "im.IN_BW_PERCENT,im.OUT_BW_PERCENT,im.PROCURED_BANDWIDTH,im.Interface_IP_Assign,ad.DEVICE_TYPE, ad.DEVICE_NAME\n"
                    + "FROM interface_monitoring im JOIN add_node ad ON im.NODE_IP = ad.DEVICE_IP\n"
                    + "WHERE im.MONITORING_PARAM='Yes'");
            resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count = count + 1;

                interfaceMonInfo = null;
                interfaceMonInfo = new InterfaceMonInfo();

                interfaceMonInfo.setDevice_ip(resultSet.getString(1));
                interfaceMonInfo.setInterface_name(resultSet.getString(2));
                interfaceMonInfo.setInterface_id(resultSet.getString(3));
                interfaceMonInfo.setAlias_name(resultSet.getString(4));
                interfaceMonInfo.setAdmin_status(resultSet.getString(5));
                interfaceMonInfo.setOperational_status(resultSet.getString(6));
                interfaceMonInfo.setIn_traffic(resultSet.getDouble(7));
                interfaceMonInfo.setOut_traffic(resultSet.getDouble(8));
                interfaceMonInfo.setCrc_error(resultSet.getString(9));
                interfaceMonInfo.setDiscard_input_packet(resultSet.getString(10));
                interfaceMonInfo.setDiscard_output_packet(resultSet.getString(11));
                interfaceMonInfo.setInterface_errors(resultSet.getString(12));
                interfaceMonInfo.setAuto_ticketing_param(resultSet.getString(13));
                interfaceMonInfo.setBw_history_param(resultSet.getString(14));
                interfaceMonInfo.setBw_threshold(resultSet.getDouble(15));
                interfaceMonInfo.setCrc_history_param(resultSet.getString(16));
                interfaceMonInfo.setMail_alert_param(resultSet.getString(17));
                interfaceMonInfo.setSms_alert_param(resultSet.getString(18));
                interfaceMonInfo.setIn_bw_percent(resultSet.getDouble(19));
                interfaceMonInfo.setOut_bw_percent(resultSet.getDouble(20));
                interfaceMonInfo.setProcured_bw(resultSet.getDouble(21));
                interfaceMonInfo.setDevice_type(resultSet.getString(23));
                interfaceMonInfo.setDeviceName(resultSet.getString(24));
                //   System.out.println(resultSet.getString(1)+resultSet.getString(2)+":temp inf IP:"+resultSet.getString(22));
                String inf_assign_ip = resultSet.getString(22);

                if (inf_assign_ip == null) {
                    inf_assign_ip = "-";
                }
                interfaceMonInfo.setInterface_ip(inf_assign_ip);

                interface_mon_list.add(interfaceMonInfo);

                String deviceInterface = resultSet.getString(1).trim() + "~" + resultSet.getString(2).trim();
                System.out.println("Interface Monitoring:" + count + ":" + deviceInterface);

                interfaceStatusMap.put(deviceInterface, interfaceMonInfo);
                interfaceInBWStatusMap.put(deviceInterface, "Normal");
                interfaceOutBWStatusMap.put(deviceInterface, "Normal");

            }

            // logger.log(Level.INFO, "Interface Monitor List Size :{0}", interface_mon_list.size());
        } catch (Exception e) {
            System.out.println("Exception in initializeInterfaceMonitor:" + e);
            logger.log(Level.SEVERE, "Exception in initializeInterfaceMonitor", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }

        System.out.println("Interface Monitor List Size :{0}" + interface_mon_list.size());

    }

}
