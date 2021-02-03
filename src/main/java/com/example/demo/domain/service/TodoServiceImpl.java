package com.example.demo.domain.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.domain.entity.Todo;
import com.example.demo.domain.repository.TodoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Transactional
    @Override
    public Todo save(Todo entity) {
        // 追加時はIDを割り振る
        if (entity.getTodoId() == null) {
            entity.setTodoId(UUID.randomUUID().toString());
            entity.setVersion(0);
        }

        return todoRepository.save(entity);
    }

    @Override
    public List<Todo> findAllByUserId(String userId) {
        return todoRepository.findAllByUserId(userId);
    }

    @Override
    public Todo findByUserIdAndTodoId(String userId, String todoId) {
        return todoRepository.findByUserIdAndTodoId(userId, todoId);
    }

    @Transactional
    @Override
    public boolean deleteByUserIdAndTodoId(String userId, String todoId) {
        // todoRepository.deleteById(todoId);
        // return true;
        return todoRepository.deleteByUserIdAndTodoId(userId, todoId) > 0;
    }

}
