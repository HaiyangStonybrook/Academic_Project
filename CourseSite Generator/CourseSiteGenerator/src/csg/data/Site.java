/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

/**
 *
 * @author hy
 */
public class Site {
    private Boolean use;
    private String navTitle;
    private String fileName;
    private String script;

    public Site( String navTitle, String fileName, String script) {
        //this.use = use;
        this.navTitle = navTitle;
        this.fileName = fileName;
        this.script = script;
    }
   
}
