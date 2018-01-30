/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import csg.TAManagerApp;
import csg.data.CSGData;
import csg.file.CSGFiles;
import csg.file.TimeSlot;
import csg.workspace.TAWorkspace;

/**
 *
 * @author hy
 */
public class ComboBox_Trans implements jTPS_Transaction {

    private ComboBox_Wrap startCombo;

    public ComboBox_Trans(ComboBox_Wrap start) {
        startCombo = start;
    }

    @Override
    public void doTransaction() {    // change to new office hour grid
        //int temp = startCombo.getOldStartTime();
        int oldTime = startCombo.getOldStartTime(); // 9
        int newTime = startCombo.getNewStartTime(); // 13
        startCombo.setOldStartTime(newTime);
        startCombo.setNewStartTime(oldTime);
        oldTime = startCombo.getOldStartTime(); // 13
        newTime = startCombo.getNewStartTime(); // 9
        undoTransaction();
        startCombo.setOldStartTime(newTime); // 9
        startCombo.setNewStartTime(oldTime); //13
        //startCombo.setOldStartTime(newTime);
        //startCombo.setNewStartTime(oldTime);
    }

    @Override
    public void undoTransaction() { // change to original office hour grid
        TAManagerApp app = startCombo.getApp();
        CSGData data = (CSGData) app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        CSGFiles file = (CSGFiles) app.getFileComponent();
        int newStartTime = startCombo.getOldStartTime(); // start time 9
        int oldStartTime = startCombo.getNewStartTime(); // 13
        int endTime = data.getEndHour();
        //data.setStartHour(newStartTime);
        //int start = data.getStartHour();
        
        ArrayList<TimeSlot> reBuildOfficeHour = startCombo.getTimeSlot();//TimeSlot.buildOfficeHoursList(data);
        //String st = reBuildOfficeHour.toString();
        //int st = reBuildOfficeHour.indexOf(1);
        data.setStartHour(newStartTime);  // 13
        //int newStart = data.getStartHour();
        workspace.enableCombox(false);
        workspace.resetWorkspace();
        workspace.reloadOfficeHoursGrid(data);

        for (TimeSlot slot : reBuildOfficeHour) {
            String time = slot.getTime();
            time = time.replace("_", ":");
            
            //time = p1 + p2;
            int timeSlot = file.parseToInteger(time);
            if (timeSlot >= newStartTime && timeSlot <= endTime) {
                data.addOfficeHoursReservation(slot.getDay(), slot.getTime(), slot.getName());
            }
        }
        //data.s(newStartTime);
        //reBuildOfficeHour = TimeSlot.buildOfficeHoursList(data);
        //startCombo.setTimeSlot(reBuildOfficeHour);
        
        int n = startCombo.getOldStartTime(); // 13
        int o = startCombo.getNewStartTime(); // 9
        String startString = "" + newStartTime;
        String startTimeCombo = file.parseToTime(startString);
        workspace.getStartTime().setValue(startTimeCombo);
        workspace.enableCombox(true);
        //startCombo.setNewStartTime(oldStartTime);
        //startCombo.setOldStartTime(newStartTime);

    }

}
