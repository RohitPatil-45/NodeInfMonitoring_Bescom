/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.pojo;

/**
 *
 * @author NPM
 */
public class SNMPConfigMaster {

    private String name;
    private String description;
    private int port;
    private int retries;
    private int timeout;
    private String version;
    private String community;
    private String context;
    private String username;
    private String authentication_protocol;
    private String authentication_password;
    private String encryption_protocol;
    private String encryption_password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthentication_protocol() {
        return authentication_protocol;
    }

    public void setAuthentication_protocol(String authentication_protocol) {
        this.authentication_protocol = authentication_protocol;
    }

    public String getAuthentication_password() {
        return authentication_password;
    }

    public void setAuthentication_password(String authentication_password) {
        this.authentication_password = authentication_password;
    }

    public String getEncryption_protocol() {
        return encryption_protocol;
    }

    public void setEncryption_protocol(String encryption_protocol) {
        this.encryption_protocol = encryption_protocol;
    }

    public String getEncryption_password() {
        return encryption_password;
    }

    public void setEncryption_password(String encryption_password) {
        this.encryption_password = encryption_password;
    }

    @Override
    public String toString() {
        return "SNMPConfigMaster{" + "name=" + name + ", description=" + description + ", port=" + port + ", retries=" + retries + ", timeout=" + timeout + ", version=" + version + ", community=" + community + ", context=" + context + ", username=" + username + ", authentication_protocol=" + authentication_protocol + ", authentication_password=" + authentication_password + ", encryption_protocol=" + encryption_protocol + ", encryption_password=" + encryption_password + '}';
    }

}
