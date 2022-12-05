package com.sanryoo.toifa.controllers;

import com.sanryoo.toifa.modal.Information;
import com.sanryoo.toifa.modal.ResponseObject;
import com.sanryoo.toifa.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/information")
public class InformationController {

    @Autowired
    private InformationRepository informationRepository;

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getInformation(@PathVariable Long id) {
        Optional<Information> foundInformation = informationRepository.findById(id);
        return foundInformation.map(information -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Find information successful", information))).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Can't find information with id = " + id, new Information())));
    }

    @PutMapping("")
    ResponseEntity<ResponseObject> updateInformation(@RequestParam("id") Long id, @RequestParam("column") String column, @RequestParam("value") String value) {
        Optional<Information> foundInformation = informationRepository.findById(id);
        return foundInformation.map(information -> {
            try {
                switch (column) {
                    case "fullname" -> information.setFullname(value);
                    case "sex" -> information.setSex(Integer.parseInt(value));
                    case "status" -> information.setStatus(Integer.parseInt(value));
                    case "searching" -> information.setSearching(Integer.parseInt(value));
                    case "avatar" -> information.setAvatar(value);
                    case "image1" -> information.setImage1(value);
                    case "image2" -> information.setImage2(value);
                    case "image3" -> information.setImage3(value);
                    case "age" -> information.setAge(Integer.parseInt(value));
                    case "height" -> information.setHeight(Integer.parseInt(value));
                    case "weight" -> information.setWeight(Integer.parseInt(value));
                    case "income" -> information.setIncome(Integer.parseInt(value));
                    case "province" -> information.setProvince(value);
                    case "appearance" -> information.setAppearance(Double.parseDouble(value));
                    case "easy" -> information.setEasy(Integer.parseInt(value));
                    case "age1" -> information.setAge1(Integer.parseInt(value));
                    case "age2" -> information.setAge2(Integer.parseInt(value));
                    case "height1" -> information.setHeight1(Integer.parseInt(value));
                    case "height2" -> information.setHeight2(Integer.parseInt(value));
                    case "weight1" -> information.setWeight1(Integer.parseInt(value));
                    case "weight2" -> information.setWeight2(Integer.parseInt(value));
                    case "income1" -> information.setIncome1(Integer.parseInt(value));
                    case "province1" -> information.setProvince1(Integer.parseInt(value));
                    case "appearance1" -> information.setAppearance1(Double.parseDouble(value));
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Updated information", informationRepository.save(information)));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Can't find information with id = " + id, new Information())));
    }

}
