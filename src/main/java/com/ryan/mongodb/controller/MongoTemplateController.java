package com.ryan.mongodb.controller;

import com.google.gson.Gson;
import com.mongodb.WriteResult;
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
        ryan1.setAge("25");
        mongoTemplate.insert(ryan1);

        // save 方法 主键存在的时候会更新数据 saveOrUpdate
        ryan1.setName("ryan2");
        mongoTemplate.save(ryan1);

        // insertAll 批量插入
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName("batch" + i);
            user.setAge(i + "");
            list.add(user);
        }
        mongoTemplate.insertAll(list);

        return "ok";
    }

    @GetMapping("/delete")
    public String delete(){
        // delet 删除
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is("ryan1");
        query.addCriteria(criteria);
        mongoTemplate.remove(query);
        return "ok";
    }

    @GetMapping("/query")
    public String query(){
        // 单条件查询
        Pattern pattern= Pattern.compile("^.*"+ "ryan" +".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("name").regex(pattern);

        Query query = new Query();
        query.fields().include("name");
        query.fields().include("sex");
        query.addCriteria(criteria);
        List<User> list = mongoTemplate.find(query, User.class);
        logger.info(gson.toJson(list));
        return gson.toJson(list);
    }

    @GetMapping("/update")
    public String update(){
        Update update = new Update();
        update.set("age", "30");
        update.set("sex", "1");
        String[] strings = {"1", "2", "3"};
        update.addToSet("info", strings);
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is("ryan");
        query.addCriteria(criteria);

        mongoTemplate.updateMulti(query, update, User.class);
        return "ok";
    }
}
