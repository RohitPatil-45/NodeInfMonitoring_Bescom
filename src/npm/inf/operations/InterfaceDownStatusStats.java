/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.datasource.Datasource;
import npm.inf.dboperations.DatabaseHelper;

/**
 *
 * @author NPM
 */
public class InterfaceDownStatusStats {

    public static Logger logger = Logger.getLogger(InterfaceDownStatusStats.class.getName());

    public void insertStatusDiff(String device_ip, String interface_name, Timestamp up_event_time) {

        long totalsecofdate = 0;
        String difference_time = "";
        long diff = 0;
        long diffSeconds = 0;
        long diffMinutes = 0;
        long diffHours = 0;
        long diffDays = 0;
        Timestamp down_event_time = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Datasource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT EVENT_TIMESTAMP FROM INTERFACE_STATUS_HISTORY WHERE  NODE_IP ='" + device_ip + "' AND INTERFACE_STATUS='down' and INTERFACE_STATUS_TYPE='operational' ORDER BY ID DESC");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                down_event_time = resultSet.getTimestamp(1);
            }
        } catch (Exception a) {
            logger.log(Level.SEVERE, "Exception in get LastDownTime DeviceIP:{0} InterfaceName:{1} ExceptionString:{2} ", new Object[]{device_ip, interface_name, a});

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
            } catch (Exception exp) {
            }
        }

        if (down_event_time == null) {
            diff = up_event_time.getTime() - 0;
        } else {
            diff = up_event_time.getTime() - down_event_time.getTime();
        }

        diffSeconds = diff / 1000 % 60;
        diffMinutes = diff / (60 * 1000) % 60;
        diffHours = diff / (60 * 60 * 1000) % 24;
        diffDays = diff / (24 * 60 * 60 * 1000);
        difference_time = diffDays + " Days " + diffHours + " Hours " + diffMinutes + " Minutes " + diffSeconds + " Seconds.";
        long numa2 = diffDays * 86400;
        long numb2 = diffHours * 3600;
        long numc2 = diffMinutes * 60;
        long numd2 = diffSeconds;
        totalsecofdate = numa2 + numb2 + numc2 + numd2;

        logger.log(Level.INFO, "Interface UpEvent:{0} DownEvent:{1} Difference:{2} DeviceIP:{3} InteraceName:{4}", new Object[]{up_event_time, down_event_time, difference_time, device_ip, interface_name});
        try {
            DatabaseHelper helper = new DatabaseHelper();
            helper.insertInterfaceStatusTimeDiff(device_ip, interface_name, down_event_time, up_event_time, difference_time, totalsecofdate);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in insertInterfaceStatusTimeDiff DeviceIP:{0} InterfaceName:{1} ExceptionString:{2} ", new Object[]{device_ip, interface_name, e});
        }

    }

}
