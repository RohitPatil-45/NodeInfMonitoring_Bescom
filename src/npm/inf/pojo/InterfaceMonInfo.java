/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
public class InterfaceMonInfo {

    private String device_ip;
    private String device_type;
    private String deviceName;
    private String interface_name;
    private String interface_id;
    private String alias_name;
    private String admin_status;
    private String operational_status;
    private double in_traffic;
    private double out_traffic;
    private String crc_error;
    private String discard_input_packet;
    private String discard_output_packet;
    private String interface_errors;
    private String auto_ticketing_param;
    private String bw_history_param;
    private double bw_threshold;
    private String crc_history_param;
    private String mail_alert_param;
    private String sms_alert_param;
    private double in_bw_percent;
    private double out_bw_percent;
    private double procured_bw;
    private String interface_ip;

    public String getInterface_ip() {
        return interface_ip;
    }

    public void setInterface_ip(String interface_ip) {
        this.interface_ip = interface_ip;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    
    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }

    public String getInterface_id() {
        return interface_id;
    }

    public void setInterface_id(String interface_id) {
        this.interface_id = interface_id;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
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

    public double getIn_traffic() {
        return in_traffic;
    }

    public void setIn_traffic(double in_traffic) {
        this.in_traffic = in_traffic;
    }

    public double getOut_traffic() {
        return out_traffic;
    }

    public void setOut_traffic(double out_traffic) {
        this.out_traffic = out_traffic;
    }

    public String getCrc_error() {
        return crc_error;
    }

    public void setCrc_error(String crc_error) {
        this.crc_error = crc_error;
    }

    public String getDiscard_input_packet() {
        return discard_input_packet;
    }

    public void setDiscard_input_packet(String discard_input_packet) {
        this.discard_input_packet = discard_input_packet;
    }

    public String getDiscard_output_packet() {
        return discard_output_packet;
    }

    public void setDiscard_output_packet(String discard_output_packet) {
        this.discard_output_packet = discard_output_packet;
    }

    public String getInterface_errors() {
        return interface_errors;
    }

    public void setInterface_errors(String interface_errors) {
        this.interface_errors = interface_errors;
    }

    public String getAuto_ticketing_param() {
        return auto_ticketing_param;
    }

    public void setAuto_ticketing_param(String auto_ticketing_param) {
        this.auto_ticketing_param = auto_ticketing_param;
    }

    public String getBw_history_param() {
        return bw_history_param;
    }

    public void setBw_history_param(String bw_history_param) {
        this.bw_history_param = bw_history_param;
    }

    public double getBw_threshold() {
        return bw_threshold;
    }

    public void setBw_threshold(double bw_threshold) {
        this.bw_threshold = bw_threshold;
    }

    public String getCrc_history_param() {
        return crc_history_param;
    }

    public void setCrc_history_param(String crc_history_param) {
        this.crc_history_param = crc_history_param;
    }

    public String getMail_alert_param() {
        return mail_alert_param;
    }

    public void setMail_alert_param(String mail_alert_param) {
        this.mail_alert_param = mail_alert_param;
    }

    public String getSms_alert_param() {
        return sms_alert_param;
    }

    public void setSms_alert_param(String sms_alert_param) {
        this.sms_alert_param = sms_alert_param;
    }

    public double getIn_bw_percent() {
        return in_bw_percent;
    }

    public void setIn_bw_percent(double in_bw_percent) {
        this.in_bw_percent = in_bw_percent;
    }

    public double getOut_bw_percent() {
        return out_bw_percent;
    }

    public void setOut_bw_percent(double out_bw_percent) {
        this.out_bw_percent = out_bw_percent;
    }

    public double getProcured_bw() {
        return procured_bw;
    }

    public void setProcured_bw(double procured_bw) {
        this.procured_bw = procured_bw;
    }

    @Override
    public String toString() {
        return "InterfaceMonInfo{" + "device_ip=" + device_ip + ", interface_name=" + interface_name + ", interface_id=" + interface_id + ", alias_name=" + alias_name + ", admin_status=" + admin_status + ", operational_status=" + operational_status + ", in_traffic=" + in_traffic + ", out_traffic=" + out_traffic + ", crc_error=" + crc_error + ", discard_input_packet=" + discard_input_packet + ", discard_output_packet=" + discard_output_packet + ", interface_errors=" + interface_errors + ", auto_ticketing_param=" + auto_ticketing_param + ", bw_history_param=" + bw_history_param + ", bw_threshold=" + bw_threshold + ", crc_history_param=" + crc_history_param + ", mail_alert_param=" + mail_alert_param + ", sms_alert_param=" + sms_alert_param + ", in_bw_percent=" + in_bw_percent + ", out_bw_percent=" + out_bw_percent + ", procured_bw=" + procured_bw + '}';
    }

}
