package OnlineTicket;

import java.io.Serializable;

public class Person implements Serializable {
    private String tc;
    private String name;
    private String sureName;

    public String getTc() {
        return tc;
    }

    public Person setTc(String tc) {
        this.tc = tc;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getSureName() {
        return sureName;
    }

    public Person setSurename(String sureName) {
        this.sureName = sureName;
        return this;
    }
}
