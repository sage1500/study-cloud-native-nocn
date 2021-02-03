package com.example.demo.app.todo;

import java.util.Locale;

import javax.validation.groups.Default;

import com.example.demo.domain.entity.Todo;
import com.example.demo.domain.service.TodoService;
import com.github.dozermapper.core.Mapper;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/todo/create")
@SessionAttributes({ "todoForm", "message" })
@RequiredArgsConstructor
@Slf4j
public class TodoCreateController {
    private final TodoService todoService;
    private final MessageSource messageSource;
    private final Mapper dozerMapper;

    @ModelAttribute("todoForm")
    public TodoForm setupTodoForm() {
        return new TodoForm();
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String index(SessionStatus sessionStatus) {
        log.debug("[TODO-CREATE]index");

        // URL直接入力されると、セッションが残ったままの可能性があるため、
        // セッションから削除して、入力画面にリダイレクト
        sessionStatus.setComplete();
        return "redirect:input";
    }

    @RequestMapping(value = "input", method = { RequestMethod.GET, RequestMethod.POST })
    public String input(TodoForm todoForm) {
        log.debug("[TODO-CREATE]input: {}", todoForm);
        return "todo/todoCreateInput";
    }

    @PostMapping("confirm")
    public String confirm(@Validated({ Default.class, TodoForm.TodoCreate.class }) TodoForm todoForm,
            BindingResult bindingResult) {
        log.debug("[TODO-CREATE]confirm: {}", todoForm);

        if (bindingResult.hasErrors()) {
            return input(todoForm);
        }

        return "todo/todoCreateConfirm";
    }

    @PostMapping("execute")
    public String execute(TodoForm todoForm, Model model) {
        log.debug("[TODO-CREATE]execute: {}", todoForm);

        String userId = "user1";

        // TodoForm を TodoResource に変換
        Todo todo = dozerMapper.map(todoForm, Todo.class);
        log.debug("[TODO-CREATE]execute : todo={}", todo);

        // サービス実行
        todo.setUserId(userId);
        Todo result = todoService.save(todo);
        log.debug("[TODO-CREATE]todo created: {}", result);

        // API呼出し結果を TodoForm に反映
        dozerMapper.map(result, todoForm);

        // 完了画面にリダイレクト
        String msg = messageSource.getMessage("message.todo.create.success", null, Locale.ROOT);
        model.addAttribute("message", msg);
        return "redirect:complete";
    }

    @GetMapping("complete")
    public String complete(SessionStatus sessionStatus, @ModelAttribute("message") String message, Model model) {
        log.debug("[TODO-CREATE]complete: message={}", message);
        sessionStatus.setComplete();
        return "todo/todoCreateComplete";
    }

    @GetMapping("cancel")
    public String cancel(SessionStatus sessionStatus) {
        log.debug("[TODO-CREATE]cancel");
        sessionStatus.setComplete();
        return "redirect:/todo/";
    }
}
