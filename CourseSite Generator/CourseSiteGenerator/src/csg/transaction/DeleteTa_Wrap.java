/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Label;
import csg.TAManagerApp;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;

/**
 *
 * @author hy
 */
public class DeleteTa_Wrap {
    private TAManagerApp app;
    private TeachingAssistant ta;
    private ArrayList<String> cellKey;
    public DeleteTa_Wrap(TeachingAssistant taa, ArrayList<String> cellKeyy ,TAManagerApp initAppp ){ //TAManagerApp initApp
        ta=taa;
        app = initAppp;
        cellKey =cellKeyy;
    }
    public  TAManagerApp getApp(){
        return app;
    }
    
    public TeachingAssistant getTA(){
        return ta;
    }
    
    public ArrayList<String> getCellKey(){
       return cellKey;
    }
}
