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
public class EndComboBox_Trans implements jTPS_Transaction {
    private EndComboBox_Wrap endCombo;

    public EndComboBox_Trans(EndComboBox_Wrap end) {
        endCombo = end;
    }
    @Override
    public void doTransaction() {
        int oldTime = endCombo.getOldStartTime(); // 20   14
        int newTime = endCombo.getNewStartTime(); // 14   20
        endCombo.setOldStartTime(newTime);
        endCombo.setNewStartTime(oldTime);
        oldTime = endCombo.getOldStartTime(); // 14    20
        newTime = endCombo.getNewStartTime(); // 20    14
        undoTransaction();
        endCombo.setOldStartTime(newTime); // 20
        endCombo.setNewStartTime(oldTime); // 14
    }

    @Override
    public void undoTransaction() {
         TAManagerApp app = endCombo.getApp();
        CSGData data = (CSGData) app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        CSGFiles file = (CSGFiles) app.getFileComponent();
        int newStartTime = endCombo.getOldStartTime(); // start time 20
        int oldStartTime = endCombo.getNewStartTime(); // 18
        int startTime = data.getStartHour();
        //data.setStartHour(newStartTime);
        //int start = data.getStartHour();
        
        ArrayList<TimeSlot> reBuildOfficeHour = endCombo.getTimeSlot();//TimeSlot.buildOfficeHoursList(data);
        //String st = reBuildOfficeHour.toString();
        //int st = reBuildOfficeHour.indexOf(1);
        data.setEndHour(newStartTime);  // 
        int after = data.getEndHour();
        //int newStart = data.getStartHour();
        workspace.enableCombox(false);
        workspace.resetWorkspace();
        workspace.reloadOfficeHoursGrid(data);

        for (TimeSlot slot : reBuildOfficeHour) {
            String time = slot.getTime();
            time = time.replace("_", ":");
            int timeSlot = file.parseToInteger(time);
            if (timeSlot >= startTime && timeSlot <= newStartTime) { // ~~~~~~~~~~~~~
                data.addOfficeHoursReservation(slot.getDay(), slot.getTime(), slot.getName());
            }
        }
        //data.s(newStartTime);
        //reBuildOfficeHour = TimeSlot.buildOfficeHoursList(data);
        //startCombo.setTimeSlot(reBuildOfficeHour);
        
        int n = endCombo.getOldStartTime(); // 20
        int o = endCombo.getNewStartTime(); // 14
        String startString = "" + newStartTime;
        String startTimeCombo = file.parseToTime(startString);
        workspace.getEndTime().setValue(startTimeCombo);
        workspace.enableCombox(true);
    }
    
}
