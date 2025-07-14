/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

import java.sql.Timestamp;

/**
 *
 * @author NPM
 */
public class InterfaceCRCInfo {

    private String device_ip;
    private String interface_name;
    private String interface_ip;
    private int new_crc_value;
    private int old_crc_value;
    private Timestamp event_timestamp;

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

    public String getInterface_ip() {
        return interface_ip;
    }

    public void setInterface_ip(String interface_ip) {
        this.interface_ip = interface_ip;
    }

    public int getNew_crc_value() {
        return new_crc_value;
    }

    public void setNew_crc_value(int new_crc_value) {
        this.new_crc_value = new_crc_value;
    }

    public int getOld_crc_value() {
        return old_crc_value;
    }

    public void setOld_crc_value(int old_crc_value) {
        this.old_crc_value = old_crc_value;
    }

    public Timestamp getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(Timestamp event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    @Override
    public String toString() {
        return "InterfaceCRCInfo{" + "device_ip=" + device_ip + ", interface_name=" + interface_name + ", interface_ip=" + interface_ip + ", new_crc_value=" + new_crc_value + ", old_crc_value=" + old_crc_value + ", event_timestamp=" + event_timestamp + '}';
    }

}
