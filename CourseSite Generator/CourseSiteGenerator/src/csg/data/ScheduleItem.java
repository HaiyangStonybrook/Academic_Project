/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.time.LocalDate;

/**
 *
 * @author hy
 */
public class ScheduleItem <E extends Comparable<E>> implements Comparable<E> {
    private String type;
    private String date;
    private String title;
    private String topic;
    
    private String time;
    private String link;
    private String criteria;

    public ScheduleItem(String type, String date, String title, String topic,
            String time, String link, String criteria) {
        this.type = type;
        this.date = date;
        this.title = title;
        this.topic = topic;
        
        this.time = time;
        this.link = link;
        this.criteria = criteria;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public int compareTo(E newStr) {
        String currentStr = getDate().toString();
        LocalDate currentDate = LocalDate.parse(currentStr);
        String newDateStr = ((ScheduleItem)newStr).getDate().toString();
        LocalDate newDate = LocalDate.parse(newDateStr);
        return currentDate.compareTo(newDate);
    }
    
    
            
}
