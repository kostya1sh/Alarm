package com.test.alarm.entities;

import java.util.Date;

/**
 * Created by kostya on 26.09.2016.
 */

public class AlarmEntity {
    private Long id;
    private Date date;
    private String msg;
    private Integer activated;
    private Integer type;
    private Integer difficult;

    public String print() {
        return "date: " + date +
                "\n msg: " + msg +
                "\n activated: " + activated +
                "\n type: " + type +
                "\n difficult: " + difficult;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
