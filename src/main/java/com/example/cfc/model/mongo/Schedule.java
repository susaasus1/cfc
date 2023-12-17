package com.example.cfc.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "schedule")
public class Schedule {

    @Id
    private Long id;

    private ArrayList<org.bson.Document> data;

    public Schedule() {
    }

    public Schedule(Long id, ArrayList<org.bson.Document> data) {
        this.id = id;
        this.data = data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ArrayList<org.bson.Document> getData() {
        return data;
    }

    public void setData(ArrayList<org.bson.Document> data) {
        this.data = data;
    }

}