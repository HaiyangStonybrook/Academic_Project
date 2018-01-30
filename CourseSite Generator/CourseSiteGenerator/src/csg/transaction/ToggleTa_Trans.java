/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import csg.data.CSGData;
import csg.workspace.TAWorkspace;

/**
 *
 * @author hy
 */
public class ToggleTa_Trans implements jTPS_Transaction {
    private ToggleTa_Wrap ta;
    
    public ToggleTa_Trans(ToggleTa_Wrap TA){
        ta = TA;
    }
    @Override
    public void doTransaction() {   //add ta in cell
        undoTransaction();
    }

    @Override
    public void undoTransaction() {  // cut ta in cell
        CSGData data = (CSGData)ta.getApp().getDataComponent();
        //TAWorkspace workspace = (TAWorkspace) ta.getApp().getWorkspaceComponent();
        String cellKey = ta.getCellKey();
        String taName = ta.getTA().getName();
        StringProperty cellProp = data.getOfficeHours().get(cellKey);
        String cellText = cellProp.getValue();
       
        // IF IT ALREADY HAS THE TA, REMOVE IT
       if(cellText.equals(taName)){
           data.removeTAFromCell(cellProp, taName);
       } 
       else if (cellText.contains("\n"+taName)||cellText.contains(taName+"\n")) {
            data.removeTAFromCell(cellProp, taName);
        } // OTHERWISE ADD IT
        else if (cellText.length() == 0) {
            cellProp.setValue(taName);
        } else {
            cellProp.setValue(cellText + "\n" + taName);
        }
    }
    
}
