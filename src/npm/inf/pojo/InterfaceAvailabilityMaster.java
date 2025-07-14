/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
public class InterfaceAvailabilityMaster {

    String router_ip = null;
    String cdate = null;
    String uptime_sec = null;
    String uptime_per = null;
    String downtime_sec = null;
    String downtime_per = null;
    String customer_name = null;
    String zone_name = null;
    String state_name = null;
    String district_name = null;
    String branch_name = null;

    public String getRouter_ip() {
        return router_ip;
    }

    public void setRouter_ip(String router_ip) {
        this.router_ip = router_ip;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getUptime_sec() {
        return uptime_sec;
    }

    public void setUptime_sec(String uptime_sec) {
        this.uptime_sec = uptime_sec;
    }

    public String getUptime_per() {
        return uptime_per;
    }

    public void setUptime_per(String uptime_per) {
        this.uptime_per = uptime_per;
    }

    public String getDowntime_sec() {
        return downtime_sec;
    }

    public void setDowntime_sec(String downtime_sec) {
        this.downtime_sec = downtime_sec;
    }

    public String getDowntime_per() {
        return downtime_per;
    }

    public void setDowntime_per(String downtime_per) {
        this.downtime_per = downtime_per;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    @Override
    public String toString() {
        return "InterfaceAvailabilityMaster{" + "router_ip=" + router_ip + ", cdate=" + cdate + ", uptime_sec=" + uptime_sec + ", uptime_per=" + uptime_per + ", downtime_sec=" + downtime_sec + ", downtime_per=" + downtime_per + ", customer_name=" + customer_name + ", zone_name=" + zone_name + ", state_name=" + state_name + ", district_name=" + district_name + ", branch_name=" + branch_name + '}';
    }

}
