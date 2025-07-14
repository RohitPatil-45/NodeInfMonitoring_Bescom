/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
public class InterfaceParamMaster {

    private String device_ip;
    private String interface_name;
    private int procured_bandwidth;
    private String bandwidth_history_param;
    private int bandwidth_threshold;
    private String crc_history;
    private String mail_alert;
    private String sms_alert;
    private String auto_ticketing;

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

    public int getProcured_bandwidth() {
        return procured_bandwidth;
    }

    public void setProcured_bandwidth(int procured_bandwidth) {
        this.procured_bandwidth = procured_bandwidth;
    }

    public String getBandwidth_history_param() {
        return bandwidth_history_param;
    }

    public void setBandwidth_history_param(String bandwidth_history_param) {
        this.bandwidth_history_param = bandwidth_history_param;
    }

    public int getBandwidth_threshold() {
        return bandwidth_threshold;
    }

    public void setBandwidth_threshold(int bandwidth_threshold) {
        this.bandwidth_threshold = bandwidth_threshold;
    }

    public String getCrc_history() {
        return crc_history;
    }

    public void setCrc_history(String crc_history) {
        this.crc_history = crc_history;
    }

    public String getMail_alert() {
        return mail_alert;
    }

    public void setMail_alert(String mail_alert) {
        this.mail_alert = mail_alert;
    }

    public String getSms_alert() {
        return sms_alert;
    }

    public void setSms_alert(String sms_alert) {
        this.sms_alert = sms_alert;
    }

    public String getAuto_ticketing() {
        return auto_ticketing;
    }

    public void setAuto_ticketing(String auto_ticketing) {
        this.auto_ticketing = auto_ticketing;
    }

    @Override
    public String toString() {
        return "InterfaceParamMaster{" + "device_ip=" + device_ip + ", interface_name=" + interface_name + ", procured_bandwidth=" + procured_bandwidth + ", bandwidth_history_param=" + bandwidth_history_param + ", bandwidth_threshold=" + bandwidth_threshold + ", crc_history=" + crc_history + ", mail_alert=" + mail_alert + ", sms_alert=" + sms_alert + ", auto_ticketing=" + auto_ticketing + '}';
    }

}
