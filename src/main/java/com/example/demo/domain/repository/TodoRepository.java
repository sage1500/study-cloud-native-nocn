package com.example.demo.domain.repository;

import java.util.List;

import com.example.demo.domain.entity.Todo;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, String> {
    List<Todo> findAllByUserId(String userId);

    Todo findByUserIdAndTodoId(String userId, String todoId);

    long deleteByUserIdAndTodoId(String userId, String todoId);
}
