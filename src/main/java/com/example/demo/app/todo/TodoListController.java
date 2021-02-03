package com.example.demo.app.todo;

import com.example.demo.domain.service.TodoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoListController {
    private final TodoService todoService;

    @GetMapping("/")
    public String index(Model model) {
        log.debug("[TODO]index");

        String userId = "user1";

        // サービス呼出し
        var list = todoService.findAllByUserId(userId);

        // 結果をビューに反映
        model.addAttribute("list", list);
        return "todo/todoList";
    }

}
