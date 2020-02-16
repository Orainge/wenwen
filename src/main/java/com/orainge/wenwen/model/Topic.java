package com.orainge.wenwen.model;

public class Topic {
    private Integer topicId;

    private Integer praentTopicId;

    private String topicName;

    private String topicDescription;

    private String topicImage;

    private Integer countQuestion;

    private Integer countFoucs;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getPraentTopicId() {
        return praentTopicId;
    }

    public void setPraentTopicId(Integer praentTopicId) {
        this.praentTopicId = praentTopicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName == null ? null : topicName.trim();
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription == null ? null : topicDescription.trim();
    }

    public String getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(String topicImage) {
        this.topicImage = topicImage == null ? null : topicImage.trim();
    }

    public Integer getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(Integer countQuestion) {
        this.countQuestion = countQuestion;
    }

    public Integer getCountFoucs() {
        return countFoucs;
    }

    public void setCountFoucs(Integer countFoucs) {
        this.countFoucs = countFoucs;
    }
}