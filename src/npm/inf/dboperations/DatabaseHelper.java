/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.datasource.Datasource;

/**
 *
 * @author NPM
 */
public class DatabaseHelper {

    public static Logger logger = Logger.getLogger(DatabaseHelper.class.getName());

    public void updateInterfaceStatus(String device_ip, String interface_name, String admin_status, String operational_status, Timestamp timestamp) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE INTERFACE_MONITORING SET ADMIN_STATUS=?,OPER_STATUS=?,STATUS_TIMESTAMP=? WHERE NODE_IP='" + device_ip + "' AND INTERFACE_NAME='" + interface_name + "'");
            preparedStatement.setString(1, admin_status);
            preparedStatement.setString(2, operational_status);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in updateInterfaceStatus DeviceIP:{0} InterfaceName:{1} AdminStatus:{2} OperaStatus:{3} ExceptionString:{4}", new Object[]{device_ip, interface_name, admin_status, operational_status, e});
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

    }

    public void insertInterfaceStatus(String device_ip, String interface_name, String interfaceStatus, String InterfaceStatusType, String interfaceIP, Timestamp timestamp) {

        interfaceIP = device_ip + "~" + interface_name;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO INTERFACE_STATUS_HISTORY (EVENT_TIMESTAMP,INTERFACE_NAME,INTERFACE_STATUS,INTERFACE_STATUS_TYPE,IP_INTERFACE,NODE_IP) VALUES (?,?,?,?,?,?)");
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setString(2, interface_name);
            preparedStatement.setString(3, interfaceStatus);
            preparedStatement.setString(4, InterfaceStatusType);
            preparedStatement.setString(5, interfaceIP);
            preparedStatement.setString(6, device_ip);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in insertInterfaceStatus DeviceIP:{0} InterfaceName:{1} interfaceStatus:{2} InterfaceStatusType:{3} ExceptionString:{4}", new Object[]{device_ip, interface_name, interfaceStatus, InterfaceStatusType, e});
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

    }

    public void insertInterfaceStatusTimeDiff(String device_ip, String interface_name, Timestamp downTimestamp, Timestamp upTimestamp, String timeDiff, long totalNoOfSec) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO INTERFACE_STATUS_DIFF (DOWN_EVENT_TIMESTAMP,INTERFACE_NAME,NODE_IP,TIME_DIFF,TOTAL_SEC,UP_EVENT_TIMESTAMP) VALUES (?,?,?,?,?,?)");
            preparedStatement.setTimestamp(1, downTimestamp);
            preparedStatement.setString(2, interface_name);
            preparedStatement.setString(3, device_ip);
            preparedStatement.setString(4, timeDiff);
            preparedStatement.setLong(5, totalNoOfSec);
            preparedStatement.setTimestamp(6, upTimestamp);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in insertInterfaceStatusTimeDiff DeviceIP:{0} InterfaceName:{1} ExceptionString:{2}", new Object[]{device_ip, interface_name, e});
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

    }

    public void updateInterfaceBWStatus(String device_ip, String interface_name, String bw_status, Timestamp timestamp, String bw_type) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE INTERFACE_MONITORING SET BW_STATUS=?,BW_TIMESTAMP=?,BW_TYPE=? WHERE NODE_IP='" + device_ip + "' AND INTERFACE_NAME='" + interface_name + "'");
            preparedStatement.setString(1, bw_status);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setString(3, bw_type);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in updateInterfaceBWStatus DeviceIP:{0} InterfaceName:{1} AdminStatus:{2} OperaStatus:{3}", new Object[]{device_ip, interface_name, bw_status, e});
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

    }

    public void insertInterfaceThresholdBWStatus(String device_ip, String interface_name, double currentBW, double BWThreshold, String BWStatus, String interfaceIP, Timestamp timestamp, String bw_type) {

        interfaceIP = device_ip + "~" + interface_name;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO BW_THRESHOLD_HISTORY (BW_STATUS,BW_THRESHOLD,BW_VAL,EVENT_TIMESTAMP,INTERFACE_NAME,IP_INTERFACE,NODE_IP,BW_TYPE) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, BWStatus);
            preparedStatement.setDouble(2, BWThreshold);
            preparedStatement.setDouble(3, currentBW);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setString(5, interface_name);
            preparedStatement.setString(6, interfaceIP);
            preparedStatement.setString(7, device_ip);
            preparedStatement.setString(8, bw_type);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in insertInterfaceThresholdBWStatus DeviceIP:{0} InterfaceName:{1} ExceptionString:{2}", new Object[]{device_ip, interface_name, e});
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

    }

    public void updateInterfaceErrorsAndDiscard(String device_ip, String interface_name, String in_error_value, String out_error_value, String in_discard_value, String out_discard_value, Timestamp timestamp, String mtu, String portDescription) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE INTERFACE_MONITORING SET INTERFACE_INPUT_ERROR=?,INTERFACE_OUTPUT_ERROR=?,DISCARD_INPUT=?,DISCARD_OUTPUT=?, MTU=?, ALIAS_NAME=? WHERE NODE_IP='" + device_ip + "' AND INTERFACE_NAME='" + interface_name + "'");
            preparedStatement.setString(1, in_error_value);
            preparedStatement.setString(2, out_error_value);
            preparedStatement.setString(3, in_discard_value);
            preparedStatement.setString(4, out_discard_value);
            preparedStatement.setString(5, mtu);
            preparedStatement.setString(6, portDescription);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in updateInterfaceErrorsAndDiscard DeviceIP:{0} InterfaceName:{1} AdminStatus:{2} OperaStatus:{3}", new Object[]{device_ip, interface_name, e});
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
    }

  


}
