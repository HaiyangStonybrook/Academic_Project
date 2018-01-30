/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import csg.TAManagerApp;
import csg.data.TeachingAssistant;

/**
 *
 * @author hy
 */
public class UpdateTa_Wrap {
    private TAManagerApp app;
    private TeachingAssistant ta;
    private ArrayList<String> cellKey;
    
    public UpdateTa_Wrap(TeachingAssistant ta, ArrayList<String> cellKey, TAManagerApp initApp){
        this.app = initApp;
        this.ta = ta;
        this.cellKey = cellKey;
    }
    
    public TeachingAssistant getTA(){
        return ta;
    }
    
    public TAManagerApp getApp(){
        return app;
    }
    
    public ArrayList<String> getCellKey(){
        return cellKey;
    }
}
