package org.group72.server.controller;

import org.group72.server.model.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class MainController {

    private static final String template = "Greetings, %s!";
    private static final AtomicLong counter = new AtomicLong();

    @GetMapping(path="/hello")
    public @ResponseBody Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    @GetMapping(path="/")
    public @ResponseBody String homePage() {
        return "Greetings from lightrip server!";
    }

}