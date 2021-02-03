package com.example.demo.app.todo;

import java.util.Locale;

import com.example.demo.domain.entity.Todo;
import com.example.demo.domain.service.TodoService;
import com.github.dozermapper.core.Mapper;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/todo/delete")
@SessionAttributes({ "todoForm", "message" })
@RequiredArgsConstructor
@Slf4j
public class TodoDeleteController {
    private final TodoService todoService;
    private final MessageSource messageSource;
    private final Mapper dozerMapper;

    @ModelAttribute("todoForm")
    public TodoForm setupTodoForm() {
        return new TodoForm();
    }

    @PostMapping("/")
    public String index(TodoForm todoForm) {
        log.debug("[TODO-DELETE]index: {}", todoForm);

        String userId = "user1";

        // サービス呼出し
        Todo todo = todoService.findByUserIdAndTodoId(userId, todoForm.getTodoId());

        // 結果を TodoForm に反映
        dozerMapper.map(todo, todoForm);
        return "todo/todoDeleteConfirm";
    }

    @PostMapping("execute")
    public String execute(TodoForm todoForm, Model model) {
        log.debug("[TODO-DELETE]execute: {}", todoForm);

        String userId = "user1";

        // サービス呼出し
        boolean result = todoService.deleteByUserIdAndTodoId(userId, todoForm.getTodoId());

        log.debug("[TODO-DELETE]result: {}", result);
        String msgKey = (result) ? "message.todo.delete.success" : "message.todo.delete.success-deleted";
        String msg = messageSource.getMessage(msgKey, null, Locale.ROOT);
        model.addAttribute("message", msg);
        return "redirect:complete";
    }

    @GetMapping("complete")
    public String complete(SessionStatus sessionStatus, @ModelAttribute("message") String message, Model model) {
        log.debug("[TODO-DELETE]complete: message={}", message);
        sessionStatus.setComplete();
        return "todo/todoDeleteComplete";
    }

    @GetMapping("cancel")
    public String cancel(SessionStatus sessionStatus) {
        log.debug("[TODO-DELETE]cancel");
        sessionStatus.setComplete();
        return "redirect:/todo/";
    }

}
