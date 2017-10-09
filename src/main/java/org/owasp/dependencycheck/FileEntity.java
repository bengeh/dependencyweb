package org.owasp.dependencycheck;

import java.io.File;
import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "file_entity")
public class FileEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "report_results", nullable = false)
    private byte[] ReportResults;

    @Column(name = "time", nullable = false)
    private long time;

    // ... additional members, often include @OneToMany mappings

    protected FileEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public FileEntity(String fileName, long time, byte[] ReportResults) {
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

    public void setReportResults(byte[] ReportResults){
        this.ReportResults = ReportResults;
    }

    public byte[] getReportResults(){
        return ReportResults;
    }

    @Override
    public String toString() {
        return String.format(
                "[id=%d, fileName='%s', time='%d', reportresults='%s']",
                id, fileName, time, ReportResults);
    }

    @Embeddable
    class FileEntityId implements Serializable{
        String fileName;
        File ReportResults;
        long time;
    }
}
