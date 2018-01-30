/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import csg.TAManagerApp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;

/**
 *
 * @author hy
 */
public class DeleteTA_Trans implements jTPS_Transaction{
    private DeleteTa_Wrap ta;
    
     public DeleteTA_Trans(DeleteTa_Wrap ta) {
       this.ta= ta;
    }

    @Override
    public void doTransaction() { // delete TA
        
        CSGData data = (CSGData)ta.getApp().getDataComponent();
        TAWorkspace workspace = (TAWorkspace) ta.getApp().getWorkspaceComponent();
        String name = ta.getTA().getName();
        String email = ta.getTA().getEmail();
        data.removeTA(name);
        ArrayList<String> cellKey = ta.getCellKey();
        HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
                for (Label label : labels.values()) {
                    String text = label.getText();
                    if (label.getText().equals(name)
                            || (label.getText().contains(name + "\n"))
                            || (label.getText().contains("\n" + name))) {
                        data.removeTAFromCell(label.textProperty(), name);
                    }
                }
          
    }

    @Override
    public void undoTransaction() { // add TA
        
       CSGData data = (CSGData)ta.getApp().getDataComponent();
       String name = ta.getTA().getName();
       String email = ta.getTA().getEmail();
       Boolean underGrad = ta.getTA().getUndergrad();
       ArrayList<String> cellKey = ta.getCellKey();
       data.addTA(name, email, underGrad);
       for(String k: cellKey){
          data.toggleTAOfficeHours(k, name);
       }
    }
    
   // @Override
    //public String toString() {
       
    //}
    
}
