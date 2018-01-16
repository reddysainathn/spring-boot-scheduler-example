package com.sample.filecopy.controller;

import com.sample.filecopy.service.MoveFilesScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyRestMovingCOntroller {

    private final MoveFilesScheduler moveFilesScheduler;

    @Autowired
    public MyRestMovingCOntroller(MoveFilesScheduler moveFilesScheduler) {
        this.moveFilesScheduler = moveFilesScheduler;
    }

    @RequestMapping("/execute")
    public String getInfo() {
        return moveFilesScheduler.moveFiles();
    }

    @RequestMapping("/status")
    public String getStatus() {
        if (moveFilesScheduler.status) {
            return "Scheduler Active";
        } else {
            return "Scheduler In-Active";
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/updatePath")
    public @ResponseBody
    String sayHello(@RequestParam(value = "inPath") String inPath, @RequestParam(value = "outPath") String outPath) {
        return moveFilesScheduler.updatePath(inPath, outPath);
    }
}
