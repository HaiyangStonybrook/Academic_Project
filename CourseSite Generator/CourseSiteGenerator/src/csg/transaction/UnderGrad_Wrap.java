/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import csg.TAManagerApp;

/**
 *
 * @author hy
 */
public class UnderGrad_Wrap {
    private String taToggleName;
    private TAManagerApp app;
    
    public UnderGrad_Wrap(String taName, TAManagerApp app){
        taToggleName = taName;
        this.app = app;
    }
    
    public String getName(){
        return taToggleName;
    }
    
    public TAManagerApp getApp(){
        return app;
    }
}
