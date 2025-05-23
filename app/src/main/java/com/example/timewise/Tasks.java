package com.example.timewise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tasks implements Serializable {
    //attributes
    private String task;//task name
    private String description;//optional description
    private String time; // the needed time to complete the task

    private String deadline;
    private int image;
    private boolean isdone;



    //default constructor
    public Tasks() {
    }

    //constructor
    public Tasks(String task, String description, String time, String deadline) {
        this.task = task;
        this.description = description;
        this.time = time;
        this.image = image;
        this.deadline = deadline;

    }
    //getters
    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getImage() {
        return image;
    }

    public boolean getIsdone() {
        return isdone;
    }

    //setters
    public void setTask(String task) {
        this.task = task;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setIsdone(boolean isDone) {
        this.isdone = isDone;
    }

}
