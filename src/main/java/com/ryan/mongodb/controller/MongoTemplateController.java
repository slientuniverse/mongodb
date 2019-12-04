package com.ryan.mongodb.controller;

import com.google.gson.Gson;
import com.mongodb.WriteResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ryan.mongodb.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author hyt
 */
@RestController
@RequestMapping("/crud")
public class MongoTemplateController {

    private final static Logger logger = LoggerFactory.getLogger(MongoTemplateController.class);
    private static Gson gson = new Gson();

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/save")
    public String save(){
        // insert 方法 有主键冲突
        User ryan1 = new User();
        ryan1.setId(UUID.randomUUID().toString());
        ryan1.setName("ryan1");
        ryan1.setAge(25);
        mongoTemplate.insert(ryan1);

        // save 方法 主键存在的时候会更新数据 saveOrUpdate
        ryan1.setName("ryan2");
        mongoTemplate.save(ryan1);

        // insertAll 批量插入
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName("batch" + i);
            user.setAge(i);
            list.add(user);
        }
        mongoTemplate.insertAll(list);

        return "ok";
    }

    @GetMapping("/delete")
    public String delete(){
        // delete 删除
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is("ryan2");
        query.addCriteria(criteria);
        DeleteResult result = mongoTemplate.remove(query, User.class);
        logger.info("普通查询：" + gson.toJson(result));
        return "ok";
    }

    @GetMapping("/query")
    public String query(){
        // 普通查询 查询返回第一个
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("batch0"));
        User user = mongoTemplate.findOne(query, User.class);
        logger.info("普通查询：" + gson.toJson(user));

        // 模糊查询
        query = new Query();
        // 不显示name
        query.fields().exclude("name");
        // 显示name 不可以同时用 include和exclude
//        query.fields().include("age");

        Pattern pattern= Pattern.compile("^.*"+ "batch" +".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("name").regex(pattern));
        List<User> list = mongoTemplate.find(query, User.class);
        logger.info("模糊查询：" + gson.toJson(list));

        // 分页查询 skip:从哪开始 limit:从开始数多少条 gt lt gte let 大于 小于 大于等于 小于等于
        query = new Query();
        query.addCriteria(Criteria.where("age").gte(5)).skip(5).limit(5);
        list = mongoTemplate.find(query, User.class);
        logger.info("分页查询：" + gson.toJson(list));

        // 多条件查询 andOperator = and  orOperator = or
        query = new Query();
        query.addCriteria(Criteria.where("age").gt(5).lt(10).orOperator(Criteria.where("name").is("batch7"), Criteria.where("name").is("batch8"), Criteria.where("name").is("batch9")));
        list = mongoTemplate.find(query, User.class);
        logger.info("多条件查询：" + gson.toJson(list));

        // 查询整个集合
        list = mongoTemplate.findAll(User.class);
        logger.info("查询集合：" + gson.toJson(list));

        // 返回删除的数据 mongoTemplate.findAndRemove

        return gson.toJson(list);
    }

    @GetMapping("/update")
    public String update(){
        // 普通更新 更新查询的第一条
        Update update = new Update();
        update.set("age", "100");
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("batch1"));
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        logger.info("普通更新：" + gson.toJson(result));

        // 批量更新
        update = new Update();
        update.set("age", "30");
        // set不存在的字段 可以插入数据
        update.set("address", "shanghai");
        query = new Query();
        Criteria criteria = Criteria.where("age").gt(5).lt(10);
        query.addCriteria(criteria);
        result = mongoTemplate.updateMulti(query, update, User.class);
        logger.info("批量更新：" + gson.toJson(result));

        return "ok";
    }
}
