/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import csg.data.CSGData;

/**
 *
 * @author hy
 */
public class addTA_Trans implements jTPS_Transaction {
    private addTA_Wrap ta;
    
    public addTA_Trans(addTA_Wrap ta){
        this.ta = ta;
    }

    @Override
    public void doTransaction() {  // add TA
       
       CSGData data = (CSGData)ta.getApp().getDataComponent();
       String name = ta.getName();
       String email = ta.getEmail(); 
       //Boolean underGrad = data.getTA(name).getUndergrad();
       data.addTA(name,email, false);
    }

    @Override
    public void undoTransaction() { // delete TA
       CSGData data = (CSGData)ta.getApp().getDataComponent();
       String name = ta.getName();
       //String email = ta.getEmail(); 
       data.removeTA(name);
    }
    
    
}
