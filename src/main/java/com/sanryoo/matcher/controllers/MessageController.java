package com.sanryoo.matcher.controllers;

import com.sanryoo.matcher.modal.MatcherMessage;
import com.sanryoo.matcher.modal.ResponseObject;
import com.sanryoo.matcher.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping("/{idsend}-{idreceive}-{start}-{end}")
    public ResponseEntity<ResponseObject> getMessages(@PathVariable("idsend") Long idsend, @PathVariable("idreceive") Long idreceive, @PathVariable("start") Long start, @PathVariable("end") Long end) {
        List<MatcherMessage> foundedMessages = messageRepository.getMessages(idsend, idreceive, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Get message successfully", foundedMessages));
    }

}
