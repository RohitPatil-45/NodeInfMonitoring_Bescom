/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.datasource.Datasource;
import npm.inf.init.NodeInfMonitoring;

/**
 *
 * @author NPM
 */
public class CRCBatchProcessingSensor implements Runnable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement preparedStatement2 = null;
    ResultSet resultSet = null;
    String sql = null;
    public static Logger logger = Logger.getLogger(CRCBatchProcessingSensor.class.getName());

    @Override
    public void run() {

        while (true) {

            try {
                Thread.sleep(80000);
            } catch (Exception e) {

            }

            NodeInfMonitoring.interfaceCRCUpdateListTemp.clear();
            NodeInfMonitoring.interfaceCRCUpdateListTemp.addAll(NodeInfMonitoring.interfaceCRCUpdateList);
            NodeInfMonitoring.interfaceCRCUpdateList.clear();

            try {
                connection = Datasource.getConnection();
                sql = "UPDATE INTERFACE_MONITORING SET CRC_ERROR=? WHERE INTERFACE_NAME=? AND NODE_IP=?";
                preparedStatement = null;
                preparedStatement = connection.prepareStatement(sql);

                for (int i = 0; i < NodeInfMonitoring.interfaceCRCUpdateListTemp.size(); i++) {

                    int crc_val = NodeInfMonitoring.interfaceCRCUpdateListTemp.get(i).getNew_crc_value();
                    String interface_name = NodeInfMonitoring.interfaceCRCUpdateListTemp.get(i).getInterface_name();
                    String device_ip = NodeInfMonitoring.interfaceCRCUpdateListTemp.get(i).getDevice_ip();

                    preparedStatement.setInt(1, crc_val);
                    preparedStatement.setString(3, interface_name);
                    preparedStatement.setString(4, device_ip);
                    preparedStatement.addBatch();
                }

                int[] count = preparedStatement.executeBatch();

            } catch (Exception exp) {
                logger.log(Level.SEVERE, "Exception In CRC Update:", exp);
            } finally {
                try {

                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }

                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {

                }
            }

            try {
                Thread.sleep(2000);
            } catch (Exception expp) {

            }

            NodeInfMonitoring.interfaceCRCInsertListTemp.clear();
            NodeInfMonitoring.interfaceCRCInsertListTemp.addAll(NodeInfMonitoring.interfaceCRCInsertList);
            NodeInfMonitoring.interfaceCRCInsertList.clear();

            try {
                connection = Datasource.getConnection();
                sql = "INSERT INTO INTERFACE_CRC_HISTORY (EVENT_TIMESTAMP,INTERFACE_NAME,IP_INTERFACE,NEW_CRC_VAL,NODE_IP,OLD_CRC_VAL) VALUES (?,?,?,?,?,?)";

                preparedStatement = null;
                preparedStatement = connection.prepareStatement(sql);

                for (int i = 0; i < NodeInfMonitoring.interfaceCRCInsertListTemp.size(); i++) {

                    int new_crc_val = NodeInfMonitoring.interfaceCRCInsertListTemp.get(i).getNew_crc_value();
                    int old_crc_val = NodeInfMonitoring.interfaceCRCInsertListTemp.get(i).getOld_crc_value();
                    String interface_name = NodeInfMonitoring.interfaceCRCInsertListTemp.get(i).getInterface_name();
                    String device_ip = NodeInfMonitoring.interfaceCRCInsertListTemp.get(i).getDevice_ip();
                    //    String interface_ip = NodeInfMonitoring.interfaceCRCInsertListTemp.get(i).getInterface_ip();
                    String interface_ip = device_ip + "~" + interface_name;

                    preparedStatement.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setString(2, interface_name);
                    preparedStatement.setString(4, interface_ip);
                    preparedStatement.setInt(3, new_crc_val);
                    preparedStatement.setString(5, device_ip);
                    preparedStatement.setInt(6, old_crc_val);

                    preparedStatement.addBatch();

                }

                int[] count2 = preparedStatement.executeBatch();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception In CRC Insert:", e);
            } finally {
                try {

                    if (preparedStatement != null) {
                        preparedStatement.close();
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
