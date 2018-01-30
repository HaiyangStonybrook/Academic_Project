/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import csg.TAManagerApp;
import csg.data.TeachingAssistant;

/**
 *
 * @author hy
 */
public class ToggleTa_Wrap {
    private TAManagerApp app;
    private TeachingAssistant ta;
    private String cellKey;
    public ToggleTa_Wrap(TeachingAssistant TA,TAManagerApp initApp, String key){
        app = initApp;
        ta = TA;
        cellKey = key;
    }
    
    public  TAManagerApp getApp(){
        return app;
    }
    
    public TeachingAssistant getTA(){
        return ta;
    }
    
    public String getCellKey(){
        return cellKey;
    }
}
