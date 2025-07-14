/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import npm.inf.properties.AppConfig;

/**
 *
 * @author NPM
 */
public class Datasource {

//    public static Connection getOracleConnection() {
//
//        Connection con = null;
//        try {
//
//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//            OracleDataSource ods = null;
//            ods = new OracleDataSource();
//            ods.setUser(AppConfig.DATABASE_USERNAME);
//            ods.setPassword(AppConfig.DATABASE_PASSWORD);
//            ods.setURL("jdbc:oracle:thin:@" + AppConfig.DATABASE_SERVER + ":" + AppConfig.DATABASE_PORT + ":xe");
//            con = ods.getConnection();
//        } catch (Exception e) {
//            System.out.println("Exception:" + e);
//        }
//
//        return con;
//
//    }
    public static Connection getMySqlConnection() {

        Connection con = null;

        // JDBC URL for MySQL 8
        
        //String jdbcURL = "jdbc:mysql://localhost:9007/npm?useSSL=false";
        String jdbcURL = "jdbc:mysql://localhost:9007/bescom?useSSL=false&rewriteBatchedStatements=true";
        
       
        String username = "root";
        String password = "Syst3m4$";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            con = DriverManager.getConnection(jdbcURL, username, password);
        } catch (Exception e) {
            System.out.println("Excep DB Connection" + e);
        }
//        try {
//
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            MysqlDataSource mds = null;
//            mds = new MysqlDataSource();
//            mds.setUser(AppConfig.DATABASE_USERNAME);
//            mds.setPassword(AppConfig.DATABASE_PASSWORD);
//            mds.setURL("jdbc:mysql://" + AppConfig.DATABASE_SERVER + ":" + AppConfig.DATABASE_PORT + "/" + AppConfig.DATABASE_SCHEMA + "");
//            con = mds.getConnection();
//        } catch (Exception e) {
//            System.out.println("Exception:" + e);
//        }

        return con;

    }

    public static Connection getConnection() {

        Connection con = null;
        try {

          //  if (AppConfig.DATABASE_TYPE != null) {
            //  if (AppConfig.DATABASE_TYPE.equalsIgnoreCase("mysql")) {
            con = getMySqlConnection();
//                } else if (AppConfig.DATABASE_TYPE.equalsIgnoreCase("oracle")) {
//                    con = getOracleConnection();
//                }

           // }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }

        return con;

    }

}
