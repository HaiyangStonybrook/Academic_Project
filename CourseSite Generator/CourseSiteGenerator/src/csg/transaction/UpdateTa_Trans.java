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
public class UpdateTa_Trans implements jTPS_Transaction{
    private UpdateTa_Wrap ota;
    private UpdateTa_Wrap nta;
    
    public UpdateTa_Trans(UpdateTa_Wrap oldTA, UpdateTa_Wrap newTA){
        this.ota= oldTA;
        this.nta = newTA;
    }
    @Override
    public void doTransaction() { //change to new ta
        UpdateTa_Wrap tempTa = ota;
        ota = nta;
        nta = tempTa;
        undoTransaction();
        tempTa = nta;
        nta = ota;
        ota = tempTa;
    }

    @Override
    public void undoTransaction() { // change to oldta
        TAManagerApp app = ota.getApp();   //redo: nta.getApp()
        CSGData data = (CSGData) app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace) ota.getApp().getWorkspaceComponent();
        //TeachingAssistant smallTa = ta.getTA();
        data.removeTA(nta.getTA().getName()); // // redo, remove oldta
        data.addTA(ota.getTA().getName(), ota.getTA().getEmail(), ota.getTA().getUndergrad()); // add newta
        //ArrayList<String> cellKey = nta.getCellKey();
        //for(String k: cellKey){
          //data.toggleTAOfficeHours(k, name);
          //StringProperty prop = data.getOfficeHours().get(k);
          //String text = prop.getValue();
          
       //}
       HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
                for (Label label : labels.values()) {
                    String text = label.getText();
                    if (label.getText().equals(nta.getTA().getName())
                            || (label.getText().contains(nta.getTA().getName() + "\n"))
                            || (label.getText().contains("\n" + nta.getTA().getName()))) {
                        //data.removeTAFromCell(label.textProperty(), nta.getTA().getName());
                            String newLabel = label.getText().replaceAll(nta.getTA().getName(), ota.getTA().getName());
                            label.setText(newLabel);
                    }
                }
    }
    
}
