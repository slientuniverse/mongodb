package com.ryan.mongodb.controller;

import com.google.gson.Gson;
import com.ryan.mongodb.entity.Magic;
import com.ryan.mongodb.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author ryan
 * @description mongodb聚合函数
 */
@RestController
@RequestMapping("/aggregation")
public class AggregationController {

    private final static Logger logger = LoggerFactory.getLogger(AggregationController.class);

    private static Gson gson = new Gson();

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/count")
    public String count(){
        List<Magic> list = mongoTemplate.findAll(Magic.class);
        if (list.isEmpty()){
            for (int i = 0; i < 20; i++) {
                Magic magic = new Magic();
                magic.setId(UUID.randomUUID().toString());
                if (i % 2 == 0){
                    magic.setRabbit("martin");
                    magic.setPigeon("ego");
                    magic.setHat("red");
                    magic.setBox("glass");
                    magic.setChain("icon");
                } else {
                    magic.setRabbit("carl");
                    magic.setPigeon("andy");
                    magic.setHat("gray");
                    magic.setBox("carbon");
                    magic.setChain("steel");
                }
                mongoTemplate.insert(magic);
                magic = new Magic();
                if (i % 3 == 0){
                    magic.setRabbit("martin");
                    magic.setPigeon("ego");
                    magic.setHat("red");
                    magic.setBox("glass");
                    magic.setChain("icon");
                } else {
                    magic.setRabbit("carl");
                    magic.setPigeon("andy");
                    magic.setHat("gray");
                    magic.setBox("carbon");
                    magic.setChain("steel");
                }
                mongoTemplate.insert(magic);
            }
        }
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(Criteria.where("chain").is("steel")));
        operations.add(Aggregation.group("rabbit").count().as("rabbit"));
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Magic> results = mongoTemplate.aggregate(aggregation, "magic", Magic.class);

        logger.info(aggregation.toString());
        logger.info("result: " + gson.toJson(results));
        return "ok";
    }

    @GetMapping("/sum")
    public String sum(){
        Pattern pattern= Pattern.compile("^.*"+ "batch" +".*$", Pattern.CASE_INSENSITIVE);

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(Criteria.where("name").regex(pattern)));
        operations.add(Aggregation.group("name").sum("age").as("age_total"));
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, "user", User.class);
        logger.info(aggregation.toString());
        logger.info("result: " + gson.toJson(results));
        return "ok";
    }
}
