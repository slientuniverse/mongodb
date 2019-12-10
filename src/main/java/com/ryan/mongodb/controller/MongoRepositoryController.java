package com.ryan.mongodb.controller;

import com.google.gson.Gson;
import com.ryan.mongodb.entity.User;
import com.ryan.mongodb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ryan
 * @description 使用MongoRepository
 */
@RestController
@RequestMapping("/repository")
public class MongoRepositoryController {

    private final static Logger logger = LoggerFactory.getLogger(MongoRepositoryController.class);
    private static Gson gson = new Gson();

    @Autowired
    UserRepository userRepository;

    @GetMapping("/save")
    public String save(){
        // 单个插入
        User master = new User();
        master.setId(UUID.randomUUID().toString());
        master.setName("master");
        master.setAge(100);
        userRepository.save(master);

        // 批量插入
        List<User> list = new ArrayList<>();
        for (int i = 20; i <= 40; i++) {
            User salve = new User();
            salve.setId(UUID.randomUUID().toString());
            salve.setName("salve" + i);
            salve.setAge(i);
            list.add(salve);
        }
        userRepository.saveAll(list);
        return "ok";
    }

    @GetMapping("/delete")
    public String delete(){
        // 删除
        User user = new User();
        user.setId("652c8682-3681-4d7a-9982-2453e7b07762");
        user.setName("master");
        user.setAge(100);
        userRepository.delete(user);

        //根据id删除
        userRepository.deleteById("f6a208c0-4c2d-47c0-a959-93310bcc6819");
        return "ok";
    }

    @GetMapping("/query")
    public String query(){
        // 根据name查询
        User batch3 = userRepository.findByName("batch3");
        logger.info("根据name查询：" + gson.toJson(batch3));

        // 不显示age
        String name = userRepository.findNameById("b43419e4-847a-4787-aecd-bbb419404fb7");
        logger.info("不显示age：" + name);

        // 模糊查询
        List<User> list = userRepository.findByNameLike("batch");
        logger.info("模糊查询：" + gson.toJson(list));
        return "ok";
    }
}
