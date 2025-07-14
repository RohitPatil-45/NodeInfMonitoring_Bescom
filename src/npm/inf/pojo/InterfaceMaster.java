/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
public class InterfaceMaster {

    String router_ip = null;
    String router_name = null;
    String branch_name = null;
    String current_bandwidth = null;
    String cdate = null;
    String ctime = null;
    String interface_name = null;
    String interface_outOctate = null;
    String interfaceinOctate = null;
    String ssa = null;
    String zone_name = null;
    String customer_name = null;
    String customer_sname = null;
    String district = null;
    String department = null;
    String state_name = null;
    String last_change_time = null;
    String crc_value = null;
    String crc_counter = null;
    String int_input_drop = null;
    String int_output_drop = null;
    int send_percentage = 0;
    int receive_percentage = 0;

    public String getRouter_ip() {
        return router_ip;
    }

    public void setRouter_ip(String router_ip) {
        this.router_ip = router_ip;
    }

    public String getRouter_name() {
        return router_name;
    }

    public void setRouter_name(String router_name) {
        this.router_name = router_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getCurrent_bandwidth() {
        return current_bandwidth;
    }

    public void setCurrent_bandwidth(String current_bandwidth) {
        this.current_bandwidth = current_bandwidth;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }

    public String getInterface_outOctate() {
        return interface_outOctate;
    }

    public void setInterface_outOctate(String interface_outOctate) {
        this.interface_outOctate = interface_outOctate;
    }

    public String getInterfaceinOctate() {
        return interfaceinOctate;
    }

    public void setInterfaceinOctate(String interfaceinOctate) {
        this.interfaceinOctate = interfaceinOctate;
    }

    public String getSsa() {
        return ssa;
    }

    public void setSsa(String ssa) {
        this.ssa = ssa;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_sname() {
        return customer_sname;
    }

    public void setCustomer_sname(String customer_sname) {
        this.customer_sname = customer_sname;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getLast_change_time() {
        return last_change_time;
    }

    public void setLast_change_time(String last_change_time) {
        this.last_change_time = last_change_time;
    }

    public String getCrc_value() {
        return crc_value;
    }

    public void setCrc_value(String crc_value) {
        this.crc_value = crc_value;
    }

    public String getCrc_counter() {
        return crc_counter;
    }

    public void setCrc_counter(String crc_counter) {
        this.crc_counter = crc_counter;
    }

    public String getInt_input_drop() {
        return int_input_drop;
    }

    public void setInt_input_drop(String int_input_drop) {
        this.int_input_drop = int_input_drop;
    }

    public String getInt_output_drop() {
        return int_output_drop;
    }

    public void setInt_output_drop(String int_output_drop) {
        this.int_output_drop = int_output_drop;
    }

    public int getSend_percentage() {
        return send_percentage;
    }

    public void setSend_percentage(int send_percentage) {
        this.send_percentage = send_percentage;
    }

    public int getReceive_percentage() {
        return receive_percentage;
    }

    public void setReceive_percentage(int receive_percentage) {
        this.receive_percentage = receive_percentage;
    }

    @Override
    public String toString() {
        return "InterfaceMaster{" + "router_ip=" + router_ip + ", router_name=" + router_name + ", branch_name=" + branch_name + ", current_bandwidth=" + current_bandwidth + ", cdate=" + cdate + ", ctime=" + ctime + ", interface_name=" + interface_name + ", interface_outOctate=" + interface_outOctate + ", interfaceinOctate=" + interfaceinOctate + ", ssa=" + ssa + ", zone_name=" + zone_name + ", customer_name=" + customer_name + ", customer_sname=" + customer_sname + ", district=" + district + ", department=" + department + ", state_name=" + state_name + ", last_change_time=" + last_change_time + ", crc_value=" + crc_value + ", crc_counter=" + crc_counter + ", int_input_drop=" + int_input_drop + ", int_output_drop=" + int_output_drop + ", send_percentage=" + send_percentage + ", receive_percentage=" + receive_percentage + '}';
    }
}
