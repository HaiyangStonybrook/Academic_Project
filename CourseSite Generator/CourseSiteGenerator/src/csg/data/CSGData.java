package csg.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import properties_manager.PropertiesManager;
import csg.TAManagerApp;
import csg.TAManagerProp;
import csg.file.CSGFiles;
import csg.workspace.TAController;
import csg.workspace.TAWorkspace;

/**
 * This is the data component for TAManagerApp. It has all the data needed to be
 * set by the user via the User Interface and file I/O can set and get all the
 * data from this object
 *
 * @author Richard McKenna
 */
public class CSGData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    TAManagerApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;

    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;

    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;
    
    // ------- CD Pane Data ---------
    String subject;
    String number;
    String semester;
    String year;
    String title;
    String instructorName;
    String instructorHome;
    ObservableList<SitePage> sitePage;
    String stylesheet;
    String exportDir;
    String templateDir;
    
    String bannerImg;
    String leftImg;
    String rightImg;
    
    // --------- recitation Pane Data ------------
    ObservableList <Recitation> recitation;
    
    // --------- Schedule Pane Data --------------
    String startMon;
    String startFri;
    ObservableList<ScheduleItem> scheduleItem;
    
    // --------- Project Pane Data ---------------
    ObservableList<Team> team;
    ObservableList<Student> student;
    

    /**
     * This constructor will setup the required data structures for use, but
     * will have to wait on the office hours grid, since it receives the
     * StringProperty objects from the Workspace.
     *
     * @param initApp The application this data manager belongs to.
     */
    public CSGData(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;

        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();

        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(TAManagerProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(TAManagerProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
        
        // CD pane 
        subject= new String();
        number= new String();
        semester= new String();
        year= new String();
        title= new String();
        instructorName= new String();
        instructorHome= new String();
        sitePage= FXCollections.observableArrayList();
        stylesheet= new String();
        exportDir = new String();
        bannerImg= new String();
        leftImg= new String();
        rightImg= new String();
        
        // RD pane
        recitation = FXCollections.observableArrayList();
        
        // --------- Schedule Pane --------------
        startMon = new String();
        startFri = new String();
        scheduleItem = FXCollections.observableArrayList();
        
        // --------- Project Pane Data ---------------
        team = FXCollections.observableArrayList();
        student = FXCollections.observableArrayList();
        
    }

    /**
     * Called each time new work is created or loaded, it resets all data and
     * data structures such that they can be used for new values.
     */
    @Override
    public void resetData(){
        resetTAData();
        resetCDData();
        resetRDData();
        resetSDData();
        resetPDData();
    }
    public void resetTAData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;

        CSGFiles file = (CSGFiles) app.getFileComponent();
        
        String startTime = "" + startHour;
        String endTime = "" + endHour;

        String startTimeCombo = file.parseToTime(startTime);
        String endTimeCombo = file.parseToTime(endTime);
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        
        //workspace.setStartTime(startTimeCombo);
        //workspace.setEndTime(endTimeCombo);
        workspace.getStartTime().setValue(startTimeCombo);// start box initial time
        workspace.getEndTime().setValue(endTimeCombo);    // end box initial time

        teachingAssistants.clear();
        officeHours.clear();
    }
    
    public void resetCDData(){
        subject= new String();
        number= new String();
        semester= new String();
        year= new String();
        title= new String();
        instructorName= new String();
        instructorHome= new String();
        sitePage.clear();
        stylesheet= new String();
        exportDir = new String();
        bannerImg= new String();
        leftImg= new String();
        rightImg= new String();
    }

    public void resetRDData(){
        recitation.clear();
        TAController.tps.clearTransaction();
    }
    
    public void resetSDData(){
        scheduleItem.clear();
        startMon = new String();
        startFri = new String();
        TAController.tps.clearTransaction();
    }
    
    public void resetPDData(){
        team.clear();
        student.clear();
        TAController.tps.clearTransaction();
    }
    
    
    // ACCESSOR METHODS
    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int newTime) {
        startHour = newTime;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int newTime) {
        endHour = newTime;
    }

    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList getTeachingAssistants() {
        return teachingAssistants;
    }

    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }

    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    public String getCellKey_int(int col, int row){
        return col+ "_" + row;
    }
    
    public String getCellKey(String day, String time) {
        //CSGFiles file = (CSGFiles) app.getFileComponent();
        int col = gridHeaders.indexOf(day);
        int row = 1;

        String newTime = time.replace("_", ":");
        int milHour = CSGFiles.parseToInteger(newTime);
        row += (milHour - startHour) * 2;
        if (time.contains("_30")) {
            row += 1;
        }
        return getCellKey_int(col, row);
    }

    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
        }
        return null;
    }

    public String getSubject() {
        return subject;
    }

    public String getNumber() {
        return number;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getInstructorHome() {
        return instructorHome;
    }

    public ObservableList<SitePage> getSitePage() {
        return sitePage;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public String getExportDir() {
        return exportDir;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }
    
    public void setExportDir(String exportDir) {
        this.exportDir = exportDir;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(String leftImg) {
        this.leftImg = leftImg;
    }

    public String getRightImg() {
        return rightImg;
    }

    public void setRightImg(String rightImg) {
        this.rightImg = rightImg;
    }

    
    
    public ObservableList<Recitation> getRecitation() {
        return recitation;
    }

    
    public String getStartMon() {
        return startMon;
    }

    public String getStartFri() {
        return startFri;
    }

    public ObservableList<ScheduleItem> getScheduleItem() {
        return scheduleItem;
    }

    public ObservableList<Team> getTeam() {
        return team;
    }

    public ObservableList<Student> getStudent() {
        return student;
    }

    public void setTeachingAssistants(ObservableList<TeachingAssistant> teachingAssistants) {
        this.teachingAssistants = teachingAssistants;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInstructorHome(String instructorHome) {
        this.instructorHome = instructorHome;
    }

    public void setSitePage(ObservableList<SitePage> sitePage) {
        this.sitePage = sitePage;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public void setRecitation(ObservableList<Recitation> recitation) {
        this.recitation = recitation;
    }

    public void setStartMon(String startMon) {
        this.startMon = startMon;
    }

    public void setStartFri(String startFri) {
        this.startFri = startFri;
    }

    public void setScheduleItem(ObservableList<ScheduleItem> scheduleItem) {
        this.scheduleItem = scheduleItem;
    }

    public void setTeam(ObservableList<Team> team) {
        this.team = team;
    }

    public void setStudent(ObservableList<Student> student) {
        this.student = student;
    }

    
    
    /**
     * This method is for giving this data manager the string property for a
     * given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }

    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
            int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }

    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;

        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();

        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        if(app!=null)
        {
        TAWorkspace workspaceComponent = (TAWorkspace) app.getWorkspaceComponent();
        workspaceComponent.reloadOfficeHoursGrid(this);
        }
    }

    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= MIN_START_HOUR)
                && (initEndHour <= MAX_END_HOUR)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    public boolean containsTA(String testName, String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
            if (ta.getEmail().equals(testEmail)) {
                return true;
            }
        }
        return false;
    }

    public void addTA(String initName, String initEmail, Boolean undergrad) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(initName, initEmail, undergrad);

        // ADD THE TA
        if (!containsTA(initName, initEmail)) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }

    public void removeTA(String name) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (name.equals(ta.getName())) {
                teachingAssistants.remove(ta);
                return;
            }
        }
    }

    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }

    /**
     * This function toggles the taName in the cell represented by cellKey.
     * Toggle means if it's there it removes it, if it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();
       
        // IF IT ALREADY HAS THE TA, REMOVE IT
       if(cellText.equals(taName)){
           removeTAFromCell(cellProp, taName);
       } 
       else if (cellText.contains("\n"+taName)||cellText.contains(taName+"\n")) {
            removeTAFromCell(cellProp, taName);
        } // OTHERWISE ADD IT
        else if (cellText.length() == 0) {
            cellProp.setValue(taName);
        } else {
            cellProp.setValue(cellText + "\n" + taName);
        }
    }

    /**
     * This method removes taName from the office grid cell represented by
     * cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        } // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        } // IS IT IN THE MIDDLE OF A LIST OF TAs
        else if (cellText.indexOf(taName) < cellText.indexOf("\n", cellText.indexOf(taName))) {
            int startIndex = cellText.indexOf("\n" + taName);
            int endIndex = startIndex + taName.length() + 1;
            cellText = cellText.substring(0, startIndex) + cellText.substring(endIndex);
            cellProp.setValue(cellText);
        } // IT MUST BE THE LAST TA
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            cellText = cellText.substring(0, startIndex);
            cellProp.setValue(cellText);
        }
    }
    
 //RD:
    
    public void addRecitation(String section, String instructor, String time, String location, String ta1, String ta2) 
    {
        
        Recitation rec = new Recitation(section, instructor,time,location, ta1,ta2);
        
        if (!containsSection(section.toString())) 
        {
            recitation.add(rec);
            
        }
        Collections.sort(recitation);
        
    }
    public boolean containsSection(String sec) {
        for (Recitation rec : recitation) {
            if (rec.getSection().toString().equals(sec)) {
                return true;
            }
        }
        return false;
    }
    //SD:
    
    public void addScheduleItem( String type, String date, String title, 
            String topic, String time, String link, String criteria ) 
    {
        ScheduleItem si = new ScheduleItem( type,  date,  title, 
             topic,  time,  link,  criteria);
        
        if (!containsDate(si.getDate().toString() )) 
        {
            scheduleItem.add(si);
            //app.getGUI().updateToolbarControls(false);
            //app.getGUI().getAppFileController().markFileAsNotSaved();
        }
        Collections.sort(scheduleItem);
        
    }
    public boolean containsDate(String date) {
        for (ScheduleItem si : scheduleItem) {
            if (si.getTime().toString().equals(date)) {
                return true;
            }
        }
        return false;
    }
    //PD:
    
    
    public void addTeam( String name, String color, String textColor, String link ) 
    {
        Team tm = new Team(  name,   color,   textColor,   link);
        
        team.add(tm);
        //app.getGUI().updateToolbarControls(false);
        //app.getGUI().getAppFileController().markFileAsNotSaved();
        
        Collections.sort(team);
        
    }
    
    
    public void addStudent(String firstName, String lastName, String team, String role    ) 
    {
        Student newStudent = new Student(   firstName,   lastName,   team,   role);
        
        if (!containsNameAsStudent(newStudent.getFirstName().toString(), newStudent.getLastName().toString() )) 
        {
            student.add(newStudent);
            //app.getGUI().updateToolbarControls(false);
            //app.getGUI().getAppFileController().markFileAsNotSaved();
        }
        Collections.sort(student);
        
    }
    public boolean containsNameAsStudent(String firstName, String lastName) {
        for (Student s : student) {
            String a = s.getFirstName().toString() + " "+ s.getLastName().toString();
            if ( a.equals(firstName+" "+ lastName)) {
                return true;
            }
        }
        return false;
    }
    //CD methods:
    public void addSitePage( boolean b,String navbarTitle, String fileName, String script  ) 
    {
        SitePage sp = new SitePage(b, navbarTitle, fileName, script);
        
        sitePage.add(sp);
        //app.getGUI().updateToolbarControls(false);
        //app.getGUI().getAppFileController().markFileAsNotSaved();
       
    }
   
}
