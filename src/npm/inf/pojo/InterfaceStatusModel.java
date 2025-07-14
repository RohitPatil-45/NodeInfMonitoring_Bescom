/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npm.inf.pojo;

import java.sql.Timestamp;

/**
 *
 * @author testsys
 */
public class InterfaceStatusModel {

  
    String device_ip;
    String interface_name;
    String interface_type;
    String admin_status;
    String operational_status;
    Timestamp eventTime;

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }

    public String getInterface_type() {
        return interface_type;
    }

    public void setInterface_type(String interface_type) {
        this.interface_type = interface_type;
    }

    public String getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(String admin_status) {
        this.admin_status = admin_status;
    }

    public String getOperational_status() {
        return operational_status;
    }

    public void setOperational_status(String operational_status) {
        this.operational_status = operational_status;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }
    
    

    
}
