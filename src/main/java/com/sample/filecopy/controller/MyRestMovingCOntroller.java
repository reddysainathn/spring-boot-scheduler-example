package com.sample.filecopy.controller;

import com.sample.filecopy.service.MoveFilesScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestMovingCOntroller {

    private final MoveFilesScheduler moveFilesScheduler;

    @Autowired
    public MyRestMovingCOntroller(MoveFilesScheduler moveFilesScheduler) {
        this.moveFilesScheduler = moveFilesScheduler;
    }

    @RequestMapping("/execute")
    public String getInfo(){
        return moveFilesScheduler.moveFiles();
    }

}
