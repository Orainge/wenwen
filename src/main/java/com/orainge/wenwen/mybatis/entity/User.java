package com.orainge.wenwen.mybatis.entity;

import java.util.Date;

public class User {
    private Integer userId;

    private String email;

    private String username;

    private String password;

    private String avatarUrl;

    private Date createTime;

    private Date lastLoginTime;

    private Integer userStatus;

    private Integer gender;

    private String sinpleDescirption;

    private String address;

    private String industry;

    private String career;

    private String education;

    private String fullDescirption;

    private Integer countLike;

    private Integer countCollect;

    private Integer countFollowing;

    private Integer countFollower;

    private Integer countBrowse;

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
        this.email = email == null ? null : email.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
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

    public String getSinpleDescirption() {
        return sinpleDescirption;
    }

    public void setSinpleDescirption(String sinpleDescirption) {
        this.sinpleDescirption = sinpleDescirption == null ? null : sinpleDescirption.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career == null ? null : career.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getFullDescirption() {
        return fullDescirption;
    }

    public void setFullDescirption(String fullDescirption) {
        this.fullDescirption = fullDescirption == null ? null : fullDescirption.trim();
    }

    public Integer getCountLike() {
        return countLike;
    }

    public void setCountLike(Integer countLike) {
        this.countLike = countLike;
    }

    public Integer getCountCollect() {
        return countCollect;
    }

    public void setCountCollect(Integer countCollect) {
        this.countCollect = countCollect;
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

    public Integer getCountBrowse() {
        return countBrowse;
    }

    public void setCountBrowse(Integer countBrowse) {
        this.countBrowse = countBrowse;
    }
}