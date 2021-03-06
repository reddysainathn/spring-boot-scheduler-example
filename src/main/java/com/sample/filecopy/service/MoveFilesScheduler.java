package com.sample.filecopy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MoveFilesScheduler {

    @Value("${network.path.in}")
    private String systemInPath;

    @Value("${network.path.out}")
    private String systemOutPath;

    @Value("${network.poolCount}")
    private Integer poolCount;

    private String message;

    private Integer countFilesMoved;

    public boolean status = false;

    public String moveFiles() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            // int count = (int)Files.list(Paths.get(systemPath)).count();
            return getAllThreads(getAllFileNames());
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Scheduled(fixedRate = 20000)
    public void moveFilesScheduled() {
        try {
            if (status == false) {
                // int count = (int)Files.list(Paths.get(systemPath)).count();
                getAllThreads(getAllFileNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllFileNames() {
        List<String> results = new ArrayList<String>();
        try {
            File[] files = new File(systemInPath).listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        results.add(file.getName());
                    }
                }
            } else {
                System.out.println("Nothing to Move");
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return results;
    }


    public String getAllThreads(List<String> fileNames) {
        if (fileNames.isEmpty()) {
            status = false;
            countFilesMoved = 0;
            return message = "Nothing to Execute---->" + countFilesMoved + " files moved.";
        } else if (fileNames.size() >= poolCount) {
            status = true;
            Double executions = Math.ceil(fileNames.size() / poolCount);
            for (int z = 0; z <= executions.intValue(); z++) {
                ArrayList<String> inputFiles = getPartisionList(fileNames, z);
                if (!inputFiles.isEmpty()) {
                    ExecutorService threadPool = Executors.newFixedThreadPool(inputFiles.size());
                    for (int i = 0; i < inputFiles.size(); i++) {
                        threadPool.execute(new MyThread(i, inputFiles.get(i), systemInPath, systemOutPath));
                    }
                    threadPool.shutdown();
                }
            }
            countFilesMoved = fileNames.size();
            status = false;
            return message = "Execution Successful---->" + countFilesMoved + " files moved.";
        } else {
            status = true;
            ExecutorService threadPool = Executors.newFixedThreadPool(fileNames.size());
            for (int i = 0; i < fileNames.size(); i++) {
                threadPool.execute(new MyThread(i, fileNames.get(i), systemInPath, systemOutPath));
            }
            threadPool.shutdown();
            countFilesMoved = fileNames.size();
            status = false;
            return message = "Execution Successful---->" + countFilesMoved + " files moved.";
        }
    }

    private ArrayList<String> getPartisionList(List<String> fileNames, Integer value) {
        int count = (value + 1) * poolCount;
        int endIndex = count - 1;
        int startIndex = endIndex - (poolCount - 1);
        if (endIndex >= fileNames.size()) {
            endIndex = fileNames.size() - 1;
        }
        return new ArrayList<String>(fileNames.subList(startIndex, endIndex + 1));
    }

    public String updatePath(String inpath, String outpath) {
        systemInPath = inpath;
        systemOutPath = outpath;
        return "In path updated to :" + systemInPath + " and Out Path:" + systemOutPath;
    }

}
