package io.pivotal.samples.dashboard.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Client {
    @Id
    @Column(length=40)
    @GeneratedValue(generator="randomId")
    @GenericGenerator(name="randomId", strategy="io.pivotal.samples.dashboard.domain.RandomIdGenerator")
    private String id;

    private String givenName;
    private String familyName;
    private String birthday;
    private String imageUrl;
    private String address;
    private String clientId;

    public Client() {
    }

    public Client(String givenName, String familyName, String birthday, String imageUrl, String address) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
