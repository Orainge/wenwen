package com.orainge.wenwen.mybatis.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer userId;

    private String email;

    private String username;

    private String password;

    private String nickname;

    private String avatarUrl;

    private Date createTime;

    private Date lastLoginTime;

    private Integer userStatus;

    private Integer gender;

    private String simpleDescription;

    private String address;

    private String industry;

    private String career;

    private String education;

    private String fullDescription;

    private Integer countFollowing;

    private Integer countFollower;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSimpleDescription() {
        return simpleDescription;
    }

    public void setSimpleDescription(String simpleDescription) {
        this.simpleDescription = simpleDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Integer getCountFollowing() {
        return countFollowing;
    }

    public void setCountFollowing(Integer countFollowing) {
        this.countFollowing = countFollowing;
    }

    public Integer getCountFollower() {
        return countFollower;
    }

    public void setCountFollower(Integer countFollower) {
        this.countFollower = countFollower;
    }
}