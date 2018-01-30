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
public class addTA_Wrap {
    private TeachingAssistant ta;
    private TAManagerApp app;
    public addTA_Wrap(TeachingAssistant ta, TAManagerApp initApp){
        this.ta = ta;
        this.app = initApp;
    }
    
    public String getName(){
        return ta.getName();
    }
    
    public String getEmail(){
        return ta.getEmail();
    }
    
     public  TAManagerApp getApp(){
        return app;
    }
}
