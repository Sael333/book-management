package bookmanagement.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1")
public interface CheckHealthControllerApi {

    @GetMapping("/checkhealth")
    String checkHealth();
}
