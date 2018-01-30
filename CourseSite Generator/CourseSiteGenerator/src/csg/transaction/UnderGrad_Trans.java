/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import csg.TAManagerApp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;

/**
 *
 * @author hy
 */
public class UnderGrad_Trans implements jTPS_Transaction{
    private UnderGrad_Wrap wrap;
    
    public UnderGrad_Trans(UnderGrad_Wrap wrap1){
        wrap =wrap1;
    }
    
    @Override
    public void doTransaction() {
        undoTransaction();
    }

    @Override
    public void undoTransaction() {
        String taName = wrap.getName();
        TAManagerApp app = wrap.getApp();
        CSGData data = (CSGData) app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TeachingAssistant ta = data.getTA(taName);
        
        Boolean b = ta.getUndergrad();
        if(b){
            ta.setUndergrad(false);
        }
        else
            ta.setUndergrad(true);
            workspace.getTATable().refresh();
            app.getGUI().updateToolbarControls(false);
         
        
    }
}
