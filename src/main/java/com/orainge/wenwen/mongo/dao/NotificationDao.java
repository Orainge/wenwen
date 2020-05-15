package com.orainge.wenwen.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.orainge.wenwen.mongo.entity.Follow;
import com.orainge.wenwen.mongo.entity.Notification;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NotificationDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
    private final String COLLECTION_NAME = "notification";
    private final Integer IS_CANCEL = 1;

    @SuppressWarnings("all")
    public List<Notification> getFeed(Integer userId, Integer pageSize, Integer nowPage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        query.fields().include("follow_user_list");
        query.fields().include("follow_question_list");
        Follow follow = mongoTemplate.findOne(query, Follow.class, "follow");
        if (follow == null) {
            return null;
        }
        List<Integer> list1 = follow.getFollow_user_list();
        List<Integer> list2 = follow.getFollow_question_list();
        List<Integer> followUserIdList = new ArrayList<Integer>();
        List<Integer> followQuestionIdList = new ArrayList<Integer>();
        if (list1 != null) {
            followUserIdList = list1;
        }
        if (list2 != null) {
            followQuestionIdList = list2;
        }

        Criteria cIn1 = Criteria.where("user_id").in(followUserIdList);
        Criteria cIn2 = Criteria.where("is_cancel").ne(IS_CANCEL);
        Criteria c11 = new Criteria().andOperator(Criteria.where("type").is(0), new Criteria().orOperator(Criteria.where("s_type").is(0), Criteria.where("s_type").is(1)));
        Criteria c12 = new Criteria().andOperator(Criteria.where("type").is(1), Criteria.where("s_type").is(1));
        Criteria c13 = new Criteria().andOperator(Criteria.where("type").is(2), Criteria.where("s_type").is(2));
        Criteria criteria1 = new Criteria().andOperator(cIn1, cIn2, new Criteria().orOperator(c11, c12, c13));

        Criteria cIn3 = Criteria.where("data.q_id").in(followQuestionIdList);
        Criteria c21 = new Criteria().andOperator(Criteria.where("type").is(0), Criteria.where("s_type").is(1));
        Criteria criteria2 = new Criteria().andOperator(cIn3, c21);

        Criteria criteria = new Criteria().orOperator(criteria1, criteria2);

        ConditionalOperators.Cond condOperation = ConditionalOperators.when(
                new Criteria().andOperator(Criteria.where("data.q_id").in(followQuestionIdList), Criteria.where("type").is(0), Criteria.where("s_type").is(1))
        ).then(1).otherwise(0);

        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("_id", "user_id", "username", "at_user_id", "at_username", "time", "type", "s_type", "data").and(condOperation).as("is_follow_question"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "time", "_id")),
                Aggregation.skip((nowPage - 1) * pageSize),
                Aggregation.limit(pageSize)
        );

        return findAggregateList(customerAgg, COLLECTION_NAME, Notification.class);
    }

    public List<Notification> getMessage(Integer userId, Integer pageSize, Integer nowPage) {
        Criteria c1 = Criteria.where("at_user_id").is(userId);
        Criteria c2 = new Criteria().andOperator(Criteria.where("type").is(0), new Criteria().orOperator(Criteria.where("s_type").is(1), Criteria.where("s_type").is(2), Criteria.where("s_type").is(3), Criteria.where("s_type").is(4), Criteria.where("s_type").is(5)));
        Criteria c3 = new Criteria().andOperator(Criteria.where("type").is(2), new Criteria().orOperator(Criteria.where("s_type").is(0), Criteria.where("s_type").is(2)));
        Criteria criteria = new Criteria().andOperator(c1, new Criteria().orOperator(c2, c3));
        Criteria c4 = Criteria.where("is_cancel").ne(IS_CANCEL);
        return get(userId, pageSize, nowPage, criteria, c4);
    }

    public List<Notification> getLike(Integer userId, Integer pageSize, Integer nowPage) {
        Criteria c1 = Criteria.where("at_user_id").is(userId);
        Criteria c2 = Criteria.where("type").is(1);
        Criteria c3 = new Criteria().orOperator(Criteria.where("s_type").is(1), Criteria.where("s_type").is(2), Criteria.where("s_type").is(3));
        Criteria c4 = Criteria.where("is_cancel").ne(IS_CANCEL);
        return get(userId, pageSize, nowPage, c1, c2, c3, c4);
    }

    private List<Notification> get(Integer userId, Integer pageSize, Integer nowPage, Criteria... criteria) {
        List<Notification> likeList = null;
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("time")));
        query.with(Sort.by(Sort.Order.desc("_id")));
        for (Criteria c : criteria) {
            query.addCriteria(c);
        }
        if (nowPage == 1) {
            query.limit(pageSize);
            likeList = mongoTemplate.find(query, Notification.class, COLLECTION_NAME);
        } else {
            query.limit((nowPage - 1) * pageSize);
            List<Notification> tempList = mongoTemplate.find(query, Notification.class, COLLECTION_NAME);
            if (tempList == null || tempList.size() == 0) {
                return null;
            }
            Notification lastOne = tempList.get(tempList.size() - 1); //取出最后一条，当作条件查接下来的数据
            Date lastTime = lastOne.getTime();
            query.addCriteria(Criteria.where("time").lt(lastTime)); //从上一页最后一条开始查（小于不包括这一条）
            query.limit(pageSize);//页大小重新赋值，覆盖 number 参数
            likeList = mongoTemplate.find(query, Notification.class, COLLECTION_NAME);
        }
        return likeList;
    }

    @SuppressWarnings("all")
    public <T> List<T> findAggregateList(Aggregation aggregation, String collectionName, Class<T> clazz) {
        AggregationResults<T> aggregate = this.mongoTemplate.aggregate(aggregation, collectionName, clazz);
        List<T> customerDetails = aggregate.getMappedResults();
        return customerDetails;
    }

    public void saveMessage(Integer userId, String nickname, NotificationType type, NotificationData data) {
        saveMessage(userId, nickname, null, null, type, data);
    }

    public void saveMessage(Integer userId, String nickname, NotificationType type) {
        saveMessage(userId, nickname, null, null, type, null);
    }

    public void saveMessage(Integer userId, String nickname, Integer atUserId, String atNickname, NotificationType type) {
        saveMessage(userId, nickname, atUserId, atNickname, type, null);
    }

    public void saveMessage(Integer userId, String nickname, Integer atUserId, String atNickname, NotificationType type, NotificationData data) {
        Notification notification = new Notification();
        if (userId == null) {
            userId = -1;
        }
        if (nickname == null) {
            nickname = "匿名用户";
        }
        notification.setUser_id(userId);
        notification.setUsername(nickname);
        notification.setTime(new Date());
        notification.setType(type.getType());
        notification.setS_type(type.getSubType());
        if (atUserId != null) {
            notification.setAt_user_id(atUserId);
        }
        if (atNickname != null) {
            notification.setAt_username(atNickname);
        }
        if (data != null) {
            notification.setData(data);
        }
        mongoTemplate.save(notification);
    }


    public void deleteMessage(Integer userId, NotificationType type) {
        deleteMessage(userId, null, type, null);
    }

    public void deleteMessage(Integer userId, NotificationType type, NotificationData data) {
        deleteMessage(userId, null, type, data);
    }


    public void deleteMessage(Integer userId, Integer atUserId, NotificationType type) {
        deleteMessage(userId, atUserId, type, null);
    }

    public void deleteMessage(Integer userId, Integer atUserId, NotificationType type, NotificationData data) {
        Query query = new Query();
        if (userId != null)
            query.addCriteria(Criteria.where("user_id").is(userId));

        if (atUserId != null) {
            query.addCriteria(Criteria.where("at_user_id").is(atUserId));
        }

        query.addCriteria(Criteria.where("type").is(type.getType()));
        query.addCriteria(Criteria.where("s_type").is(type.getSubType()));

        if (data != null) {
            Integer q_id = data.getQ_id();
            Integer a_id = data.getA_id();
            Integer qc_id = data.getQc_id();
            Integer ac_id = data.getAc_id();
            String title = data.getTitle();

            if (q_id != null) {
                query.addCriteria(Criteria.where("data.q_id").is(q_id));
            }
            if (a_id != null) {
                query.addCriteria(Criteria.where("data.a_id").is(a_id));
            }
            if (qc_id != null) {
                query.addCriteria(Criteria.where("data.qc_id").is(qc_id));
            }
            if (ac_id != null) {
                query.addCriteria(Criteria.where("data.ac_id").is(ac_id));
            }
            if (title != null) {
                query.addCriteria(Criteria.where("data.title").is(title));
            }
        }
        query.addCriteria(Criteria.where("is_cancel").ne(1));
        Update update = new Update().set("is_cancel", 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        if (updateResult.getModifiedCount() != 1) {
            deleteMessageInNullUserId(atUserId, type, data);
        }
    }

    private void deleteMessageInNullUserId(Integer atUserId, NotificationType type, NotificationData data) {
        Query query = new Query();

        query.addCriteria(Criteria.where("user_id").is(-1));

        if (atUserId != null) {
            query.addCriteria(Criteria.where("at_user_id").is(atUserId));
        }

        query.addCriteria(Criteria.where("type").is(type.getType()));
        query.addCriteria(Criteria.where("s_type").is(type.getSubType()));

        if (data != null) {
            Integer q_id = data.getQ_id();
            Integer a_id = data.getA_id();
            Integer qc_id = data.getQc_id();
            Integer ac_id = data.getAc_id();
            String title = data.getTitle();

            if (q_id != null) {
                query.addCriteria(Criteria.where("data.q_id").is(q_id));
            }
            if (a_id != null) {
                query.addCriteria(Criteria.where("data.a_id").is(a_id));
            }
            if (qc_id != null) {
                query.addCriteria(Criteria.where("data.qc_id").is(qc_id));
            }
            if (ac_id != null) {
                query.addCriteria(Criteria.where("data.ac_id").is(ac_id));
            }
            if (title != null) {
                query.addCriteria(Criteria.where("data.title").is(title));
            }
        }
        query.addCriteria(Criteria.where("is_cancel").ne(1));
        Update update = new Update().set("is_cancel", 1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }
}
