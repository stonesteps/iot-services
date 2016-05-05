package com.bwg.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;


@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class GluuUser {


    private String userName;
    private GluuName name;
    private String displayName;
    private String password;

    public GluuUser(){};

    public GluuUser(User user) {
        GluuName gName = new GluuUser.GluuName();

        this.setUserName(user.getUsername());
        this.setDisplayName(user.getUsername());
        this.setPassword("helloKitty");

        gName.setGivenName(user.getFirstName());
        gName.setFamilyName(user.getLastName());
        this.setName(gName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GluuName getName() {
        return name;
    }

    public void setName(GluuName name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GluuUser)) return false;

        GluuUser gluuUser = (GluuUser) o;

        if (userName != null ? !userName.equals(gluuUser.userName) : gluuUser.userName != null) return false;
        if (name != null ? !name.equals(gluuUser.name) : gluuUser.name != null) return false;
        if (displayName != null ? !displayName.equals(gluuUser.displayName) : gluuUser.displayName != null)
            return false;
        return password != null ? password.equals(gluuUser.password) : gluuUser.password == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public class GluuName {
        private String givenName;
        private String familyName;
        private String middleName;
        private String honorificPrefix;
        private String honorificSuffix;

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

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getHonorificPrefix() {
            return honorificPrefix;
        }

        public void setHonorificPrefix(String honorificPrefix) {
            this.honorificPrefix = honorificPrefix;
        }

        public String getHonorificSuffix() {
            return honorificSuffix;
        }

        public void setHonorificSuffix(String honorificSuffix) {
            this.honorificSuffix = honorificSuffix;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GluuName)) return false;

            GluuName gluuName = (GluuName) o;

            if (givenName != null ? !givenName.equals(gluuName.givenName) : gluuName.givenName != null) return false;
            if (familyName != null ? !familyName.equals(gluuName.familyName) : gluuName.familyName != null)
                return false;
            if (middleName != null ? !middleName.equals(gluuName.middleName) : gluuName.middleName != null)
                return false;
            if (honorificPrefix != null ? !honorificPrefix.equals(gluuName.honorificPrefix) : gluuName.honorificPrefix != null)
                return false;
            return honorificSuffix != null ? honorificSuffix.equals(gluuName.honorificSuffix) : gluuName.honorificSuffix == null;

        }

        @Override
        public int hashCode() {
            int result = givenName != null ? givenName.hashCode() : 0;
            result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
            result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
            result = 31 * result + (honorificPrefix != null ? honorificPrefix.hashCode() : 0);
            result = 31 * result + (honorificSuffix != null ? honorificSuffix.hashCode() : 0);
            return result;
        }
    }
 }
