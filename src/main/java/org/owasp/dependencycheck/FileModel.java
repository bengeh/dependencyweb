package org.owasp.dependencycheck;

import java.io.File;
public class FileModel {

    private Long id;
    private File ReportResults;
    private String fileName;
    private long time;

    public FileModel(String fileName, long time, File ReportResults) {
        this.fileName = fileName;
        this.time = time;
        this.ReportResults = ReportResults;
    }


    public void setFileName(String fileName){

        this.fileName = fileName;

    }

    public String getFileName(){
        return fileName;
    }


    public void setTime(long timeTaken){

        this.time = timeTaken;
    }

    public long getTime(){
        return time;
    }

    public void setReportResults(File ReportResults){
        this.ReportResults = ReportResults;
    }

    public File getReportResults(){
        return ReportResults;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, fileName='%s', time='%d', reportresults='%s']",
                id, fileName, time, ReportResults);
    }
}
