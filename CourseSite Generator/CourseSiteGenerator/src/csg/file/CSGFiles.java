package csg.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.TAManagerApp;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.ScheduleItem;
import csg.data.SitePage;
import csg.data.Student;
import csg.data.TeachingAssistant;
import csg.data.Team;
import csg.data.TeamJS;
import csg.workspace.TAWorkspace;
import static djf.settings.AppPropertyType.EXPORT_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.EXPORT_COMPLETED_TITLE;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA manager app. It provides
 * all saving and loading services for the application.
 *
 * @author Richard McKenna
 */
public class CSGFiles implements AppFileComponent {

    // THIS IS THE APP ITSELF
    TAManagerApp app;

    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    
    // ------------- TA Pane ---------------
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_NAME = "name";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_EMAIL = "email";
    static final String JSON_UNDERGRAD = "undergrad";
    static final String JSON_UNDERGRAD_TA = "undergrad_ta"; // this is array 
    
    // ----------- CD Pane --------------
    static final String JSON_CD_SUBJECT = "subjectCD";
    static final String JSON_CD_NUMBER = "numberCD";
    static final String JSON_CD_SEMESTER = "semesterCD";
    static final String JSON_CD_YEAR = "yearCD";
    static final String JSON_CD_TITLE = "titleCD";
    static final String JSON_CD_INSTRUCTOR_NAME = "instructorNameCD";
    static final String JSON_CD_INSTRUCTOR_HOME = "instructorHomeCD";
    static final String JSON_CD_EXPORT_DIR = "exportDirCD";
    
    
    static final String JSON_CD_SITE_PAGE_ARRAY = "sitePageArray"; // this is the array of site page
    static final String JSON_ADD_BANNER_IMG = "addBannerImg";
    static final String JSON_ADD_LEFT_IMG = "addLeftImg";
    static final String JSON_ADD_RIGHT_IMG = "addRightImg";
    static final String JSON_CD_STYLESHEET = "styleSheetCD";
    
    // ------------ site page ---------------
    static final String JSON_USE = "use";
    static final String JSON_NAV_TITLE = "navTitle";
    static final String JSON_FILE_NAME = "fileName";
    static final String JSON_SCRIPT = "script";
    
    // ------------ RD Pane ----------------
    static final String JSON_RECITATION_ARRAY = "recitationArray";
    static final String JSON_SECTION = "sectionRD";
    static final String JSON_DAY_TIME = "dayTimeRD";
    static final String JSON_LOCATION = "locationRD";
    static final String JSON_TA1 = "ta1RD";
    static final String JSON_TA2 = "ta2RD";
    static final String JSON_INSTRUCTOR = "instructorRD";
    
    // ------------- SD Pane ------------------
    static final String JSON_SD_START_MON = "startMon";
    static final String JSON_SD_START_FRI= "startFri";
    static final String JSON_SCHEDULE_ITEM_ARRAY = "schedule_item_Array";
    static final String JSON_SD_TYPE = "typeSD";
    static final String JSON_SD_DATE = "dateSD";
    static final String JSON_SD_TITLE = "titleSD";
    static final String JSON_SD_TOPIC = "topicSD";
    static final String JSON_SD_TIME = "timeSD";
    static final String JSON_SD_LINK = "linkSD";
    static final String JSON_SD_CRITERIA = "criteriaSD";
 
    
    // --------------PD Pane --------------------
    static final String JSON_TEAM_ARRAY = "teamArray";
    static final String JSON_STUDENT_ARRAY = "studentArray";
    
    static final String JSON_PD_NAME = "namePD";
    static final String JSON_PD_COLOR = "colorPD";
    static final String JSON_PD_TEXT_COLOR = "textColorPD";
    static final String JSON_PD_LINK = "linkPD";
    
    static final String JSON_PD_LAST_NAME = "lastNamePD";
    static final String JSON_PD_FIRST_NAME = "firstNamePD";
    static final String JSON_PD_TEAM = "teamPD";
    static final String JSON_PD_ROLE = "rolePD";
    

    public CSGFiles(TAManagerApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        boolean load = false;
        if(app == null)
            load = true;

        // CLEAR THE OLD DATA OUT
        CSGData dataManager = (CSGData) data;

        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // LOAD THE START AND END HOURS
        String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        //int startHourInt = Integer.parseInt(startHour);
        //int endHourInt = Integer.parseInt(endHour);
        dataManager.initHours(startHour, endHour);
        

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
        if (!load)
            app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
        if (load){
            
            //int startHourInt = parseToInteger(startHour);
            //int endHourInt = parseToInteger(endHour);
            //dataManager.setStartHour(startHourInt);
            //dataManager.setEndHour(endHourInt);
            // first row, day
            HashMap officeHoursHash=dataManager.getOfficeHours();
            String[] firstRow = {"Start Time", "End Time", "MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY"};
            for (int col =0; col<6; col++){
                int row = 0;
                String key = col+"_"+row;
                SimpleStringProperty str = new SimpleStringProperty(firstRow[col]);
                officeHoursHash.put(key, str);
            }
            // first col, time
            int hour = dataManager.getStartHour();
            for(int row = 1; row< dataManager.getNumRows(); row++){
                int col = 0;
                String key = col+ "_" +row;
                if((row&1)==0){
                    String evenRow = TAWorkspace.buildCellText(hour, "30");
                    SimpleStringProperty str = new SimpleStringProperty(evenRow);
                    officeHoursHash.put(key, str);
                    hour++;
                }
                else{
                    String oddRow = TAWorkspace.buildCellText(hour, "00");
                    SimpleStringProperty str = new SimpleStringProperty(oddRow);
                    officeHoursHash.put(key, str);
                }
            }
            // other cells
            for(int row = 1; row<dataManager.getNumRows(); row++){
                for(int col = 2; col<7; col++){
                    String key = col +"_"+row;
                    SimpleStringProperty str = new SimpleStringProperty("");
                    officeHoursHash.put(key, str);
                }
            }
            
            
            
        }
        
        ArrayList<String> taNameArrayList = new ArrayList<String>();
        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = json.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            Boolean underGrad = jsonTA.getBoolean(JSON_UNDERGRAD);
            dataManager.addTA(name, email,underGrad );
            
            taNameArrayList.add(name);
        }

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(JSON_DAY);
            String time = jsonOfficeHours.getString(JSON_TIME);
            String name = jsonOfficeHours.getString(JSON_NAME);
            dataManager.addOfficeHoursReservation(day, time, name);
        }
        
        String startHourStr = parseToTime(startHour);
        String endHourStr = parseToTime(endHour);
        
        if(!load){
            TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
            workspace.enableCombox(false);
            workspace.getStartTime().setValue(startHourStr);
            workspace.getEndTime().setValue(endHourStr);
            workspace.enableCombox(true);     
        }
        
        // RD 
        JsonArray rdArray = json.getJsonArray(JSON_RECITATION_ARRAY);
        for (int i = 0; i<rdArray.size(); i++){
            JsonObject jsonRD = rdArray.getJsonObject(i);
            String section = jsonRD.getString(JSON_SECTION);
            String day_time = jsonRD.getString(JSON_DAY_TIME);
            String location = jsonRD.getString(JSON_LOCATION);
            String ta1 = jsonRD.getString(JSON_TA1);
            String ta2 = jsonRD.getString(JSON_TA2);
            String instructor = jsonRD.getString(JSON_INSTRUCTOR);
            
            dataManager.addRecitation(section, instructor, day_time, location, ta1, ta2);
        }
        
        // SD
        JsonArray sdArray = json.getJsonArray(JSON_SCHEDULE_ITEM_ARRAY);
        for (int i = 0; i<sdArray.size(); i++){
            JsonObject jsonSD = sdArray.getJsonObject(i);
            String typeSD = jsonSD.getString(JSON_SD_TYPE);
            String dateSD = jsonSD.getString(JSON_SD_DATE);
            String titleSD = jsonSD.getString(JSON_SD_TITLE);
            String topicSD = jsonSD.getString(JSON_SD_TOPIC);
            String timeSD = jsonSD.getString(JSON_SD_TIME);
            String linkSD = jsonSD.getString(JSON_SD_LINK);
             String criteriaSD = jsonSD.getString(JSON_SD_CRITERIA);
            
            dataManager.addScheduleItem(typeSD, dateSD, titleSD, topicSD, timeSD, linkSD, criteriaSD);
            
        }
        
        String startMon = json.getString(JSON_SD_START_MON);
        String startFri = json.getString(JSON_SD_START_FRI);
        dataManager.setStartMon(startMon);
        dataManager.setStartFri(startFri);
        if(!load){
            TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
            workspace.getMonPicker().setValue(LocalDate.parse(startMon));
            workspace.getFriPicker().setValue(LocalDate.parse(startFri));
        }
        
        // PD
            // team
            ArrayList<String> teamNameList = new ArrayList<String>();
            JsonArray teamArray = json.getJsonArray(JSON_TEAM_ARRAY);
        for (int i = 0; i<teamArray.size(); i++){
            JsonObject jsonTeam = teamArray.getJsonObject(i);
            String teamName = jsonTeam.getString(JSON_PD_NAME);
            String teamColor = jsonTeam.getString(JSON_PD_COLOR);
            String teamTextColor = jsonTeam.getString(JSON_PD_TEXT_COLOR);
            String teamLink = jsonTeam.getString(JSON_PD_LINK);
            
            dataManager.addTeam(teamName, teamColor, teamTextColor, teamLink);
            teamNameList.add(teamName);
        }
            // student
            JsonArray stuArray = json.getJsonArray(JSON_STUDENT_ARRAY);
        for (int i = 0; i<stuArray.size(); i++){
            JsonObject jsonStu = stuArray.getJsonObject(i);
            String stuLastName = jsonStu.getString(JSON_PD_LAST_NAME);
            String stuFirstName = jsonStu.getString(JSON_PD_FIRST_NAME);
            String stuTeam = jsonStu.getString(JSON_PD_TEAM);
            String stuRole = jsonStu.getString(JSON_PD_ROLE);
               
            dataManager.addStudent(stuFirstName, stuLastName, stuTeam, stuRole);    
        }             
        
        //CD
        JsonArray cdArray = json.getJsonArray(JSON_CD_SITE_PAGE_ARRAY);
        for (int i = 0; i<cdArray.size(); i++){
            JsonObject jsonCD = cdArray.getJsonObject(i);
            boolean use = jsonCD.getBoolean(JSON_USE);
            String navTitle = jsonCD.getString(JSON_NAV_TITLE);
            String fileName = jsonCD.getString(JSON_FILE_NAME);
            String script = jsonCD.getString(JSON_SCRIPT);
            
            dataManager.addSitePage(use, navTitle, fileName, script);
        }
        
        String subject = json.getString(JSON_CD_SUBJECT);
        String number = json.getString(JSON_CD_NUMBER);
        String semester = json.getString(JSON_CD_SEMESTER);
        String year = json.getString(JSON_CD_YEAR);
        String titleCD = json.getString(JSON_CD_TITLE);
        String instructorName = json.getString(JSON_CD_INSTRUCTOR_NAME);
        String instructorHome = json.getString(JSON_CD_INSTRUCTOR_HOME);
        String exportDir = json.getString(JSON_CD_EXPORT_DIR);
        String bannerImg = json.getString(JSON_ADD_BANNER_IMG);
        String addLeftImg = json.getString(JSON_ADD_LEFT_IMG);
        String addRightImg = json.getString(JSON_ADD_RIGHT_IMG);
        String styleSheet = json.getString(JSON_CD_STYLESHEET);
        
        dataManager.setSubject(subject);
        dataManager.setNumber(number);
        dataManager.setSemester(semester);
        dataManager.setYear(year);
        dataManager.setTitle(titleCD);
        dataManager.setInstructorName(instructorName);
        dataManager.setInstructorHome(instructorHome);
        dataManager.setExportDir(exportDir);
        dataManager.setBannerImg(bannerImg);
        dataManager.setLeftImg(addLeftImg);
        dataManager.setRightImg(addRightImg);
        dataManager.setStylesheet(styleSheet);
        
        if(!load){
            TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
            workspace.getCourseSubjectCb().setValue(subject);
            workspace.getCourseNumberCb().setValue(number);
            workspace.getCourseSemesterCb().setValue(semester);
            workspace.getCourseYearCb().setValue(year);
            
            workspace.getCourseTitleTf().setText(titleCD);
            workspace.getCourseNameTf().setText(instructorName);
            workspace.getCourseHomeTf().setText(instructorHome);
            
            workspace.getCourseExportDir().setText(exportDir);
            workspace.getStyleSheetCb().setValue(styleSheet);
            
            workspace.getBannerImg(bannerImg);
            workspace.getLeftImg(addLeftImg);
            workspace.getRightImg(addRightImg);
            
            for(String s: teamNameList){
                workspace.getTeamNameList().add(s);
            }
            
            for(String s: taNameArrayList){
                workspace.getSuperTANameList().add(s);
            }
            
        }
        
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
        CSGData dataManager = (CSGData) data;

    // ----------------------------- TA DATA ---------------------------------------
        // NOW BUILD THE TA JSON OBJCTS TO SAVE
        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
        ObservableList<TeachingAssistant> tas = dataManager.getTeachingAssistants();
        for (TeachingAssistant ta : tas) {
            JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_NAME, ta.getName())
                    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_UNDERGRAD, ta.getUndergrad()).build();
            
            taArrayBuilder.add(taJson);
        }
        JsonArray undergradTAsArray = taArrayBuilder.build();

        // NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
        JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
        for (TimeSlot ts : officeHours) {
            JsonObject tsJson = Json.createObjectBuilder()
                    .add(JSON_DAY, ts.getDay())
                    .add(JSON_TIME, ts.getTime())
                    .add(JSON_NAME, ts.getName()).build();
            
            timeSlotArrayBuilder.add(tsJson);
        }
        JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
        // ----------------------------- CD DATA ---------------------------------------
        JsonArrayBuilder cdArrayBuilder = Json.createArrayBuilder();
        ObservableList<SitePage> sitePage = dataManager.getSitePage();
        for (SitePage s : sitePage){
            JsonObject cdJson = Json.createObjectBuilder()
                    .add(JSON_USE, s.isU())
                    .add(JSON_NAV_TITLE, s.getNav().toString())
                    .add(JSON_FILE_NAME, s.getFile().toString())
                    .add(JSON_SCRIPT, s.getScr().toString())
                    .build();
         
            
            cdArrayBuilder.add(cdJson);
        }
        JsonArray cdArray = cdArrayBuilder.build();
        
        // ----------------------------- RD DATA ---------------------------------------
        JsonArrayBuilder rdArrayBuilder = Json.createArrayBuilder();
        ObservableList<Recitation> recitation = dataManager.getRecitation();
        for (Recitation r : recitation){
            JsonObject rdJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, r.getSection().toString())
                    .add(JSON_DAY_TIME, r.getTime().toString())
                    .add(JSON_LOCATION, r.getLocation().toString())
                    .add(JSON_TA1, r.getTa1().toString())
                    .add(JSON_TA2, r.getTa2().toString())
                    .add(JSON_INSTRUCTOR, r.getInstructor().toString())
                    .build();
            
            rdArrayBuilder.add(rdJson);
        }
        JsonArray rdArray = rdArrayBuilder.build();
        
        // ----------------------------- SD DATA ---------------------------------------
        JsonArrayBuilder sdArrayBuilder = Json.createArrayBuilder();
        ObservableList<ScheduleItem> scheduleItem = dataManager.getScheduleItem();
        for (ScheduleItem s : scheduleItem){
            JsonObject sdJson = Json.createObjectBuilder()
                    .add(JSON_SD_TYPE, s.getType().toString())
                    .add(JSON_SD_DATE, s.getDate().toString())
                    .add(JSON_SD_TITLE, s.getTitle().toString())
                    .add(JSON_SD_TOPIC, s.getTopic().toString())
                    .add(JSON_SD_TIME, s.getTime().toString())
                    .add(JSON_SD_LINK, s.getLink().toString())
                    .add(JSON_SD_CRITERIA, s.getCriteria().toString())
                    .build();
            
            
            sdArrayBuilder.add(sdJson);
        }
        JsonArray sdArray = sdArrayBuilder.build();
        
        // ----------------------------- PD DATA ---------------------------------------
        JsonArrayBuilder pdTeamArrayBuilder = Json.createArrayBuilder();
        ObservableList<Team> team = dataManager.getTeam();
        for (Team t : team){
            JsonObject pdTeamJson = Json.createObjectBuilder()
                    .add(JSON_PD_NAME, t.getName().toString())
                    .add(JSON_PD_COLOR, t.getColor().toString())
                    .add(JSON_PD_TEXT_COLOR, t.getTextColor().toString())
                    .add(JSON_PD_LINK, t.getLink().toString())
                    .build();
            
            pdTeamArrayBuilder.add(pdTeamJson);
        }
        JsonArray pdTeamArray = pdTeamArrayBuilder.build();
        
        JsonArrayBuilder pdStudentArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> student = dataManager.getStudent();
        for (Student s : student){
            JsonObject pdStudentJson = Json.createObjectBuilder()
                    .add(JSON_PD_LAST_NAME, s.getLastName().toString())
                    .add(JSON_PD_FIRST_NAME, s.getFirstName().toString())
                    .add(JSON_PD_TEAM, s.getTeam().toString())
                    .add(JSON_PD_ROLE, s.getRole().toString())
                    .build();
            
            pdStudentArrayBuilder.add(pdStudentJson);
        }
        JsonArray pdStudentArray = pdStudentArrayBuilder.build();
        
        // ----------------------------- Build together ---------------------------------------

        JsonObject dataManagerJSO = Json.createObjectBuilder()
                // TA
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
                .add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
                // CD
                .add(JSON_CD_SUBJECT, "" + dataManager.getSubject())
                .add(JSON_CD_NUMBER,  "" + dataManager.getNumber())
                .add(JSON_CD_SEMESTER, "" + dataManager.getSemester())
                .add(JSON_CD_YEAR, "" + dataManager.getYear())
                .add(JSON_CD_TITLE, "" + dataManager.getTitle())
                .add(JSON_CD_INSTRUCTOR_NAME, "" + dataManager.getInstructorName())
                .add(JSON_CD_INSTRUCTOR_HOME, "" + dataManager.getInstructorHome())
                .add(JSON_CD_EXPORT_DIR , "" + dataManager.getExportDir())
                .add(JSON_ADD_BANNER_IMG, "" + dataManager.getBannerImg())
                .add(JSON_ADD_LEFT_IMG, "" + dataManager.getLeftImg())
                .add(JSON_ADD_RIGHT_IMG, "" + dataManager.getRightImg())
                .add(JSON_CD_STYLESHEET, "" + dataManager.getStylesheet())
                .add(JSON_CD_SITE_PAGE_ARRAY, cdArray)
                // RD
                .add(JSON_RECITATION_ARRAY, rdArray)
                // SD
                .add(JSON_SD_START_MON, "" + dataManager.getStartMon())
                .add(JSON_SD_START_FRI, "" + dataManager.getStartFri())
                .add(JSON_SCHEDULE_ITEM_ARRAY, sdArray)
                // PD
                .add(JSON_TEAM_ARRAY, pdTeamArray)
                .add(JSON_STUDENT_ARRAY, pdStudentArray)
                .build(); 
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    public String parseToTime(String hour) {
        Integer hr = Integer.parseInt(hour);
        if (hr == 0) {
            return "12:00am";
        } else if (hr < 12) {
            return "" + hr + ":00am";
        } else if (hr == 12) {
            return "12:00pm";
        } else {
            return "" + (hr - 12) + ":00pm";
        }
    }

    // parse a time string to 24-hour Integer 
    public static int parseToInteger(String str) // qq
    {
        int i = str.length() - 2;
        if (str.charAt(i) == 'a') // am
        {
            if (str.length() == 6) {
                String[] strArr = str.split(":");
                String firstPart = strArr[0];
                int fp = Integer.parseInt(firstPart);
                return fp;
            } else {
                char[] charArr = str.toCharArray();
                char mp = charArr[1];
                if (mp == '0') {
                    return 10;// "10:00am"// 10   
                } else if (mp == '1') {
                    return 11;//"11:00am" //11
                } else {
                    return 0;// "12:00am",// 0
                }
            }
        } else// pm
        {
            if (str.equals("12:00pm") || str.equals("12:30pm")) {
                return 12;
            } else {
                String[] strArr = str.split(":");
                String fp = strArr[0];
                int partInt = Integer.parseInt(fp);
                return partInt + 12;
            }
        }
    }

     public void saveTeamsAndStudents(AppDataComponent data, String filePath) throws IOException 
    {
        //1. GET THE DATA
	CSGData dataManager = (CSGData)data;
        //2. convert to js data :
        ObservableList<TeamJS> teamsJS =  FXCollections.observableArrayList();//
        ObservableList<Team> teams = dataManager.getTeam();
        for(Team t : teams)
        {
            String teamColor = t.getColor();
            teamColor = "#" + teamColor;
            
            String r = ""+ Integer.valueOf( teamColor.substring( 1, 3 ), 16 );
            String g = "" + Integer.valueOf( teamColor.substring( 3, 5 ), 16 );
            String b = "" +Integer.valueOf( teamColor.substring( 5, 7 ), 16 ); 
            
            TeamJS newTeam = new TeamJS( t.getName() , r,g,b, t.getTextColor()    );
            teamsJS.add( newTeam);
            
        }
        
        JsonArrayBuilder pdArrayBuilder_Teams = Json.createArrayBuilder();
        //ObservableList<Team> teams = dataManager.getTeams();
        for (TeamJS tm : teamsJS) {	    
            JsonObject teamsJson = Json.createObjectBuilder()
                    .add( "name"  , tm.getName()    )
                    .add(  "red" ,   tm.getRed()   )
                    .add(  "green"  ,  tm.getGreen() )
                    .add(  "blue"  ,  tm.getBlue())
                    .add(   "text_color" ,  tm.getTextColor())
                  
                    .build();

            pdArrayBuilder_Teams.add(teamsJson);
        }
        JsonArray teamsArray = pdArrayBuilder_Teams.build();
        
        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = dataManager.getStudent();
        for (Student s : students) {	    
            JsonObject studentJson = Json.createObjectBuilder()
                    .add(  "lastName" , s.getLastName().toString() )
                    .add(   "firstName", s.getFirstName().toString())
                    .add( "team" ,s.getTeam().toString())
                    .add("role" ,s.getRole().toString()  )
                   
                    .build();

            studentsArrayBuilder.add(studentJson);
        }
        JsonArray studentsArray = studentsArrayBuilder.build();
        
        // next 
        JsonArrayBuilder spArrayBuilder = Json.createArrayBuilder();
        ObservableList<SitePage>sps = dataManager.getSitePage();
        for(SitePage sp: sps){
            JsonObject spJson = Json.createObjectBuilder()
                    .add("use", sp.isU())
                    .add("navTitle", sp.getNav())
                    .add("fileName", sp.getFile())
                    .build();
            spArrayBuilder.add(spJson);
                
        }
        JsonArray sitePagesArray = spArrayBuilder.build();
        
	// Then put Together
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_CD_SUBJECT, ""+dataManager.getSubject() )
                .add(JSON_CD_NUMBER,""+dataManager.getNumber() )
                .add(JSON_CD_SEMESTER,""+dataManager.getSemester())
                .add(JSON_CD_YEAR,""+dataManager.getYear())
                .add(JSON_CD_TITLE,""+dataManager.getTitle())
                .add(JSON_CD_INSTRUCTOR_NAME,""+dataManager.getInstructorName())
                .add(JSON_CD_INSTRUCTOR_HOME,""+dataManager.getInstructorHome())
                .add(JSON_ADD_BANNER_IMG,""+dataManager.getBannerImg())
                .add(JSON_ADD_LEFT_IMG,""+dataManager.getLeftImg())
                .add(JSON_ADD_RIGHT_IMG,""+dataManager.getRightImg())
                .add(JSON_CD_STYLESHEET,""+dataManager.getStylesheet())
                .add("site_pages", sitePagesArray)
                .add("students", studentsArray)
                .add("teams", teamsArray)
		.build();
	
       
	// JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    public void saveProjectsData(AppDataComponent data, String filePath) throws IOException 
    {
        
        //1. GET THE DATA
	CSGData dataManager = (CSGData)data;
        //2. convert to js data :
        
        JsonArrayBuilder projectsArrayBuilder = Json.createArrayBuilder();
      	ObservableList<Team> teams = dataManager.getTeam();
        for (Team t : teams) {
            
            //--
                ArrayList<String> studentsArray = new ArrayList() ;
                //
                String teamName = t.getName();
                
                ObservableList<Student> allStudents = dataManager.getStudent();
                for(Student stu: allStudents)
                {
                    if(stu.getTeam().equals( teamName  ))
                    {
                        studentsArray.add(   stu.getFirstName() +" " +stu.getLastName() );
                    }
                }
              // make studentsArray  
                String x = studentsArray.toString();
                x=x.replaceAll("\\[","");
                x=x.replaceAll("\\]", "");
                
              // make project Array
            JsonObject projArrJson = Json.createObjectBuilder()
                    .add( "name"  ,t.getName()  )  
                    .add("students",   x )
                    .add("link", t.getLink())
                    
                    .build();

            projectsArrayBuilder.add(projArrJson);
        }
        JsonArray projectsArray = projectsArrayBuilder.build();
        
        JsonArrayBuilder workArrayBuilder = Json.createArrayBuilder();
      	    
            JsonObject workArrJson = Json.createObjectBuilder()
                    .add( "semester"  , dataManager.getSemester()+" "+ dataManager.getYear() )  
                    .add("projects", projectsArray)
                    
                    .build();

            workArrayBuilder.add(workArrJson);
        
        JsonArray workArr = workArrayBuilder.build();
        
  
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		
                //.add("semester", dataManager.getSemester()+" "+ dataManager.getYear())
                
                .add("work", workArr)
		.build();

	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    
    
    // This one shares for: ScheduleDataInHWs.json
    public void saveScheduleData(AppDataComponent data, String filePath) throws IOException 
    {
        //1. GET THE DATA
	CSGData dataManager = (CSGData)data;
        //2. convert to js data :
        
        //"Holiday" ,"Lecture","References","Recitations" , "HW"
       
        JsonArrayBuilder holidayArrayBuilder = Json.createArrayBuilder();
        ObservableList<ScheduleItem> scheduleItems = dataManager.getScheduleItem();
        for (ScheduleItem si : scheduleItems) {
            if(si.getType().equals( "Holiday"))
            {
                JsonObject holidayJson = Json.createObjectBuilder()
                        
                        .add("month", parseToMonth (si.getDate() ) )
                        .add("day",  parseToDay (si.getDate() ) )
                        .add("title"  ,si.getTitle().toString() )
                        .add( "link" , si.getLink().toString()  )
                        
                        .build();

                holidayArrayBuilder.add(holidayJson);
            }
        }
        JsonArray holidaysArray = holidayArrayBuilder.build();
        
        //2.lectures
        JsonArrayBuilder lectureArrayBuilder = Json.createArrayBuilder();
        for (ScheduleItem si : scheduleItems) 
        {
            if(si.getType().equals(  "Lecture"))
            {
                JsonObject lectureJson = Json.createObjectBuilder()
                        
                        .add("month", parseToMonth (si.getDate() ) )
                        .add("day",  parseToDay (si.getDate() ) )
                        .add("title"  ,si.getTitle().toString() )
                        .add("topic", si.getTopic())
                        .add( "link" , si.getLink().toString()  )
                        
                        .build();

                lectureArrayBuilder.add(lectureJson);
            }
        }
        JsonArray lecturesArray = lectureArrayBuilder.build();
        //3.references
        JsonArrayBuilder refArrayBuilder = Json.createArrayBuilder();
        for (ScheduleItem si : scheduleItems) 
        {
            if(si.getType().equals(  "References"))
            {
                JsonObject refJson = Json.createObjectBuilder()
                        
                        .add("month", parseToMonth (si.getDate() ) )
                        .add("day",  parseToDay (si.getDate() ) )
                        .add("title"  ,si.getTitle().toString() )
                        .add("topic", si.getTopic())
                        .add( "link" , si.getLink().toString()  )
                        
                        .build();

                refArrayBuilder.add(refJson);
            }
        }
        JsonArray refArray = refArrayBuilder.build();
        //4.Recitations
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        for (ScheduleItem si : scheduleItems) 
        {
            if(si.getType().equals(  "Recitations"))
            {
                JsonObject recitationsJson = Json.createObjectBuilder()
                        
                        .add("month", parseToMonth (si.getDate() ) )
                        .add("day",  parseToDay (si.getDate() ) )
                        .add("title"  ,si.getTitle().toString() )
                        .add("topic", si.getTopic())                        
                        
                        .build();

                recitationsArrayBuilder.add(recitationsJson);
            }
        }
        JsonArray recitationsArray = recitationsArrayBuilder.build();
        //5.  HW
	JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
        for (ScheduleItem si : scheduleItems) 
        {
            if(si.getType().equals(  "HW"))
            {
                JsonObject hwsJson = Json.createObjectBuilder()
                        
                        .add("month", parseToMonth (si.getDate() ) )
                        .add("day",  parseToDay (si.getDate() ) )
                        .add("title"  ,si.getTitle().toString() )
                        .add("topic", si.getTopic())
                        .add( "link" , si.getLink().toString()  )
                        .add("time",  si.getTime() )
                        .add("criteria",  si.getCriteria() )
                        
                        .build();

                hwsArrayBuilder.add(hwsJson);
            }
        }
        JsonArray hwsArray = hwsArrayBuilder.build();
        
	// THEN PUT IT ALL IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		
                .add("startingMondayMonth" , parseToMonth ( dataManager.getStartMon() ) )
                .add("startingMondayDay",   parseToDay( dataManager.getStartMon()  )    )
                .add("endingFridayMonth",  parseToMonth(dataManager.getStartFri() ))
                .add("endingFridayDay", parseToDay(dataManager.getStartFri()))
                .add( "holidays" , holidaysArray)
                .add( "lectures" , lecturesArray)
                .add( "references" , refArray)
                .add( "recitations" , recitationsArray )
                .add( "hws" , hwsArray)
		.build();
	 
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    
    }
    public void saveOfficeHoursGridData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
	CSGData dataManager = (CSGData)data;
        
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
	ObservableList<TeachingAssistant> tas = dataManager.getTeachingAssistants();
	for (TeachingAssistant ta : tas) {	    
	    JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_NAME, ta.getName() )
                    .add(JSON_EMAIL, ta.getEmail() )
                    .add( JSON_UNDERGRAD, ta.getUndergrad()).build();
            
	    taArrayBuilder.add(taJson);
	}
	JsonArray undergradTAsArray = taArrayBuilder.build();

	// NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
	JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
	ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
	for (TimeSlot ts : officeHours) {	    
	    JsonObject tsJson = Json.createObjectBuilder()
		    .add(JSON_DAY, ts.getDay())
		    .add(JSON_TIME, ts.getTime())
		    .add(JSON_NAME, ts.getName()).build();
	    timeSlotArrayBuilder.add(tsJson);
	}
	JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    public void saveRecitationsData(AppDataComponent data, String filePath) throws IOException 
    {
        
        //1. GET THE DATA
	CSGData dataManager = (CSGData)data;
        //2. convert to js data :
        
        JsonArrayBuilder rdArrayBuilder = Json.createArrayBuilder();
        ObservableList<Recitation> recitations = dataManager.getRecitation();
        for (Recitation rec : recitations) {	    
            JsonObject recitationJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, rec.getSection().toString() + " " +"(" + rec.getInstructor().toString()  +")" )
                    .add(JSON_DAY_TIME, rec.getTime().toString()   )
                    .add( JSON_LOCATION,rec.getLocation().toString())
                    .add( JSON_TA1, rec.getTa1().toString() )
                    .add( JSON_TA2, rec.getTa2().toString() )
                    .build();

            rdArrayBuilder.add(recitationJson);
        }
        JsonArray rdArray = rdArrayBuilder.build();
        
        
	// THEN PUT IT  IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		
                .add( "recitations" , rdArray)
                
		.build();
        
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData() throws IOException
    {
        TAWorkspace work  = (TAWorkspace)app.getWorkspaceComponent();
        String exportPath = work.getCourseExportDir().getText(); // "file:./exportData"
        
        String templatePath = work.getSubSiteTemplateDir().getText();
                        //"file:./templates/CSE219"
        
        CSGData dataManager = (CSGData)app.getDataComponent();
        
         //PRECONDITIONS:
        boolean continueToExport = false;
        ObservableList<SitePage> sitePagesList = dataManager.getSitePage();
        if (sitePagesList.size()==0)
            return;
        
        for(SitePage si : sitePagesList)
        {
            if(si.isU()==true)
                continueToExport = true;
        }
        if(!continueToExport)
            return;
        //====
        
        
        //from templatePath -> exportPath  (.../exportData)
         //1. check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
         //  2.  check to  parse source path 
        if(templatePath.startsWith("file:."))
            templatePath = templatePath.replace("file:.",  System.getProperty("user.dir"));
        
        //First, copy everything 
        FileUtils.copyDirectoryToDirectory( new File(templatePath), new File(exportPath));
        
        //Second, replace all jsons 
                            // ( exportPath : .../exportData )
        
        
        // 1. get second part(CSE219)
        int indexSecondPart = templatePath.lastIndexOf("/")+1;
        String secondPart = templatePath.substring(indexSecondPart);//CSE219
        
        //2. get dirs
        String parentDir = exportPath+"/" + secondPart +"/public_html"; 
        String jsDir = parentDir +"/js"; 
        
        //3. substitute 6 json files under var jsDir
        
        //  #1
        String path = jsDir  + "/TeamsAndStudents.json";
        try {
            saveTeamsAndStudents(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        //#2
        path = jsDir  + "/ScheduleDataInHWs.json";
        try {
            saveScheduleData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        //#3 and #4
        path =    jsDir  + "/OfficeHoursGridData.json";
        try {
            saveOfficeHoursGridData(app.getDataComponent(), path );
            
            path = jsDir  + "/RecitationsData.json";

            saveRecitationsData( app.getDataComponent(), path );
        } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //#5
        path =  jsDir  + "/ScheduleData.json";
        try {
            saveScheduleData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        //#6
        
        path =  jsDir  + "/ProjectsData.json";
        try {
            saveProjectsData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //4. Delete unrelated html file(s)
        ObservableList<SitePage> sitePages = dataManager.getSitePage();
        
        
        for( SitePage si : sitePages)
        {
            if(si.isU()==false)
            {
                String pathTBD = parentDir   + "/" +  si.getFile();
                File file = new File(pathTBD);
                file.delete();
                
            }
            
        }
        
        
        // TELL THE USER THE FILE HAS BEEN exported
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        dialog.show(props.getProperty(EXPORT_COMPLETED_TITLE),props.getProperty(EXPORT_COMPLETED_MESSAGE));

        /*
        ObservableList<SitePage> sitePages = dataManager.getSitePage();
        boolean incorrectFormatException = false;
        for( SitePage si : sitePages)
        {
            if(si.isU())
            {
                String navbarTitle = si.getNav();
                if( navbarTitle.equalsIgnoreCase("home") )
                {
                    exportHome(templatePath, exportPath);
                }
                else if( navbarTitle.equalsIgnoreCase("hws") )
                {
                    exportHWs(templatePath, exportPath);
                }
                else if(navbarTitle.equalsIgnoreCase("projects") )
                {
                    exportProjects(templatePath, exportPath);
                }
                else if(navbarTitle.equalsIgnoreCase("schedule") )
                {
                    exportSchedule(templatePath, exportPath);
                }
                else if(navbarTitle.equalsIgnoreCase("syllabus") )
                {
                    exportSyllabus(templatePath, exportPath);
                }
                else
                {
                    //not formatted with directory name above.
                    incorrectFormatException = true;
                }
            
            }
            
        }
        if(!incorrectFormatException)
        {
        // TELL THE USER THE FILE HAS BEEN exported
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show(props.getProperty(EXPORT_COMPLETED_TITLE),props.getProperty(EXPORT_COMPLETED_MESSAGE));
        }*/
        
    }
    /*
    public void exportSyllabus(String fromTempPath, String toExportPath)
    {
        //fromTempPath : .../CSE219       toExportPath: .../exportData
        
        //1. get second part(CSE219) from fromTempPath
        int indexSecondPart = fromTempPath.lastIndexOf("/")+1;
        String secondPart = fromTempPath.substring(indexSecondPart);//CSE219
        
        //2. create exporting path-DIRECTORY
        String exportPath = toExportPath+"/"+ secondPart ; //       .../exportData/CSE219
        
        // check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
        // create file
        File dirExportChildPath = new File(exportPath);
        
        if(!dirExportChildPath.exists() && !dirExportChildPath.isDirectory())
        {
            dirExportChildPath.mkdirs();    //final destination : dirExportChildPath
        }
        //3. get source path 
        //      parse source path if not formatted well.
        if(fromTempPath.startsWith("file:."))
            fromTempPath = fromTempPath.replace("file:.",  System.getProperty("user.dir"));
        
        fromTempPath  = fromTempPath +"/Syllabus"; // dig into child  (final source path)
        
        try {
            FileUtils.copyDirectoryToDirectory( new File( fromTempPath) ,dirExportChildPath );
            } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String officeHoursGridDataPath =    exportPath  + "/Syllabus/public_html/js/OfficeHoursGridData.json";
        try {
            saveOfficeHoursGridData(app.getDataComponent(), officeHoursGridDataPath );
            
            String recitationsDataPath =    exportPath  + "/Syllabus/public_html/js/RecitationsData.json";

            saveRecitationsData( app.getDataComponent(), recitationsDataPath );
        } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // -------------------- export schedule ---------------------
    public void exportSchedule(String fromTempPath, String toExportPath)
    {
        //fromTempPath : .../CSE219       toExportPath: .../exportData
        
        //1. get second part(CSE219) from fromTempPath
        int indexSecondPart = fromTempPath.lastIndexOf("/")+1;
        String secondPart = fromTempPath.substring(indexSecondPart);//CSE219
        
        //2. create exporting path-DIRECTORY
        String exportPath = toExportPath+"/"+ secondPart ; //       .../exportData/CSE219
        
        // check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
        // create file
        File dirExportChildPath = new File(exportPath);
        
        if(!dirExportChildPath.exists() && !dirExportChildPath.isDirectory())
        {
            dirExportChildPath.mkdirs();    //final destination : dirExportChildPath
        }
        //3. get source path 
        //      parse source path if not formatted well.
        if(fromTempPath.startsWith("file:."))
            fromTempPath = fromTempPath.replace("file:.",  System.getProperty("user.dir"));
        
        fromTempPath  = fromTempPath +"/Schedule"; // dig into child  (final source path)
        
        try {
            FileUtils.copyDirectoryToDirectory( new File( fromTempPath) ,dirExportChildPath );
            } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //
        String path =    exportPath  + "/Schedule/public_html/js/ScheduleData.json";
        try {
            saveScheduleData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    // -------------- export projects-------------
    public void exportProjects(String fromTempPath, String toExportPath)
    {
        //fromTempPath : .../CSE219       toExportPath: .../exportData
        
        //1. get second part(CSE219) from fromTempPath
        int indexSecondPart = fromTempPath.lastIndexOf("/")+1;
        String secondPart = fromTempPath.substring(indexSecondPart);//CSE219
        
        //2. create exporting path-DIRECTORY
        String exportPath = toExportPath+"/"+ secondPart ; //       .../exportData/CSE219
        
        // check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
        // create file
        File dirExportChildPath = new File(exportPath);
        
        if(!dirExportChildPath.exists() && !dirExportChildPath.isDirectory())
        {
            dirExportChildPath.mkdirs();    //final destination : dirExportChildPath
        }
        //3. get source path 
        //      parse source path if not formatted well.
        if(fromTempPath.startsWith("file:."))
            fromTempPath = fromTempPath.replace("file:.",  System.getProperty("user.dir"));
        
        fromTempPath  = fromTempPath +"/Projects"; // dig into child  (final source path)
        
        try {
            FileUtils.copyDirectoryToDirectory( new File( fromTempPath) ,dirExportChildPath );
            } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //
        String path =    exportPath  + "/Projects/public_html/js/ProjectsData.json";
        try {
            saveProjectsData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    // ---------------------- export HW ---------------------------
    public void exportHWs(String fromTempPath, String toExportPath)
    {

        //fromTempPath : .../CSE219       toExportPath: .../exportData
        
        //1. get second part(CSE219) from fromTempPath
        int indexSecondPart = fromTempPath.lastIndexOf("/")+1;
        String secondPart = fromTempPath.substring(indexSecondPart);//CSE219
        
        //2. create exporting path-DIRECTORY
        String exportPath = toExportPath+"/"+ secondPart ; //       .../exportData/CSE219
        
        // check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
        // create file
        File dirExportChildPath = new File(exportPath);
        
        if(!dirExportChildPath.exists() && !dirExportChildPath.isDirectory())
        {
            dirExportChildPath.mkdirs();    //final destination : dirExportChildPath
        }
        //3. get source path 
        //      parse source path if not formatted well.
        if(fromTempPath.startsWith("file:."))
            fromTempPath = fromTempPath.replace("file:.",  System.getProperty("user.dir"));
        
        fromTempPath  = fromTempPath +"/HWs"; // dig into child  (final source path)
        
        try {
            FileUtils.copyDirectoryToDirectory( new File( fromTempPath) ,dirExportChildPath );
            } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //
        String path = exportPath  + "/HWs/public_html/js/ScheduleDataInHWs.json";
        try {
            saveScheduleData(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    public void exportHome(String fromTempPath, String toExportPath)
    {
        //fromTempPath : .../CSE219       toExportPath: .../exportData
        
        //1. get second part(CSE219) from fromTempPath
        int indexSecondPart = fromTempPath.lastIndexOf("/")+1;
        String secondPart = fromTempPath.substring(indexSecondPart);//CSE219
        
        //2. create exporting path-DIRECTORY
        String exportPath = toExportPath+"/"+ secondPart ; //       .../exportData/CSE219
        
        // check if parse needed
        if(exportPath.startsWith("file:."))
            exportPath = exportPath.replace("file:.",  System.getProperty("user.dir"));
        
        // create file
        File dirExportChildPath = new File(exportPath);
        
        if(!dirExportChildPath.exists() && !dirExportChildPath.isDirectory())
        {
            dirExportChildPath.mkdirs();    //final destination : dirExportChildPath
        }
        //3. get source path 
        //      parse source path if not formatted well.
        if(fromTempPath.startsWith("file:."))
            fromTempPath = fromTempPath.replace("file:.",  System.getProperty("user.dir"));
        
        fromTempPath  = fromTempPath +"/Home"; // dig into child  (final source path)
        
        try {
            FileUtils.copyDirectoryToDirectory( new File( fromTempPath) ,dirExportChildPath );
            } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //
        String path = exportPath  + "/Home/public_html/js/TeamsAndStudents.json";
        try {
            saveTeamsAndStudents(app.getDataComponent(), path );
            
         } catch (IOException ex) {
            Logger.getLogger(CSGFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
          
    }*/
    public String parseToDay(String s)
    {
            String day  = s.substring(8);
            int dayInt = Integer.parseInt( day);
            
            return dayInt+"";
            
    }
        
    public String parseToMonth(String s)
    {
        String month = s.substring(  5,7);
        
        int monthInt = Integer.parseInt( month);
        
        return monthInt+"";
        
    }

    //@Override
    //public void exportData(AppDataComponent data, String filePath) throws IOException {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   // }


    
}
