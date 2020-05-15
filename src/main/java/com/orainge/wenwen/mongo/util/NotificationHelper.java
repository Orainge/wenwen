package com.orainge.wenwen.mongo.util;

import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.entity.Notification;
import com.orainge.wenwen.mongo.entity.NotificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationHelper {
    public static final Integer DEFAULT_PAGE_SIZE = 10; //默认分页大小

    @Autowired
    private NotificationDao notificationDao;

    public List<NotificationResponseData> getFeed(Integer userId, Integer nowPage) {
        List<Notification> list = notificationDao.getFeed(userId, DEFAULT_PAGE_SIZE, nowPage);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<NotificationResponseData> result = new ArrayList<NotificationResponseData>();
        for (Notification m : list) {
            NotificationData notificationData = m.getData();
            NotificationResponseData n = new NotificationResponseData();
            List<Object> l = new ArrayList<Object>();
            if (m.getIs_follow_question() == 0) {
                l.add(m.getUser_id());
                l.add(m.getUsername());
                l.add(notificationData.getQ_id());
                l.add(notificationData.getTitle());
                if (m.getType() == 0 && m.getS_type() == 0) {
                    n.setType(0);
                } else if (m.getType() == 0 && m.getS_type() == 1) {
                    l.add(notificationData.getA_id());
                    n.setType(1);
                } else if (m.getType() == 2 && m.getS_type() == 2) {
                    n.setType(2);
                } else if (m.getType() == 1 && m.getS_type() == 1) {
                    l.add(notificationData.getA_id());
                    n.setType(3);
                }
            } else {
                l.add(notificationData.getQ_id());
                l.add(notificationData.getTitle());
                l.add(notificationData.getA_id());
                n.setType(4);
            }
            n.setParam(l);
            result.add(n);
        }
        return result;
    }

    public List<NotificationResponseData> getMessage(Integer userId, Integer nowPage) {
        List<Notification> list = notificationDao.getMessage(userId, DEFAULT_PAGE_SIZE, nowPage);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<NotificationResponseData> result = new ArrayList<NotificationResponseData>();
        for (Notification m : list) {
            NotificationData notificationData = m.getData();
            NotificationResponseData n = new NotificationResponseData();
            List<Object> l = new ArrayList<Object>();
            l.add(m.getUser_id());
            l.add(m.getUsername());
            if (m.getType() == 0) {
                l.add(notificationData.getQ_id());
                l.add(notificationData.getTitle());
                n.setType(1);
                if (m.getS_type() == 1) {
                    n.setType(2);
                    n.setSubType(0);
                    l.add(notificationData.getA_id());
                } else if (m.getS_type() == 2) {
                    l.add(notificationData.getQc_id());
                    n.setSubType(0);
                } else if (m.getS_type() == 3) {
                    l.add(notificationData.getAc_id());
                    n.setSubType(1);
                } else if (m.getS_type() == 4) {
                    l.add(notificationData.getQc_id());
                    n.setSubType(2);
                } else if (m.getS_type() == 5) {
                    l.add(notificationData.getAc_id());
                    n.setSubType(3);
                }
            } else if (m.getType() == 2) {
                n.setType(0);
                if (m.getS_type() == 0) {
                    n.setSubType(0);
                } else if (m.getS_type() == 2) {
                    l.add(notificationData.getQ_id());
                    l.add(notificationData.getTitle());
                    l.add(notificationData.getA_id());
                    n.setSubType(2);
                }
            }
            n.setParam(l);
            result.add(n);
        }
        return result;
    }

    public List<NotificationResponseData> getLike(Integer userId, Integer nowPage) {
        List<Notification> list = notificationDao.getLike(userId, DEFAULT_PAGE_SIZE, nowPage);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<NotificationResponseData> result = new ArrayList<NotificationResponseData>();
        for (Notification m : list) {
            NotificationData notificationData = m.getData();
            NotificationResponseData n = new NotificationResponseData();
            List<Object> l = new ArrayList<Object>();
            l.add(m.getUser_id());
            l.add(m.getUsername());
            l.add(notificationData.getQ_id());
            l.add(notificationData.getTitle());
            if (m.getType() == 1 && m.getS_type() == 0) {
                n.setType(0);
            } else if (m.getType() == 1 && m.getS_type() == 1) {
                l.add(notificationData.getA_id());
                n.setType(1);
            } else if (m.getType() == 1 && m.getS_type() == 2) {
                l.add(notificationData.getQc_id());
                n.setType(2);
            } else if (m.getType() == 1 && m.getS_type() == 3) {
                l.add(notificationData.getAc_id());
                n.setType(3);
            }
            n.setParam(l);
            result.add(n);
        }
        return result;
    }
}
