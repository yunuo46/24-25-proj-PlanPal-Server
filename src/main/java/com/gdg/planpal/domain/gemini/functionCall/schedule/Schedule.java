package com.gdg.planpal.domain.gemini.functionCall.schedule;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Schedule {
    Long spotId;
    String startDate;
    String startTime;

    String endDate;
    String endTime;

    String spotName;

    public Schedule(Long spotId, String spotName,String startDate,String startTime,String endDate,String endTime){
        this.spotId=spotId;
        this.spotName=spotName;
        this.startDate=startDate;
        this.startTime=startTime;
        this.endDate=endDate;
        this.endTime= endTime;
    }
    public Schedule(){}


    public String toString(){
        return "["+this.spotId.toString()+":"+this.spotName+"]"+this.startDate+":"+this.startTime+"~"+this.endDate+":"+this.endTime;
    }

    public LocalDateTime convertToLocalTime(String time){
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public LocalDateTime getStartDateTime(){
        return convertToLocalTime(startDate+" "+startTime);
    }

    public LocalDateTime getEndDateTime(){
        return convertToLocalTime(endDate+" "+endTime);
    }
}
