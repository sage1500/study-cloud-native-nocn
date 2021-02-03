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
@RequestMapping("/todo/update")
@SessionAttributes({ "todoForm", "message" })
@RequiredArgsConstructor
@Slf4j
public class TodoUpdateController {
    private final TodoService todoService;
    private final MessageSource messageSource;
    private final Mapper dozerMapper;

    @ModelAttribute("todoForm")
    public TodoForm setupTodoForm() {
        return new TodoForm();
    }

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public String index(TodoForm todoForm) {
        log.debug("[TODO-UPDATE]index: {}", todoForm);

        String userId = "user1";

        // サービス呼出し
        Todo todo = todoService.findByUserIdAndTodoId(userId, todoForm.getTodoId());

        // 結果を TodoForm に反映
        dozerMapper.map(todo, todoForm);

        return "todo/todoUpdateInput";
    }

    @PostMapping("input")
    public String input(TodoForm todoForm) {
        log.debug("[TODO-UPDATE]input: {}", todoForm);
        return "todo/todoUpdateInput";
    }

    @PostMapping("confirm")
    public String confirm(@Validated({ Default.class, TodoForm.TodoUpdate.class }) TodoForm todoForm,
            BindingResult bindingResult) {
        log.debug("[TODO-UPDATE]confirm: {}", todoForm);

        if (bindingResult.hasErrors()) {
            return input(todoForm);
        }

        return "todo/todoUpdateConfirm";
    }

    @PostMapping("execute")
    public String execute(TodoForm todoForm, Model model) {
        log.debug("[TODO-UPDATE]execute: {}", todoForm);

        // TodoForm を Todo に変換
        Todo todo = dozerMapper.map(todoForm, Todo.class);

        // サービス呼出し
        todo.setUserId("user1");
        var result = todoService.save(todo);
        log.debug("[TODO-UPDATE]updated: {}", result);

        // 結果を TodoForm に反映
        dozerMapper.map(result, todoForm);

        String msg = messageSource.getMessage("message.todo.update.success", null, Locale.ROOT);
        model.addAttribute("message", msg);
        return "redirect:complete";
    }

    @GetMapping("complete")
    public String complete(SessionStatus sessionStatus, @ModelAttribute("message") String message, Model model) {
        log.debug("[TODO-UPDATE]complete: message={}", message);
        sessionStatus.setComplete();
        return "todo/todoUpdateComplete";
    }

    @GetMapping("cancel")
    public String cancel(SessionStatus sessionStatus) {
        log.debug("[TODO-UPDATE]cancel");
        sessionStatus.setComplete();
        return "redirect:/todo/";
    }

}
