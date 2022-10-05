package com.db.cb.signalhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
public class Controller {

    private final SignalService application = new SignalService();

    @GetMapping("/home/{signal}")
    protected ResponseEntity<Integer> receiveSignal(@PathVariable("signal") int signal) throws MethodArgumentTypeMismatchException {
        application.handleSignal(signal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}