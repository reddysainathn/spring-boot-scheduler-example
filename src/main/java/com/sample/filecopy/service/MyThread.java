package com.sample.filecopy.service;

import org.springframework.stereotype.Component;

import java.io.File;


public class MyThread implements Runnable {

    private Integer whichFile;
    private String fileName;
    private String fileInPath;
    private String fileOutPath;

    public MyThread(Integer whichFile, String fileName, String fileInPath, String fileOutPath) {
        this.whichFile = whichFile;
        this.fileName = fileName;
        this.fileInPath = fileInPath;
        this.fileOutPath = fileOutPath;
    }

    public void run() {
        String fileIn = fileInPath + "/" + fileName;
        String fileOut = fileOutPath;
        File afile = new File(fileIn);
        if (afile.renameTo(new File(fileOut + "/" + "Output_" + fileName))) {
            System.out.println("File is moved successful! ->" + fileName);
            //Any Logic Specific to files can go here.
        } else {
            System.out.println("File is failed to move!");
        }
    }
}