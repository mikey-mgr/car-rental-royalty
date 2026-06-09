package com.Mike.Proj.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mike.Proj.dto.contact.ContactMessageCreateRequest;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.ContactMessages;
import com.Mike.Proj.repository.ContactMsgRepo;

@Service
public class ContactMsgService {

    @Autowired
    ContactMsgRepo contactMsgRepo;

    //retrieve a list of all messages
    public List<ContactMessages> getMessages(){
        List<ContactMessages> msgs = contactMsgRepo.findAll();

        if(msgs.isEmpty()){
            throw new CustomException("There are no contact messages");
        } else return msgs;
    }

    /**
     * Save a contact message from a validated DTO.
     * Prevents mass-assignment attacks by reconstructing the entity server-side.
     */
    public void saveMessage(ContactMessageCreateRequest request){
        if(Objects.isNull(request)){
            throw new CustomException("The submitted message contains no data");
        }
        
        // Create entity server-side with only DTO fields (no ID)
        ContactMessages msg = new ContactMessages(
            request.getName(),
            request.getEmail(),
            request.getCity(),
            request.getCountry(),
            request.getPhone(),
            request.getMessage()
        );
        
        contactMsgRepo.save(msg);
    }

    //find a contact message by name
    public List<ContactMessages> findMessage(String name){
        List<ContactMessages> msgs = contactMsgRepo.findByNameContaining(name);

        if(msgs.isEmpty()){
            throw new CustomException("There is no message with name: " + name);
        } else return msgs;
    }
}

