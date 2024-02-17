package com.the_experts.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class IndexController {

    @GetMapping({"/", "/{duration}"})
    String Index(@PathVariable(name = "duration", required = false) Integer duration) {
        if (duration == null) {
            duration = 1;
        }

        try {
            Thread.sleep(Duration.ofSeconds(duration));
        } catch (InterruptedException ignored) {
        }

        return "ok";
    }
}