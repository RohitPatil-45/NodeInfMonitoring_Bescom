/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import npm.inf.datasource.Datasource;
import npm.inf.init.NodeInfMonitoring;

/**
 *
 * @author NPM
 */
public class BandwidthBatchProcessingSensor implements Runnable {

    //  Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement pStmt_bwstatus = null;
    PreparedStatement pStmt_bwhistory = null;
    // PreparedStatement preparedStatement2 = null;
    ResultSet resultSet = null;
    String sql = null;
   // public static Logger logger = Logger.getLogger(BandwidthBatchProcessingSensor.class.getName());

    @Override
    public void run() {

        while (true) {

            try {
                Thread.sleep(10000);
            } catch (Exception e) {

            }

            NodeInfMonitoring.interfaceBandwidthUpdateListTemp.clear();
            NodeInfMonitoring.interfaceBandwidthUpdateListTemp.addAll(NodeInfMonitoring.interfaceBandwidthUpdateList);
            NodeInfMonitoring.interfaceBandwidthUpdateList.clear();
            Connection connection_update = null;
            try {
                connection_update = Datasource.getConnection();
                sql = "UPDATE INTERFACE_MONITORING SET IN_TRAFFIC=?,OUT_TRAFFIC=?,INTERFACE_INPUT_ERROR=?,INTERFACE_OUTPUT_ERROR=?,DISCARD_INPUT=?,DISCARD_OUTPUT=?, MTU=?, ALIAS_NAME=? WHERE INTERFACE_NAME=? AND NODE_IP=?";
                preparedStatement = null;
                preparedStatement = connection_update.prepareStatement(sql);
                connection_update.setAutoCommit(false);

                for (int i = 0; i < NodeInfMonitoring.interfaceBandwidthUpdateListTemp.size(); i++) {
                    try {

                        double in_traffic = NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getIn_traffic();
                        double out_traffic = NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getOut_traffic();
                        String interface_name = NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getInterface_name();
                        String device_ip = NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getDevice_ip();

                        preparedStatement.setDouble(1, in_traffic);
                        preparedStatement.setDouble(2, out_traffic);
                        preparedStatement.setString(3, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getINTERFACE_INPUT_ERROR());
                        preparedStatement.setString(4, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getINTERFACE_OUTPUT_ERROR());
                        preparedStatement.setString(5, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getDISCARD_INPUT());
                        preparedStatement.setString(6, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getDISCARD_OUTPUT());
                        preparedStatement.setString(7, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getMTU());
                        preparedStatement.setString(8, NodeInfMonitoring.interfaceBandwidthUpdateListTemp.get(i).getALIAS_NAME());
                        preparedStatement.setString(9, interface_name);
                        preparedStatement.setString(10, device_ip);
                        preparedStatement.addBatch();
                    } catch (Exception e) {
                        System.out.println("update interfcae exception:" + e);
                    }
                }

                preparedStatement.executeBatch();
                connection_update.commit();
                //System.out.println("Upodae INTERFACE_MONITORING count:"+count.length);

            } catch (Exception exp) {
                System.out.println("Exception In Bandwidth Update:"+ exp);
               // logger.log(Level.SEVERE, "Exception In Bandwidth Update:", exp);
            } finally {
                try {

                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }

                    if (connection_update != null) {
                        connection_update.close();
                    }
                } catch (Exception e) {

                }
            }

//                    Connection connection = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//
//            connection = Datasource.getConnection();
//            preparedStatement = connection.prepareStatement("UPDATE INTERFACE_MONITORING SET INTERFACE_INPUT_ERROR=?,INTERFACE_OUTPUT_ERROR=?,DISCARD_INPUT=?,DISCARD_OUTPUT=?, MTU=?, ALIAS_NAME=? WHERE NODE_IP='" + device_ip + "' AND INTERFACE_NAME='" + interface_name + "'");
//            preparedStatement.setString(1, in_error_value);
//            preparedStatement.setString(2, out_error_value);
//            preparedStatement.setString(3, in_discard_value);
//            preparedStatement.setString(4, out_discard_value);
//            preparedStatement.setString(5, mtu);
//            preparedStatement.setString(6, portDescription);
//            preparedStatement.executeUpdate();
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Exception in updateInterfaceErrorsAndDiscard DeviceIP:{0} InterfaceName:{1} AdminStatus:{2} OperaStatus:{3}", new Object[]{device_ip, interface_name, e});
//        } finally {
//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (Exception e) {
//            }
//        }
            try {
                Thread.sleep(2000);
            } catch (Exception expp) {

            }

            int count5 = 0;
            NodeInfMonitoring.interfaceBandwidthInsertListTemp.clear();
            NodeInfMonitoring.interfaceBandwidthInsertListTemp.addAll(NodeInfMonitoring.interfaceBandwidthInsertList);
            NodeInfMonitoring.interfaceBandwidthInsertList.clear();

            Connection connection = null;
            try {
                connection = Datasource.getConnection();
                String sql1 = "INSERT INTO INTERFACE_BW_HISTORY (EVENT_TIMESTAMP,INTERFACE_NAME,IN_TRAFFIC,IP_INTERFACE,NODE_IP,OUT_TRAFFIC) VALUES (?,?,?,?,?,?)";
                String sql_status_bw = "INSERT INTO INTERFACE_BW_HISTORY_STATUS (device_ip,interface_name,interface_ip,interface_status,in_traffic,out_traffic,working_hour_flag,timestamp,timestamp_epoch,ip_interface_name,crc_value) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pStmt_bwhistory = null;
                pStmt_bwstatus = null;

                pStmt_bwhistory = connection.prepareStatement(sql1);
                pStmt_bwstatus = connection.prepareStatement(sql_status_bw);
                connection.setAutoCommit(false);

                for (int i = 0; i < NodeInfMonitoring.interfaceBandwidthInsertListTemp.size(); i++) {
                    count5 = count5 + 1;

                    double in_traffic = NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getIn_traffic();
                    double out_traffic = NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getOut_traffic();
                    String interface_name = NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getInterface_name();
                    String device_ip = NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getDevice_ip();
                    // String interface_ip = NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getInterface_ip();
                    String interface_ip = device_ip + "~" + interface_name;

                    try {

                        pStmt_bwhistory.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
                        pStmt_bwhistory.setString(2, interface_name);
                        pStmt_bwhistory.setDouble(3, in_traffic);
                        pStmt_bwhistory.setString(4, interface_ip);
                        pStmt_bwhistory.setString(5, device_ip);
                        pStmt_bwhistory.setDouble(6, out_traffic);
                        pStmt_bwhistory.addBatch();
                    } catch (Exception e) {
                        System.out.println("Exception:" + e);
                    }

                    //Working hours start
                    try {
                        pStmt_bwstatus.setString(1, device_ip);
                        pStmt_bwstatus.setString(2, interface_name);
                        pStmt_bwstatus.setString(3, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getInterface_ip());
                        pStmt_bwstatus.setString(4, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getInterface_status());
                        pStmt_bwstatus.setDouble(5, in_traffic);
                        pStmt_bwstatus.setDouble(6, out_traffic);
                        pStmt_bwstatus.setInt(7, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getWorkingHourFlag());
                        pStmt_bwstatus.setString(8, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getDatetime());
                        pStmt_bwstatus.setLong(9, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getEpochTimeL());
                        pStmt_bwstatus.setString(10, device_ip + "~" + interface_name);
                        pStmt_bwstatus.setDouble(11, NodeInfMonitoring.interfaceBandwidthInsertListTemp.get(i).getCrc_value());
                        pStmt_bwstatus.addBatch();
                    } catch (Exception e) {
                        System.out.println("Exception:" + e);
                    }
                    //working hours end

                    if (count5 % 1000 == 0) {
                        try {
                            pStmt_bwstatus.executeBatch();
                        } catch (Exception e) {
                            System.out.println("Exception:" + e);
                        }
                        try {
                            pStmt_bwhistory.executeBatch();
                        } catch (Exception e) {
                            System.out.println("Exception:" + e);
                        }

                        pStmt_bwhistory = null;
                        pStmt_bwhistory = connection.prepareStatement(sql1);
                        
                        pStmt_bwstatus = null;
                        pStmt_bwstatus = connection.prepareStatement(sql_status_bw);

                    }

                }

                //Remaining
                try {
                    pStmt_bwstatus.executeBatch();
                } catch (Exception e) {
                    System.out.println("Exception pStmt_bwstatus:" + e);
                }
                try {
                    pStmt_bwhistory.executeBatch();
                } catch (Exception e) {
                    System.out.println("Exception pStmt_bwhistory:" + e);
                }

                connection.commit();
            } catch (Exception e) {
                System.out.println("Exceptionnn:" + e);
//                logger.log(Level.SEVERE, "Exception In Bandwidth Insert:", e);
            } finally {
                try {

                    if (pStmt_bwhistory != null) {
                        pStmt_bwhistory.close();
                    }

                    if (pStmt_bwstatus != null) {
                        pStmt_bwstatus.close();
                    }

                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception ep) {
                }
            }

        }

    }

}
