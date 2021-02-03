package com.example.demo.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

//import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import lombok.Data;

@Entity
@Data
public class Todo {
    private String userId;
    @org.springframework.data.annotation.Id
    @Id
    private String todoId;
    private String todoTitle;
    @Version
    private long version;
}
