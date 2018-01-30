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
public class SitePage {
    private  boolean u;
    private  String nav;
    private  String file;
    private  String scr;

    public SitePage(boolean u, String nav, String file, String scr) {
        this.u = u;
        this.nav = nav;
        this.file = file;
        this.scr = scr;
    }

    public boolean isU() {
        return u;
    }

    public void setU(boolean u) {
        this.u = u;
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getScr() {
        return scr;
    }

    public void setScr(String scr) {
        this.scr = scr;
    }
    
    
/*
    public SitePage(boolean u, String nav, String file, String scri ){
        use = u;
        nav = nav;
        fileName = file;
        script = scri;
    }
   
    public String getNavTitle() {
        return navT;
    }

    public void setNavTitle(String navTitle) {
        this.nav = navTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }*/

    
}
