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
public class InterfaceBandwidthInfo {

    private String device_ip;
    private String interface_name;
    private String interface_ip;
    private double out_traffic;
    private double in_traffic;
    private double crc_value;
    private Timestamp event_timestamp;
    int workingHourFlag = 0;
    String datetime = null;
    long epochTimeL = 0;
    private String interface_status;

    private String INTERFACE_INPUT_ERROR;
    private String INTERFACE_OUTPUT_ERROR;
    private String DISCARD_INPUT;
    private String DISCARD_OUTPUT;
    private String MTU;
    private String ALIAS_NAME;

    public String getINTERFACE_INPUT_ERROR() {
        return INTERFACE_INPUT_ERROR;
    }

    public void setINTERFACE_INPUT_ERROR(String INTERFACE_INPUT_ERROR) {
        this.INTERFACE_INPUT_ERROR = INTERFACE_INPUT_ERROR;
    }

    public String getINTERFACE_OUTPUT_ERROR() {
        return INTERFACE_OUTPUT_ERROR;
    }

    public void setINTERFACE_OUTPUT_ERROR(String INTERFACE_OUTPUT_ERROR) {
        this.INTERFACE_OUTPUT_ERROR = INTERFACE_OUTPUT_ERROR;
    }

    public String getDISCARD_INPUT() {
        return DISCARD_INPUT;
    }

    public void setDISCARD_INPUT(String DISCARD_INPUT) {
        this.DISCARD_INPUT = DISCARD_INPUT;
    }

    public String getDISCARD_OUTPUT() {
        return DISCARD_OUTPUT;
    }

    public void setDISCARD_OUTPUT(String DISCARD_OUTPUT) {
        this.DISCARD_OUTPUT = DISCARD_OUTPUT;
    }

    public String getMTU() {
        return MTU;
    }

    public void setMTU(String MTU) {
        this.MTU = MTU;
    }

    public String getALIAS_NAME() {
        return ALIAS_NAME;
    }

    public void setALIAS_NAME(String ALIAS_NAME) {
        this.ALIAS_NAME = ALIAS_NAME;
    }

    public double getCrc_value() {
        return crc_value;
    }

    public void setCrc_value(double crc_value) {
        this.crc_value = crc_value;
    }

    public String getInterface_status() {
        return interface_status;
    }

    public void setInterface_status(String interface_status) {
        this.interface_status = interface_status;
    }

    public int getWorkingHourFlag() {
        return workingHourFlag;
    }

    public void setWorkingHourFlag(int workingHourFlag) {
        this.workingHourFlag = workingHourFlag;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getEpochTimeL() {
        return epochTimeL;
    }

    public void setEpochTimeL(long epochTimeL) {
        this.epochTimeL = epochTimeL;
    }

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

    public double getOut_traffic() {
        return out_traffic;
    }

    public void setOut_traffic(double out_traffic) {
        this.out_traffic = out_traffic;
    }

    public double getIn_traffic() {
        return in_traffic;
    }

    public void setIn_traffic(double in_traffic) {
        this.in_traffic = in_traffic;
    }

    public Timestamp getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(Timestamp event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    @Override
    public String toString() {
        return "InterfaceBandwidthInfo{" + "device_ip=" + device_ip + ", interface_name=" + interface_name + ", interface_ip=" + interface_ip + ", out_traffic=" + out_traffic + ", in_traffic=" + in_traffic + ", event_timestamp=" + event_timestamp + '}';
    }

}
