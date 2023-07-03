package com.example.controllers;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	Logger logger = LoggerFactory.getLogger(HelloWorldController.class);
	
	@Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;
	
	@Value("${service.helloworld.message}")
	private String message;
	
	@Autowired
	public HelloWorldController(JmsTemplate jmsTemplate, Queue queue) {
		this.jmsTemplate = jmsTemplate;
		this.queue = queue;
	}

	@GetMapping("/queue/produce")
	public ResponseEntity<String> queueProduce() {
				
		jmsTemplate.convertAndSend(queue, message);
		return ResponseEntity.ok("Queue was produced successfuly.");
		
	}
	
	@JmsListener(destination = "${jms.queue.name}")
    public void queueListener(String message) {
        logger.info("Queue was consumed successfuly by Listener. Message: " + message);
    }
	
}
