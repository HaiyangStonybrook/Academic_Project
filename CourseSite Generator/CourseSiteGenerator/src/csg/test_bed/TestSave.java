/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.test_bed;

import csg.data.CSGData;
import csg.file.CSGFiles;
import csg.workspace.TAWorkspace;
import static djf.settings.AppStartupConstants.PATH_DATA;
import static djf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import java.io.IOException;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 *
 * @author hy
 */
public class TestSave {
    public static void main(String[] args)
    {
        
        //get PropertiesManager
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //add contents to PropertiesManager
        props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
        
        //load properties from xml
        try {
            props.loadProperties("app_properties.xml", PROPERTIES_SCHEMA_FILE_NAME);
        } catch (InvalidXMLFileFormatException ex) {
            ex.printStackTrace();
        }
        
        
        //create Managers
        CSGData dataManager = new CSGData(null);
        
        CSGFiles fileManager = new CSGFiles(null);
        
        
        //TESTS-1.  TAS_DATA:
        dataManager.setStartHour(10);
        dataManager.setEndHour(19);
        dataManager.addTA("Wonje Kang", "wonje.kang@stonybrook.edu", false);
        dataManager.addTA("Haiyang", "haiyang.kang@stonybrook.edu", false);
        dataManager.addTA("Chase", "chase@stonybrook.edu", false);
        
        
        HashMap officeHoursHash = dataManager.getOfficeHours();
        //first, inicilize all the cell keys
        //1. first row
        String[] firstRow = { "Start Time","End Time","MONDAY","TUESDAY","WEDNESDAY", "THURSDAY","FRIDAY"   };
        for(int col = 0; col<=6 ;col++)
        {
            int row = 0;
            String key = col +"_"+row;
            SimpleStringProperty val =new SimpleStringProperty( firstRow[col] );
            officeHoursHash.put(key, val);
            
        }
        //2. first col.( except first row)
        int hour = dataManager.getStartHour();
        for (int row = 1; row < dataManager.getNumRows(); row++) {
            int col = 0;
            String key = col +"_"+row;
            if ( (row & 1) == 0 )//even
            {
            String valueStr = new String();
            valueStr = TAWorkspace.buildCellText(hour, "30");
            
            SimpleStringProperty val = new SimpleStringProperty(valueStr);
            officeHoursHash.put(key, val);
            hour++;
            }
            else
            {
            String valueStr = TAWorkspace.buildCellText(hour, "00");
            
            SimpleStringProperty val = new SimpleStringProperty(valueStr);
            officeHoursHash.put(key, val);
            
            }
            
        }
        
        
        //3. all the cell inside
        for (int row = 1; row < dataManager.getNumRows(); row++) {
            for (int col = 2; col < 7; col++) {
                String key = col +"_"+row;
                SimpleStringProperty s = new SimpleStringProperty("");
                officeHoursHash.put(key, s);
                
            }
        }
        
        dataManager.addOfficeHoursReservation("MONDAY", "10_00am", "Haiyang");
        dataManager.addOfficeHoursReservation("TUESDAY", "2_00pm", "Chase");
        
        
        //TEST 2. RD:
                 
        
        dataManager.addRecitation("R02", "McKenna", 
              "Wed 3:30pm-4:23pm",
              "Old CS 2114",
                "Jane Doe", 
               "Joe Shmo");
        
        
        //TEST 3. SD
        dataManager.addScheduleItem("Holiday",
                "2017-02-09",
                "Spring break",
                "Event Programming",
                "11:59pm",
                "http://test.com/test/test",
               "https://docs.google.com/spreadsheets/d/1hDY9OR4Nu5p2XLd3mkTB93y78Bnk3LInO0Ydkrz8gZg"
        );
        dataManager.setStartMon("2012-04-23");
        dataManager.setStartFri("2017-04-21");
        //TEST 4. PD:
        dataManager.addTeam("very good",
                "553467",
                "f022cc",
                "http://c4-comics.appspot.com"
                );
        dataManager.addStudent(
                "Beau",
            "Brummell",
                "Atomic Comics",
                "Lead Designer"             
                );
        //TEST 5. CD:
        dataManager.addSitePage(
                Boolean.TRUE, 
                "Home",
                "index.html",
                "HomeBuilder.js"
                );
        dataManager.addSitePage(
                Boolean.TRUE, 
                "Syllabus", 
                "Syllabus.html", 
                "SyllabusBuilder.js"
                );
        dataManager.addSitePage(
                Boolean.TRUE, 
                "Schedule", 
                "schedule.html", 
                "ScheduleBuilder.js"
                );
        dataManager.addSitePage(
                Boolean.TRUE, 
                "HWs", 
                "hws.html", 
                "HWsBuilder.js"
                );
        dataManager.addSitePage(
                Boolean.TRUE, 
                "Projects", 
                "projects.html", 
                "ProjectsBuilder.js"
                );
        
        dataManager.setSubject( "CSE" );
        dataManager.setNumber( "219" );
        dataManager.setSemester( "Fall" );
        dataManager.setYear(  "2017" );
        dataManager.setTitle( "Computer Science III" );
        dataManager.setInstructorName( "Richard McKenna");
        dataManager.setInstructorHome( "http://www.cs.stonybrook.edu/~richard" );
        dataManager.setExportDir( "file:./exportData"  );
        dataManager.setTemplateDir("file:./templates/CSE219");
        
        
        String pathTest = "file:./images/" +  "yale_university.png" ;
        String pathTest2 = "file:./images/" +  "yale_cs.png" ;
        
        dataManager.setBannerImg( pathTest  );
        dataManager.setLeftImg( pathTest );
        dataManager.setRightImg( pathTest2  );
        
        dataManager.setStylesheet("./css/sea_wolf_1.css");
    
        
        
        //FINALLY, SAVE DATA:
        String testPath = System.getProperty("user.dir"); 
                        ///Users/hy/workspace/NetBeans/CSE219/hw4/hw4/CourseSiteGenerator/work
        String filePath = testPath +"/work/SiteSaveTest.json";
        try {
            fileManager.saveData(dataManager, filePath);
        } catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    
    
    
    }

}
