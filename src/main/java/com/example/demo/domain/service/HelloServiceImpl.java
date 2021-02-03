package com.example.demo.domain.service;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String userId) {
        return "Hello " + userId;
    }

}
