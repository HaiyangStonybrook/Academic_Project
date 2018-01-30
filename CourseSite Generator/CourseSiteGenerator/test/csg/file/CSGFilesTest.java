/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.ScheduleItem;
import csg.data.SitePage;
import csg.data.Student;
import csg.data.TeachingAssistant;
import csg.data.Team;
import djf.components.AppDataComponent;
import static djf.settings.AppStartupConstants.PATH_DATA;
import static djf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 *
 * @author hy
 */
public class CSGFilesTest {
    
    public CSGFilesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of loadData method, of class CSGFiles.
     */
    @Test
    public void testLoadData() throws Exception {
        System.out.println("loadData");
         //get PropertiesManager
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //add contents to PropertiesManager
        props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
        
        //load properties from xml
        try {
            props.loadProperties("app_properties.xml", PROPERTIES_SCHEMA_FILE_NAME);
        }   catch (InvalidXMLFileFormatException ex) {
            ex.printStackTrace();
        }
        //create Managers
        CSGData dataManager = new CSGData(null);
        CSGFiles fileManager = new CSGFiles(null);
        String path =  System.getProperty("user.dir")+"/work/SiteSaveTest.json";
        fileManager.loadData(dataManager,  path );
        
        // test start hour, end hour
        assertEquals(10, dataManager.getStartHour());
        assertEquals(19, dataManager.getEndHour());
        
        // TA Pane
        TeachingAssistant ta = (TeachingAssistant) dataManager.getTeachingAssistants().get(0);
        String taName = ta.getName();
        String taEmail =ta.getEmail();
        Boolean taUndergrad = ta.getUndergrad();
        
        assertEquals("Chase", taName);
        assertEquals("chase@stonybrook.edu", taEmail);
        assertEquals(false, taUndergrad);
        
        // test office hour
        HashMap map = (HashMap)dataManager.getOfficeHours();
        //dataManager.addOfficeHoursReservation("MONDAY",  "10_00am","Haiyang");
        
        SimpleStringProperty officehour =  (SimpleStringProperty) map.get("2_1");
        String str = officehour.get();
        assertEquals("Haiyang",str);
        
        // CD pane
        assertEquals("CSE", dataManager.getSubject());
        assertEquals("219", dataManager.getNumber());
        assertEquals("Fall", dataManager.getSemester());
        assertEquals("2017", dataManager.getYear());
        assertEquals("Computer Science III", dataManager.getTitle());
        assertEquals("Richard McKenna", dataManager.getInstructorName());
        assertEquals("./css/sea_wolf_1.css", dataManager.getStylesheet());
        SitePage site = (SitePage)dataManager.getSitePage().get(0);
        Boolean use = site.isU();
        String navTitle = site.getNav();
        String fileName = site.getFile();
        String script = site.getScr();
        Boolean testUse = true;
        assertEquals(testUse, use);
        assertEquals("Home", navTitle);
        assertEquals("index.html", fileName);
        assertEquals("HomeBuilder.js", script);
        
        // RD Pane
        Recitation rec = (Recitation)dataManager.getRecitation().get(0);
        String section = rec.getSection();
        String dayTime = rec.getTime();
        String location = rec.getLocation();
        assertEquals("R02", section);
        assertEquals("Wed 3:30pm-4:23pm", dayTime);
        assertEquals("Old CS 2114", location);
        
        // SD Pane
        assertEquals("2012-04-23", dataManager.getStartMon());
        assertEquals("2017-04-21", dataManager.getStartFri());
        ScheduleItem si = (ScheduleItem)dataManager.getScheduleItem().get(0);
        String type = si.getType();
        String title = si.getTitle();
        String topic = si.getTopic();
        assertEquals("Holiday", type);
        assertEquals("Spring break", title);
        assertEquals("Event Programming", topic);
        
        // PD Pane
        Team team =(Team) dataManager.getTeam().get(0);
        String name = team.getName();
        String color = team.getColor();
        String textColor = team.getTextColor();
        assertEquals("very good", name);
        assertEquals("553467", color);
        assertEquals("f022cc", textColor);
        
        Student stu = (Student) dataManager.getStudent().get(0);
        String lastName = stu.getLastName();
        String firstName = stu.getFirstName();
        String teamName =stu.getTeam();
        String role = stu.getRole();
        assertEquals("Brummell", lastName);
        assertEquals("Beau", firstName);
        assertEquals("Atomic Comics", teamName);
        assertEquals("Lead Designer", role);
                
  
    }

  
}
