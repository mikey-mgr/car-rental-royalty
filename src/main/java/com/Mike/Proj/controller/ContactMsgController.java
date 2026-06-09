package com.Mike.Proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.contact.ContactMessageCreateRequest;
import com.Mike.Proj.model.ContactMessages;
import com.Mike.Proj.service.ContactMsgService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contact")
public class ContactMsgController {

    @Autowired
    ContactMsgService contactMsgService;

    /**
     * Save a new contact message.
     * Accepts a validated DTO that prevents ID-based mass assignment attacks.
     */
    @PostMapping("/submit")
    public ResponseEntity<APIResponse> saveMessage(@Valid @RequestBody ContactMessageCreateRequest request){
        contactMsgService.saveMessage(request);
        return new ResponseEntity<APIResponse>(new APIResponse(true, "Message has been sent"), HttpStatus.CREATED);
    }

    //get a list of all sent messages
    @GetMapping("/list")
    public ResponseEntity<List<ContactMessages>> getMessage(){
        List<ContactMessages> msgs = contactMsgService.getMessages();
        return new ResponseEntity<List<ContactMessages>>(msgs, HttpStatus.OK);
    }

    @GetMapping("/find-message")
    public ResponseEntity<List<ContactMessages>> findMessage(@RequestParam String name){
        List<ContactMessages> msgs = contactMsgService.findMessage(name);
        return new ResponseEntity<List<ContactMessages>>(msgs, HttpStatus.OK);
    }
}

