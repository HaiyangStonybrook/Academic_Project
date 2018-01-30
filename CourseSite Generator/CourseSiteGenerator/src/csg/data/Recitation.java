/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

/**
 *
 * @author hy
 */
public class Recitation <E extends Comparable<E>> implements Comparable<E> {
    private String section;
    private String instructor;
    private String time;
    private String location;
    private String ta1;
    private String ta2;

    public Recitation(String section, String instructor, String time, String location, String ta1, String ta2) {
        this.section = section;
        this.instructor = instructor;
        this.time = time;
        this.location = location;
        this.ta1 = ta1;
        this.ta2 = ta2;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTa1() {
        return ta1;
    }

    public void setTa1(String ta1) {
        this.ta1 = ta1;
    }

    public String getTa2() {
        return ta2;
    }

    public void setTa2(String ta2) {
        this.ta2 = ta2;
    }

    @Override
    public int compareTo(E newRec) {
        return getSection().toString().compareTo(((Recitation) newRec).getSection().toString());
    }
    
    
}
