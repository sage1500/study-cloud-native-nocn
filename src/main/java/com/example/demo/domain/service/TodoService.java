package com.example.demo.domain.service;

import java.util.List;

import com.example.demo.domain.entity.Todo;

public interface TodoService {
    Todo save(Todo entity);
    List<Todo> findAllByUserId(String userId);
    Todo findByUserIdAndTodoId(String userId, String todoId);
    boolean deleteByUserIdAndTodoId(String userId, String todoId);
}
