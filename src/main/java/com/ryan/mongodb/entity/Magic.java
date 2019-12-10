package com.ryan.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ryan
 */
@Document(collection = "magic")
public class Magic {

    @Id
    private String id;
    private String rabbit;
    private String pigeon;
    private String hat;
    private String box;
    private String chain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRabbit() {
        return rabbit;
    }

    public void setRabbit(String rabbit) {
        this.rabbit = rabbit;
    }

    public String getPigeon() {
        return pigeon;
    }

    public void setPigeon(String pigeon) {
        this.pigeon = pigeon;
    }

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }
}
