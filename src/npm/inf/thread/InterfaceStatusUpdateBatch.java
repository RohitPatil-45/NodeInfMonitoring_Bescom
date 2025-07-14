/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npm.inf.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import npm.inf.datasource.Datasource;
import npm.inf.init.NodeInfMonitoring;

/**
 *
 * @author testsys
 */
public class InterfaceStatusUpdateBatch implements Runnable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement preparedStatement2 = null;
    ResultSet resultSet = null;
    String sql = null;

    public void run() {
        System.out.println("Start LatencyUpdate");
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (Exception expp) {
                System.out.println("Exception In Thread Sleep BatchUpdate :" + expp);
            }

            try {
                NodeInfMonitoring.tempStatusUpdateInterfaceList.clear();
                NodeInfMonitoring.tempStatusUpdateInterfaceList.addAll(NodeInfMonitoring.statusUpdateInterfaceList);
                NodeInfMonitoring.statusUpdateInterfaceList.clear();
            } catch (Exception e) {
                System.out.println("Excpetion in try catch packet drop=" + e);
            }

            try {
                connection = Datasource.getConnection();

                sql = "UPDATE INTERFACE_MONITORING SET ADMIN_STATUS=?,OPER_STATUS=?,STATUS_TIMESTAMP=? WHERE NODE_IP=? AND INTERFACE_NAME=?";
                preparedStatement = connection.prepareStatement(sql);

                String sql2 = "INSERT INTO INTERFACE_STATUS_HISTORY (EVENT_TIMESTAMP,INTERFACE_NAME,INTERFACE_STATUS,INTERFACE_STATUS_TYPE,IP_INTERFACE,NODE_IP) VALUES (?,?,?,?,?,?)";
                preparedStatement2 = connection.prepareStatement(sql2);

                connection.setAutoCommit(false);
                for (int i = 0; i < NodeInfMonitoring.tempStatusUpdateInterfaceList.size(); i++) {
                    try {
                        preparedStatement.setString(1, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getAdmin_status());
                        preparedStatement.setString(2, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getOperational_status());
                        preparedStatement.setTimestamp(3, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getEventTime());
                        preparedStatement.setString(4, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getDevice_ip());
                        preparedStatement.setString(5, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_name());
                        preparedStatement.addBatch();
                        System.out.println("updated IP :" + NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getDevice_ip() + ":" + NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_name());

                        String interfaceIP = NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getDevice_ip() + "~" + NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_name();
                        String interfaceStatus = null;
                        if (NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_type().equals("admin")) {
                            interfaceStatus = NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getAdmin_status();
                        } else {
                            interfaceStatus = NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getOperational_status();
                        }

                        preparedStatement2.setTimestamp(1, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getEventTime());
                        preparedStatement2.setString(2, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_name());
                        preparedStatement2.setString(3, interfaceStatus);
                        preparedStatement2.setString(4, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getInterface_type());
                        preparedStatement2.setString(5, interfaceIP);
                        preparedStatement2.setString(6, NodeInfMonitoring.tempStatusUpdateInterfaceList.get(i).getDevice_ip());
                        preparedStatement2.addBatch();
                        // System.out.println("updated values  :" + NodeStatusLatencyMonitoring.latency_update_temp.get(i).getAvg_response());
//                        if (i == 1000) {
//                            int[] count = preparedStatement.executeBatch();
//
//                            System.out.println("UPDATE Interface Status " + count.length);
//                            preparedStatement = null;
//                            preparedStatement = connection.prepareStatement(sql);
//                        }

                        //System.out.println(dateFormat.format(in_startdate));
                    } catch (Exception e) {
                        System.out.println("Exception in packet drop=" + e);
                    }
                }
                int[] count = preparedStatement.executeBatch();
                int[] count2 = preparedStatement2.executeBatch();
                connection.commit();

                System.out.println("##UPDATE Interface status count: " + count.length+":"+count2.length);
            } catch (Exception exp) {
                System.out.println("Exception batch update interface status:" + exp);
            } finally {
                try {

                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception ep) {
                    System.out.println("Exception1111Insweertr in update==== " + ep);
                }
            }

        }

    }
}
