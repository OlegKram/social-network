package coding.twitterclone.controller;

import coding.twitterclone.domain.Message;
import coding.twitterclone.domain.User;
import coding.twitterclone.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

//main controller for queries
@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    //greeting page
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    //main page
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

    //add messages
    Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
        messages = messageRepo.findByTag(filter);
    } else {
        messages = messageRepo.findAll();
    }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
}

    //filter and search messages
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model
    ) {
        Message message = new Message(text, tag, user);

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);

        return "main";
    }
}