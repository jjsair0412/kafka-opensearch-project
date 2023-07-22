package com.example.kafkaopensearchproject.controller;

import com.example.kafkaopensearchproject.service.ManageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private final ManageProducer manageProducer;

    @GetMapping("/startProducer")
    public int start() {
        return manageProducer.StartProducer();
    }

    @GetMapping("/stopProducer")
    public int stop(){
        return manageProducer.StopProducer();
    }

}
