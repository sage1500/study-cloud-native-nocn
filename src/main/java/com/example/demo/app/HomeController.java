package com.example.demo.app;

import com.example.demo.domain.service.HelloService;
import com.example.demo.domain.service.TodoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final HelloService helloService;
    private final TodoService todoService;

    @GetMapping
    public String home(Model model) {
        log.debug("[HOME]index");

        String userId = "user1";

        // サービス呼出し
        var hello = helloService.hello(userId);
        var todoList = todoService.findAllByUserId(userId);

        // 結果をビューに反映
        model.addAttribute("hello", hello);
        model.addAttribute("todoList", todoList);

        return "home";
    }

}
