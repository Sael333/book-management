package bookmanagement.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckHealthController implements CheckHealthControllerApi {

    @Override
    public String checkHealth() {
        return "OK";
    }
}
