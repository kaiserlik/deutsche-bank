package com.db.cb.signalhandler;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
public class Controller {

    private final SignalService application;

    {
        try {
            application = new SignalService();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/home/int/{signal}")
    protected ResponseEntity<Integer> receiveSignal(@PathVariable("signal") int signal) throws MethodArgumentTypeMismatchException {
        application.handleSignal(signal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/home/json")
    protected ResponseEntity<String> receiveSignal(@RequestParam(value="signal") String signal) throws MethodArgumentTypeMismatchException, ParseException {
        application.handleSignal(Utility.parseJson(signal));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}