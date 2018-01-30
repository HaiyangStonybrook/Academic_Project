/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import csg.TAManagerApp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.file.TimeSlot;

/**
 *
 * @author hy
 */
public class ComboBox_Wrap {
   private TAManagerApp app;
    //TeachingAssistant ta;
    private ArrayList<TimeSlot> timeSlot;
    private int oldStartTime;
    private int newStartTime;
    //TAData data;
    public ComboBox_Wrap( ArrayList<TimeSlot> initTimeSlot, TAManagerApp initApp, int oldTime, int newTime){
        //ta = TA;
        app = initApp;
        timeSlot = initTimeSlot;
        oldStartTime = oldTime;
        newStartTime = newTime;
       // data = oldData;
        
    }
    
    //public TeachingAssistant getTA(){
      //  return ta;
    //}
    
    public TAManagerApp getApp(){
        return app;
    }
    
    public ArrayList<TimeSlot> getTimeSlot(){
        return timeSlot;
    }
    
    public int getOldStartTime(){
        return oldStartTime;
    }
    
    public int getNewStartTime(){
        return newStartTime;
    }
    
    public void setOldStartTime(int time){
        oldStartTime = time;
    }
    
    public void setNewStartTime(int time){
        newStartTime = time;
    }
    
    public void setTimeSlot(ArrayList<TimeSlot> time){
        timeSlot = time;
    }
}
