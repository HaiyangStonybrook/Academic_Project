/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hy
 */
public class TeamJS {
    private StringProperty name; 
    private StringProperty red; 
    private StringProperty green; 
    private StringProperty blue; 
    private StringProperty textColor; 
    
    public TeamJS(String name, String red,String green, String blue,  String textColor ) {
        this.name =new SimpleStringProperty (name);
        this.red =new SimpleStringProperty (red);
        this.green =new SimpleStringProperty (green);
        this.blue =new SimpleStringProperty (blue);
        
        
         
        this.textColor = new SimpleStringProperty (textColor);
        
    }

    public String getName() {
        return name.get();
    }

    public String getRed() {
        return red.get();
    }

    public String getGreen() {
        return green.get();
    }

    public String getBlue() {
        return blue.get();
    }

    public String getTextColor() {
        return textColor.get();
    }

}
