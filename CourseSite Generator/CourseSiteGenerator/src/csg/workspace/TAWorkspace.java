package csg.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import csg.TAManagerApp;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import static javafx.scene.input.KeyCombination.keyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.TAManagerProp;
import static csg.TAManagerProp.ADD_ICON;
import static csg.TAManagerProp.CHANGE_ICON;
import static csg.TAManagerProp.CLEAR_ICON;
import static csg.TAManagerProp.REMOVE_ICON;
import static csg.TAManagerProp.YALE_CS_ICON;
import static csg.TAManagerProp.YALE_UNIVERSITY_ICON;
import csg.data.SitePage;
import csg.style.TAStyle;
import csg.data.CSGData;
import csg.data.Recitation;
import csg.data.ScheduleItem;
import csg.data.Site;
import csg.data.Student;
import csg.data.TeachingAssistant;
import csg.data.Team;
import csg.file.CSGFiles;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
 * @author Richard McKenna
 */
public class TAWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    TAManagerApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    TAController controller;
    boolean enable;
    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    
    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    TableColumn<TeachingAssistant, Boolean> undergradColumn;

    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    TextField emailTextField;
    Button addButton;
    Button clearButton;
    ComboBox startBox;
    ComboBox endBox;
    boolean doubleListen;

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    HBox CBox;
    final KeyCombination KeyCombControlZ;
    final KeyCombination KeyCombControlY;
    
    String addBtText;
    String clearBtText;
    String changeBtText;
    
     
    
    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;
    
    // tab pane
    TabPane tabPane;
    Tab courseTab;
    Tab taTab ;
    Tab recitationTab;
    Tab scheduleTab;
    Tab projectTab;

    /* course pane*/
    VBox coursePane ;
    VBox courseUpperPane;
    VBox courseMiddlePane ;
    VBox courseLowerPane ;
    Label courseInfo;
    Label siteTemplate;
    Label pageStyle;
    TableView courseMidTable;
    TableColumn<SitePage, Boolean> checkUseCol;
    TableColumn navbarColnew; 
    TableColumn fileNameCol;
    TableColumn scriptCol;
    Label sitePages;
    ImageView image_yale;
    ImageView image_yale_cs;
    ImageView image_yale2;
    
    String yale_university;
    String yale_department;
    
    ComboBox courseSubjectCb;
    ComboBox courseNumberCb;
    ComboBox courseSemesterCb;
    ComboBox courseYearCb;
    ComboBox styleSheetCb;
    
    TextField courseTitleTf;
    TextField courseNameTf;
    TextField courseHomeTf;
    
    String defaultExportDir;
    String defaultTemplateDir;
    Label courseExportDir;
    Label subSiteTemplateDir;
            
    
    
    
    /* recitation pane*/
    VBox recitationPane;
    VBox recitationUpperPane;
    VBox recitationLowerPane;
    HBox recitationHeaderPane;
    TableView recitationTable;
    Label recitationHeader;
    Label recitationAddHeader ;
    
    ObservableList<String> superTANameList;
    
    TextField recitationSectionTf ;
    TextField recitationInstructorTf ;
    TextField recitationDayTf ;
    TextField recitationLocationTf ;
    
    ComboBox recitationTA1Cb ;
    ComboBox recitationTA2Cb;
    /* schedule pane */
    VBox schedulePane;
    VBox scheduleUpperPane;
    HBox calendarPane;
    VBox scheduleLowerPane;
    HBox scheculeItemPane;
    Label scheduleHeader;
    Label calendarHeader;
    Label scheduleItemHeader;
    TableView scheduleTable;
    Label scheduleAddHeader;
    DatePicker monPicker;
    DatePicker friPicker;
    DatePicker scheduleDateDp;
    ComboBox scheduleTypeCb;
    
    TextField scheduleTimeTf;
    TextField scheduleTitleTf;
    TextField scheduleTopicTf ;
    TextField scheduleLinkTf;
    TextField scheduleCriteriaTf;
    
    
    /* project pane */
    Label projectHeader; 
    VBox projectPane;
    VBox projectUpperPane;
    VBox projectLowerPane;
    HBox teamHbox;
    TableView teamTable;
    HBox studentHbox;
    TableView studentTable;
    Label teamHeader;
    Label teamAddHeader;
    Label studentHeader;
    Label studentAddHeader;
    ObservableList <String> teamNameList;
    TextField teamNameTf ;
    ColorPicker teamColorCp ;
    ColorPicker teamTextColorCp ;
    TextField teamLinkTf ;
    TextField studentFirstNameTf;
    TextField studentLastNameTf;
    TextField studentRoleTf;
    ComboBox studentTeamCb;
    
    
    
    /**
     * The contstructor initializes the user interface, except for
     * the full office hours grid, since it doesn't yet know what
     * the hours will be until a file is loaded or a new one is created.
     */
    public TAWorkspace(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        enable = true;
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(TAManagerProp.TAS_HEADER_TEXT.toString());
        tasHeaderLabel = new Label(tasHeaderText);
        tasHeaderBox.getChildren().add(tasHeaderLabel);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        CSGData data = (CSGData) app.getDataComponent();
        CSGFiles file = (CSGFiles)app.getFileComponent();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        String nameColumnText = props.getProperty(TAManagerProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(TAManagerProp.EMAIL_COLUMN_TEXT.toString());
        nameColumn = new TableColumn(nameColumnText);
        emailColumn = new TableColumn(emailColumnText);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        
        
        // --------------- add undergrad checkBox column here --------------------
        String undergradColumnText = props.getProperty(TAManagerProp.UNDER_COLUMN_TEXT.toString());
        undergradColumn = new TableColumn<TeachingAssistant, Boolean>(undergradColumnText);
        undergradColumn.setCellValueFactory(new Callback<CellDataFeatures<TeachingAssistant, Boolean>, ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<TeachingAssistant, Boolean> ex){
                TeachingAssistant person = ex.getValue();
                
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.getUndergrad());
                
                // column changes
                booleanProp.addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldvalue, Boolean newValue){
                        person.setUndergrad(newValue);
                        controller.handleUndergradToggle(person);
                        
                        app.getGUI().updateToolbarControls(false);
                        
                        }
                    });
                    return booleanProp;
                }
            }
        );
        
        undergradColumn.setCellFactory(new Callback<TableColumn<TeachingAssistant, Boolean>, TableCell<TeachingAssistant, Boolean>>(){
            @Override
            public TableCell<TeachingAssistant,Boolean> call (TableColumn<TeachingAssistant, Boolean> ex){
                CheckBoxTableCell<TeachingAssistant, Boolean> cell = new CheckBoxTableCell<TeachingAssistant, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(.3));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(.6));
        undergradColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(.1));
        taTable.setEditable(true);
        
        //taTable.getColumns().add(nameColumn);
        //taTable.getColumns().add(emailColumn);
        taTable.getColumns().addAll(undergradColumn, nameColumn, emailColumn);
        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(TAManagerProp.NAME_PROMPT_TEXT.toString());
        String emailPromptText = props.getProperty(TAManagerProp.EMAIL_PROMPT_TEXT.toString());
        String addButtonText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString());
        String clearButtonText = props.getProperty(TAManagerProp.CLEAR_BUTTON_TEXT.toString());
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        //setButtonImage(addButton,ADD_ICON.toString());
        
        clearButton=new Button(clearButtonText);
        //clearButton.setText(changeBtText);
        //setButtonImage(clearButton,CLEAR_ICON.toString());
        
        addBox = new HBox();
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.21));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.19));
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);
        clearButton.setDisable(true);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        CBox = new HBox();
        ObservableList<String> timeList = FXCollections.observableArrayList(
        "12:00am","1:00am","2:00am","3:00am","4:00am","5:00am","6:00am",
        "7:00am","8:00am","9:00am","10:00am","11:00am","12:00pm","1:00pm",
        "2:00pm","3:00pm","4:00pm","5:00pm","6:00pm","7:00pm",
        "8:00pm","9:00pm","10:00pm","11:00pm"
        );
        
        String combBoxStartTimeStr = props.getProperty(TAManagerProp.START_COMBO_TEXT.toString());
        String combBoxEndTimeStr = props.getProperty(TAManagerProp.END_COMBO_TEXT.toString());
      
        Label combBoxStartTimeLabel = new Label(combBoxStartTimeStr);
        combBoxStartTimeLabel.setStyle("-fx-font-size: 16pt;");
        combBoxStartTimeLabel.setPadding(new Insets(0,10,0,0));
        
        Label combBoxEndTimeLabel = new Label(combBoxEndTimeStr);
        combBoxEndTimeLabel.setStyle("-fx-font-size: 16pt;");
        combBoxEndTimeLabel.setPadding(new Insets(0,15,0,30));
        
        startBox = new ComboBox(timeList);
        endBox = new ComboBox(timeList);
        
        CBox.getChildren().addAll(combBoxStartTimeLabel,startBox,combBoxEndTimeLabel,endBox);
        CBox.setPadding(new Insets(0,0,0,100));
        String officeHoursGridText = props.getProperty(TAManagerProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        officeHoursHeaderBox.getChildren().add(CBox);
            
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);        
        leftPane.getChildren().add(taTable);        
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(rightPane));
//------------------- this is tabPane --------------------------
        tabPane = new TabPane();
        tabPane.setTabMaxWidth(270);
        tabPane.setTabMinWidth(270);
        
        //tabPane.setPrefWidth(550);
        
        String courseStr=props.getProperty(TAManagerProp.COURSE.toString());
        String taDataStr=props.getProperty(TAManagerProp.TADATA.toString());
        String recitationStr=props.getProperty(TAManagerProp.RECITATION.toString());
        String scheduleStr=props.getProperty(TAManagerProp.SCHEDULE.toString());
        String projectStr=props.getProperty(TAManagerProp.PROJECT.toString());
                
        courseTab = new Tab(courseStr);
        taTab = new Tab(taDataStr);
        recitationTab = new Tab(recitationStr);
        scheduleTab = new Tab(scheduleStr);
        projectTab = new Tab(projectStr);
        
        
        
        tabPane.getTabs().add(courseTab);
        tabPane.getTabs().add(taTab);
        tabPane.getTabs().add(recitationTab);
        tabPane.getTabs().add(scheduleTab);
        tabPane.getTabs().add(projectTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        taTab.setContent(sPane);
        
        workspace = new BorderPane();// tabpane
        //workspace.setMaxWidth(800);
        //workspace.setMinWidth(800);
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(tabPane);
        
        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));

        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new TAController(app);

        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        
      
        addButton.setOnAction(e -> {
        controller.handleAddTA();
        });
        
            
        clearButton.setOnAction(e -> {
            controller.handleClearTA();
        });
        
        taTable.setOnMouseClicked(e -> {
            controller.handleSelectTA();
            clearButton.setDisable(false);
        });
        
        taTable.setFocusTraversable(true);
        taTable.setOnKeyPressed(e -> {
            controller.handleKeyPress(e.getCode());
        });
        
        startBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            
            @Override
            public void changed(ObservableValue ov, Object oldTime, Object newTime) {
                //if(oldTime!=null)
                //controller.handleStartTime(oldTime, newTime);
                if(enable)
                    controller.handleStartTime(oldTime, newTime);
            }
        });
        
        endBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            
            @Override
            public void changed(ObservableValue ov, Object oldTime, Object newTime) {
                if(enable)
                controller.handleEndTime(oldTime, newTime);
                //if(doubleListen)
                   // controller.handleStartTime(oldTime, newTime);
            }
        });
        
        // control+z, control+y
        KeyCombControlZ = new KeyCodeCombination(KeyCode.Z,KeyCombination.CONTROL_DOWN);
        KeyCombControlY = new KeyCodeCombination(KeyCode.Y,KeyCombination.CONTROL_DOWN);
        
        app.getGUI().getAppPane().setOnKeyPressed(e->{
            
            if(KeyCombControlZ.match(e)){
                TAController.tps.undoTransaction();
            }
            else if(KeyCombControlY.match(e)){
                TAController.tps.doTransaction();
            } 
        });
        
        app.getGUI().getUndoButton().setOnMouseClicked(e-> {
            TAController.tps.undoTransaction();
            if (TAController.tps.getMostRecentTransaction()<0)
                app.getGUI().getUndoButton().setDisable(true);
            
             
        });
        
        app.getGUI().getRedoButton().setOnMouseClicked(e->{
            TAController.tps.doTransaction();
            app.getGUI().getUndoButton().setDisable(false);
            
        });
        
//---------------------here is another 4 tabPane--------------------------------
//-------------------------------------------------------------------------------

    addBtText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT_1.toString());
    changeBtText = props.getProperty(TAManagerProp.CHANGE_BUTTON_TEXT.toString());
    clearBtText = props.getProperty(TAManagerProp.CLEAR_BUTTON_TEXT_1.toString());
// ------------------- here is Course Deatil pane -------------------------------
//-------------------------------------------------------------------------------
     coursePane = new VBox();
     courseUpperPane = new VBox();
     courseMiddlePane = new VBox();
     courseLowerPane = new VBox();
     
    // course detail upper pane
    // 1 gridpane
     courseInfo = new Label(props.getProperty(TAManagerProp.COURSE_INFO_HEADER.toString()));
     courseInfo.setPadding(new Insets(20,0,20,0));
    
    GridPane course1 = new GridPane();
    
    Label courseSubject = new Label(props.getProperty(TAManagerProp.COURSE_INFO_SUBJECT.toString()));
    ObservableList<String> subjectList = FXCollections.observableArrayList("CSE", "ISE", "CE");
    courseSubjectCb= new ComboBox(subjectList);
    courseSubjectCb.setPrefWidth(100);
    
    Label courseNumber = new Label(props.getProperty(TAManagerProp.COURSE_INFO_NUMBER.toString()));
    ObservableList<String> numberList = FXCollections.observableArrayList("114", "214", "219", "308", "380");
    courseNumberCb= new ComboBox(numberList);
    courseNumberCb.setPrefWidth(100);
    
    Label courseSemester = new Label(props.getProperty(TAManagerProp.COURSE_INFO_SEMESTER.toString()));
    ObservableList<String> semesterList = FXCollections.observableArrayList("Fall", "Winter", "Spring", "Summer");
    courseSemesterCb = new ComboBox(semesterList);
    courseSemesterCb.setPrefWidth(100);
    
    Label courseYear = new Label(props.getProperty(TAManagerProp.COURSE_INFO_YEAR.toString()));
    ObservableList<String> yearList = FXCollections.observableArrayList("2017", "2018", "2019");
    courseYearCb= new ComboBox(yearList);
    courseYearCb.setPrefWidth(100);
    
    Label courseTitle = new Label(props.getProperty(TAManagerProp.COURSE_INFO_TITLE.toString()));
    courseTitleTf = new TextField();
    String courseTitlePromptText = props.getProperty(TAManagerProp.COURSE_TITLE_PROMT.toString());
    courseTitleTf.setPromptText(courseTitlePromptText);
    courseTitleTf.setPrefWidth(300);
    
    Label courseName = new Label(props.getProperty(TAManagerProp.COURSE_INFO_INSTRUCTOR.toString()));
    String courseNamePromptText = props.getProperty(TAManagerProp.COURSE_INSTRUCTOR_NAME_PROMT.toString());
    courseNameTf = new TextField();
    courseNameTf.setPromptText(courseNamePromptText );
    courseNameTf.setPrefWidth(300);
    
    Label courseHome = new Label(props.getProperty(TAManagerProp.COURSE_INFO_HOME.toString()));
    String courseHomePromptText = props.getProperty(TAManagerProp.COURSE_INSTRUCTOR_HOME_PROMT.toString());
    courseHomeTf = new TextField();
    courseHomeTf.setPromptText(courseHomePromptText  );
    courseHomeTf.setPrefWidth(300);
    
    Label courseExport = new Label(props.getProperty(TAManagerProp.COURSE_INFO_EXPORT.toString()));
    defaultExportDir = "file:./exportData";
    courseExportDir = new Label(defaultExportDir);
    Button courseInfoButton = new Button();
    
    courseInfoButton.setText(changeBtText);
    //setButtonImage(courseInfoButton,CHANGE_ICON.toString());
    courseInfoButton.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CHANGE_TOOLTIP.toString())));
    
    
    course1.add(courseSubject, 0, 0);
    course1.add(courseSubjectCb, 1, 0);
    course1.add(courseNumber, 2, 0);
    course1.add(courseNumberCb, 3, 0);
    course1.add(courseSemester, 0, 1);
    course1.add(courseSemesterCb, 1, 1);
    course1.add(courseYear, 2, 1);
    course1.add(courseYearCb, 3, 1);
    course1.add(courseTitle, 0, 2);
    course1.add(courseTitleTf, 1, 2);
    course1.add(courseName, 0, 3);
    course1.add(courseNameTf, 1, 3);
    course1.add(courseHome, 0, 4);
    course1.add(courseHomeTf, 1, 4);
    course1.add(courseExport, 0, 5);
    course1.add(courseExportDir, 1, 5);
    course1.add(courseInfoButton, 2, 5);
    course1.setHgap(20);
    course1.setVgap(20);
    course1.setPadding(new Insets(0,0,20,0));
    courseUpperPane.getChildren().add(courseInfo);
    courseUpperPane.getChildren().add(course1);
    
    
    // course detail middle pane
    // cdm1
    siteTemplate = new Label(props.getProperty(TAManagerProp.SITE_TEMPLATE_HEADER.toString()));
    siteTemplate.setPadding(new Insets(20,0,20,0));
    Label subSiteTemplate = new Label(props.getProperty(TAManagerProp.SITE_TEMPLATE_EXPLATION.toString()));
    subSiteTemplate.setPadding(new Insets(0,0,20,0));
    
    defaultTemplateDir = "file:./templates/CSE219";
    subSiteTemplateDir = new Label(defaultTemplateDir);
    subSiteTemplateDir.setPadding(new Insets(0,0,20,0));
    Button selectTempDir = new Button();
    //setButtonImage(selectTempDir,CHANGE_ICON.toString());
    String seletTempDirText = props.getProperty(TAManagerProp.COURSE_SELETCT_TEMPLATE_BUTTON.toString());
    selectTempDir.setText(seletTempDirText);
    selectTempDir.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CHANGE_TOOLTIP.toString()))); // set button image
    selectTempDir.setPadding(new Insets(0,0,0,20));
    
    sitePages = new Label(props.getProperty(TAManagerProp.SITE_TEMPLATE_SITE_PAGE.toString()));
    sitePages.setPadding(new Insets(20,0,20,0));
    
    // cdm2
    // course detail middle pane table
    courseMidTable = new TableView();
    CSGData data2 = (CSGData)app.getDataComponent();
    ObservableList <SitePage> siteData = data2.getSitePage();
    courseMidTable.setItems(siteData);
    //checkUseCol = new TableColumn(props.getProperty(TAManagerProp.SITE_TEMPLATE_USE.toString()));
    navbarColnew = new TableColumn(props.getProperty(TAManagerProp.SITE_TEMPLATE_NAVBAR_TITLE.toString()));
    fileNameCol= new TableColumn(props.getProperty(TAManagerProp.SITE_TEMPLATE_FILE_NAME.toString()));
    scriptCol= new TableColumn(props.getProperty(TAManagerProp.SITE_TEMPLATE_SCRIPT.toString()));
    
    // use column check box 
    
    String useColumnText = props.getProperty(TAManagerProp.SITE_TEMPLATE_USE.toString());
        checkUseCol = new TableColumn<SitePage, Boolean>(useColumnText);
        checkUseCol.setCellValueFactory(new Callback<CellDataFeatures<SitePage, Boolean>, ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<SitePage, Boolean> ex){
                SitePage site = ex.getValue();
                
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(site.isU());
                
                // column changes
                booleanProp.addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldvalue, Boolean newValue){
                        //site.setUndergrad(newValue);
                        //controller.handleUndergradToggle(person);
                        
                        //app.getGUI().updateToolbarControls(false);
                        site.setU(newValue);
                       // app.getGUI().updateToolbarControls(false);
                        
                        }
                    });
                    return booleanProp;
                }
            }
        );
        checkUseCol.setCellFactory(new Callback<TableColumn<SitePage, Boolean>, TableCell<SitePage, Boolean>>(){
            @Override
            public TableCell<SitePage,Boolean> call (TableColumn<SitePage, Boolean> ex){
                CheckBoxTableCell<SitePage, Boolean> cell = new CheckBoxTableCell<SitePage, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
   
   // set courseMidTable items
   
   navbarColnew.setCellValueFactory(new PropertyValueFactory<SitePage,String>("nav"));
   fileNameCol.setCellValueFactory(new PropertyValueFactory<SitePage,String>("file"));
   scriptCol.setCellValueFactory(new PropertyValueFactory<SitePage,String>("scr"));
   
   
    
    courseMidTable.getColumns().addAll(checkUseCol,navbarColnew,fileNameCol,scriptCol);
    courseMidTable.setEditable(true);
    courseMidTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    
    checkUseCol.prefWidthProperty().bind(courseMidTable.widthProperty().multiply(.25));
    navbarColnew.prefWidthProperty().bind(courseMidTable.widthProperty().multiply(.25));
    fileNameCol.prefWidthProperty().bind(courseMidTable.widthProperty().multiply(.25));
    scriptCol.prefWidthProperty().bind(courseMidTable.widthProperty().multiply(.25));
    
    //courseMidTable.setEditable(true);
    courseMidTable.setPrefWidth(700);
    courseMidTable.setMaxWidth(700);
    courseMidTable.setMinWidth(700);
    
    // set cell value factory of course Mid Table
   
    //courseMidTable.setPrefHeight(300);
    
    // cdm3
    // add all nodes in course detail middle pane
    courseMiddlePane.getChildren().addAll(siteTemplate,subSiteTemplate,subSiteTemplateDir,selectTempDir,sitePages,courseMidTable);
    
    // cdl1
    pageStyle = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_HEADER.toString()));
    pageStyle.setPadding(new Insets(20,0,20,0));
    Label bannerImage = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_BANNER.toString()));
    Label leftFooter = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_LEFT_FOOTER.toString()));
    Label rightFooter = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_RIGHT_FOOTER.toString()));
    Label styleSheet = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_STYLESHEET.toString()));
    Label pageStyleNote = new Label(props.getProperty(TAManagerProp.PAGE_STYLE_NOTE.toString()));
    Button bannerImageBt = new Button(changeBtText);
    
    Button leftFooterBt = new Button(changeBtText);
    Button rightFooterBt = new Button(changeBtText);
    // set button image
    //setButtonImage(bannerImageBt,CHANGE_ICON.toString());
    //bannerImageBt.setText(taDataStr);
    bannerImageBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CHANGE_TOOLTIP.toString())));
    
    //setButtonImage(leftFooterBt,CHANGE_ICON.toString());
    leftFooterBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CHANGE_TOOLTIP.toString())));
    
    //setButtonImage(rightFooterBt,CHANGE_ICON.toString());
    rightFooterBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CHANGE_TOOLTIP.toString())));
    
    styleSheetCb = new ComboBox();
    styleSheetCb.setPrefWidth(120);
    
    // *************** here need default image******************
      yale_university = "file:./images/"+props.getProperty(YALE_UNIVERSITY_ICON.toString());
      yale_department = "file:./images/"+props.getProperty(YALE_CS_ICON.toString());
      
      //String path = "file:./images/" + props.getProperty(icon);
        javafx.scene.image.Image image1 = new javafx.scene.image.Image(yale_university);
        javafx.scene.image.Image image2 = new javafx.scene.image.Image(yale_department);
        
        image_yale = new ImageView(image1);
        image_yale2 = new ImageView(image1);
        image_yale_cs = new ImageView(image2);
    
    GridPane pageStyleGripPane = new GridPane();
    pageStyleGripPane.add(bannerImage, 0, 0);
    pageStyleGripPane.add(leftFooter, 0, 1);
    pageStyleGripPane.add(rightFooter, 0, 2);
    pageStyleGripPane.add(styleSheet, 0, 3);
    pageStyleGripPane.add(pageStyleNote, 0, 4,3,1);
    
    pageStyleGripPane.add(image_yale, 1, 0);
    pageStyleGripPane.add(image_yale2, 1, 1);
    pageStyleGripPane.add(image_yale_cs, 1, 2);
    pageStyleGripPane.add(styleSheetCb, 1, 3);
    pageStyleGripPane.add(bannerImageBt, 2, 0);
    pageStyleGripPane.add(leftFooterBt, 2, 1);
    pageStyleGripPane.add(rightFooterBt, 2, 2);
    pageStyleGripPane.setHgap(20);
    pageStyleGripPane.setVgap(20);
    pageStyleGripPane.setPadding(new Insets(0,0,20,20));
    // add all to courseLowerPane
    courseLowerPane.getChildren().addAll(pageStyle,pageStyleGripPane);
    
    
    courseUpperPane.setStyle("-fx-border-color: #99ccff");
    courseMiddlePane.setStyle("-fx-border-color: #99ccff");
    courseLowerPane.setStyle("-fx-border-color: #99ccff");
    coursePane.getChildren().add(courseUpperPane);
    coursePane.getChildren().add(courseMiddlePane);
    coursePane.getChildren().add(courseLowerPane);
    
    ScrollPane courseScroll = new ScrollPane(coursePane); // set scrollPane
    coursePane.prefWidthProperty().bind(courseScroll.widthProperty()); // set bind width
    courseTab.setContent(courseScroll);
    
// ----------------------------Recitation Data ---------------------------
//-------------------------------------------------------------------------------
    recitationPane = new VBox();
    recitationUpperPane = new VBox();
    recitationLowerPane = new VBox();
    recitationHeaderPane = new HBox();
    GridPane recitationLowerGp = new GridPane(); // for recitation lower grid pane
    
    // -------------Recitation Data Upper Pane ----------------------
    recitationTable = new TableView();
    
    // 1 recitation remove pane
    recitationHeader = new Label(props.getProperty(TAManagerProp.RECITATION_HEADER.toString()));
    Button recitationremoveBt = new Button();
    
    
    setButtonImage(recitationremoveBt,REMOVE_ICON.toString());
    recitationremoveBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.REMOVE_TOOLTIP.toString())));
    
    recitationHeaderPane.getChildren().addAll(recitationHeader,recitationremoveBt);
    recitationHeaderPane.setPadding(new Insets(20,0,20,0));
    
    // 2 recitation table
    TableColumn sectionCol  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_SECTION_COLUMN.toString()));
    TableColumn instructorCol  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_INSTRUCTOR_COLUMN.toString()));
    TableColumn dayCol  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_DAY_COLUMN.toString()));
    TableColumn locationCol  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_LOCATION_COLUMN.toString()));
    TableColumn ta1Col  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_TA1_COLUMN.toString()));
    TableColumn ta2Col  = new TableColumn(props.getProperty(TAManagerProp.RECITATION_TA2_COLUMN.toString()));
    
    sectionCol.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    instructorCol.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    dayCol.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    locationCol.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    ta1Col.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    ta2Col.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.16));
    
    recitationTable.getColumns().addAll(sectionCol,instructorCol,dayCol,locationCol, ta1Col, ta2Col);
    recitationTable.setPrefWidth(700);
    recitationTable.setMaxWidth(700);
    recitationTable.setMinWidth(700);
 //   recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    recitationUpperPane.getChildren().addAll(recitationHeaderPane, recitationTable);
    
    // set recitationTable Items
    ObservableList<Recitation> recitationData = data.getRecitation();
    recitationTable.setItems(recitationData);
    sectionCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("section"));
    instructorCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("instructor"));
    dayCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("time"));
   locationCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("location"));
   ta1Col.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta1"));
   ta2Col.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta2"));
    
    // -------------Recitation Data Lower Pane ----------------------
    
    // 1 all labels
     recitationAddHeader = new Label(props.getProperty(TAManagerProp.RECITATION_ADD_HEADER.toString()));
     recitationAddHeader.setPadding(new Insets(20,0,20,0));
    Label recitationSection = new Label(props.getProperty(TAManagerProp.RECITATION_SECTION.toString()));
    Label recitationInstructor = new Label(props.getProperty(TAManagerProp.RECITATION_INSTRUCTOR.toString()));
    Label recitationDay = new Label(props.getProperty(TAManagerProp.RECITATION_DAY.toString()));
    Label recitationLocation = new Label(props.getProperty(TAManagerProp.RECITATION_LOCATION.toString()));
    Label recitationTA1 = new Label(props.getProperty(TAManagerProp.RECITATION_TA1.toString()));
    Label recitationTA2 = new Label(props.getProperty(TAManagerProp.RECITATION_TA2.toString()));
    
    // 2 all text field 
    recitationSectionTf = new TextField();
    recitationInstructorTf = new TextField();
    recitationDayTf = new TextField();
    recitationLocationTf = new TextField();
    
    recitationSectionTf.setPrefWidth(150);
    recitationInstructorTf.setPrefWidth(150);
    recitationDayTf.setPrefWidth(150);
    recitationLocationTf.setPrefWidth(150);
    
    // 3 all combo box
    superTANameList = FXCollections.observableArrayList();
    
    recitationTA1Cb = new ComboBox(superTANameList);
    recitationTA2Cb = new ComboBox(superTANameList);
    recitationTA1Cb.setPrefWidth(150);
    recitationTA2Cb.setPrefWidth(150);
    
    // 4 all button
    Button recitationAddBt = new Button(addBtText);
    //recitationAddBt.setPadding(new Insets(0,0,0,20));
    Button recitationClearBt = new Button(clearBtText);

    
    //setButtonImage(recitationAddBt,ADD_ICON.toString());
    recitationAddBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.ADD_TOOLTIP.toString())));
    
    //setButtonImage(recitationClearBt,CLEAR_ICON.toString());
    recitationClearBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CLEAR_TOOLTIP.toString())));
    
    // 5 build in grid pane
    recitationLowerGp.add(recitationSection, 0, 0);
    recitationLowerGp.add(recitationInstructor, 0, 1);
    recitationLowerGp.add(recitationDay, 0, 2);
    recitationLowerGp.add(recitationLocation, 0, 3);
    recitationLowerGp.add(recitationTA1, 0, 4);
    recitationLowerGp.add(recitationTA2, 0, 5);
    recitationLowerGp.add(recitationAddBt, 0, 6);
    recitationLowerGp.add(recitationSectionTf, 1, 0);
    recitationLowerGp.add(recitationInstructorTf, 1, 1);
    recitationLowerGp.add(recitationDayTf, 1, 2);
    recitationLowerGp.add(recitationLocationTf, 1, 3);
    recitationLowerGp.add(recitationTA1Cb, 1, 4);
    recitationLowerGp.add(recitationTA2Cb, 1, 5);
    recitationLowerGp.add(recitationClearBt, 1, 6);
    recitationLowerGp.setHgap(20);
    recitationLowerGp.setVgap(20);
    recitationLowerGp.setPadding(new Insets(0,0,20,20));
    
    recitationLowerPane.getChildren().addAll(recitationAddHeader,recitationLowerGp);
    
    // build Recitation Data Pane
    recitationUpperPane.setStyle("-fx-border-color: #99ccff");
    recitationLowerPane.setStyle("-fx-border-color: #99ccff");
    recitationPane.getChildren().addAll(recitationUpperPane, recitationLowerPane);
    ScrollPane recitationScroll = new ScrollPane(recitationPane); // set scrollPane
    recitationPane.prefWidthProperty().bind(recitationScroll.widthProperty()); // set bind width
    recitationTab.setContent(recitationScroll);
    
//---------------------Schedule Data Pane----------------------------------------------------------   
//--------------------------------------------------------------------------------------------------
    schedulePane = new VBox();
    scheduleUpperPane = new VBox();
    calendarPane = new HBox();
    scheduleLowerPane = new VBox();
    scheculeItemPane = new HBox();
    GridPane scheduleGp = new GridPane();
    
    scheduleHeader = new Label(props.getProperty(TAManagerProp.SCHEDULE_HEADER.toString()));
    scheduleHeader.setPadding(new Insets(20,0,20,0));
  //--------------------- Schedule Upper Pane ------------------------------
    calendarHeader = new Label(props.getProperty(TAManagerProp.SCHEDULE_CALENDAR.toString()));
    calendarHeader.setPadding(new Insets(10,0,20,0));
    
    Label calendarMon = new Label(props.getProperty(TAManagerProp.SCHEDULE_START_MON.toString()));
    Label calendarFri = new Label(props.getProperty(TAManagerProp.SCHEDULE_START_FRI.toString()));
    monPicker = new DatePicker();
    friPicker = new DatePicker();
    
  // date picker
    final Callback<DatePicker, DateCell> dayMonday = new Callback<DatePicker, DateCell>(){
            @Override
            public DateCell call(DatePicker param) {
               return new DateCell(){
                   @Override
                   public void updateItem(LocalDate item, boolean empty){
                       super.updateItem(item, empty);
                       
                       if (!item.getDayOfWeek().equals(DayOfWeek.MONDAY) ){
                           setDisable(true);
                           setStyle("-fx-background-color: #99ccff;");
                       }
                   }
               };
            }
  
  }; 
  monPicker.setDayCellFactory(dayMonday);
  
  final Callback<DatePicker, DateCell> dayFriday = new Callback<DatePicker, DateCell>(){
            @Override
            public DateCell call(DatePicker param) {
               return new DateCell(){
                   @Override
                   public void updateItem(LocalDate item, boolean empty){
                       super.updateItem(item, empty);
                       
                       if (!item.getDayOfWeek().equals(DayOfWeek.FRIDAY) ){
                           setDisable(true);
                           setStyle("-fx-background-color: #99ccff;");
                       }
                   }
               };
            }
  
  }; 
  friPicker.setDayCellFactory(dayFriday);
    
    
    calendarPane.getChildren().addAll(calendarMon,monPicker,calendarFri,friPicker);
    calendarPane.setPadding(new Insets(0,0,20,0));
    scheduleUpperPane.getChildren().addAll(calendarHeader,calendarPane);
  
  //--------------------- Schedule Lower Pane ------------------------------ 
    // 1 schedule item
    scheduleItemHeader = new Label(props.getProperty(TAManagerProp.SCHEDULE_ITEMS.toString()));
    Button scheduleRemoveBt = new Button();
    setButtonImage(scheduleRemoveBt,REMOVE_ICON.toString());
    scheduleRemoveBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.REMOVE_TOOLTIP.toString())));
    
    scheculeItemPane.getChildren().addAll(scheduleItemHeader,scheduleRemoveBt);
    scheculeItemPane.setPadding(new Insets(20,0,0,20));
    
    // 2 schedule table
    scheduleTable = new TableView();
    TableColumn typeCol  = new TableColumn(props.getProperty(TAManagerProp.SCHEDULE_TYPE_COL.toString()));
    TableColumn dateCol  = new TableColumn(props.getProperty(TAManagerProp.SCHEDULE_DATE_COL.toString()));
    TableColumn titleCol  = new TableColumn(props.getProperty(TAManagerProp.SCHEDULE_TITLE_COL.toString()));
    TableColumn topicCol  = new TableColumn(props.getProperty(TAManagerProp.SCHEDULE_TOPIC_COL.toString()));
    
    ObservableList<ScheduleItem> scheduleData = data.getScheduleItem();
    scheduleTable.setItems(scheduleData);
    scheduleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    typeCol.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("type"));
    dateCol.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("date"));
    titleCol.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("title"));
    topicCol.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("topic"));
    
    typeCol.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(.25));
    dateCol.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(.25));
    titleCol.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(.25));
    topicCol.prefWidthProperty().bind(scheduleTable.widthProperty().multiply(.25));
    
    scheduleTable.getColumns().addAll(typeCol,dateCol,titleCol,topicCol);
    scheduleTable.setPrefWidth(700);
    scheduleTable.setMaxWidth(700);
    scheduleTable.setMinWidth(700);
    
    // set schedule table items
    
    
    
    // 3 schedule grid pane 
    scheduleAddHeader = new Label(props.getProperty(TAManagerProp.SCHEDULE_ADD_HEADER.toString()));
    
    
    Label scheduleType = new Label(props.getProperty(TAManagerProp.SCHEDULE_TYPE.toString()));
    Label scheduleDate = new Label(props.getProperty(TAManagerProp.SCHEDULE_DATE.toString()));
    Label scheduleTime = new Label(props.getProperty(TAManagerProp.SCHEDULE_TIME.toString()));
    Label scheduleTitle = new Label(props.getProperty(TAManagerProp.SCHEDULE_TITLE.toString()));
    Label scheduleTopic = new Label(props.getProperty(TAManagerProp.SCHEDULE_TOPIC.toString()));
    Label scheduleLink = new Label(props.getProperty(TAManagerProp.SCHEDULE_LINK.toString()));
    Label scheduleCriteria = new Label(props.getProperty(TAManagerProp.SCHEDULE_CRITERIA.toString()));
    
    ObservableList<String>typeList = FXCollections.observableArrayList("Holliday", "Homework", "Lecture");
    scheduleTypeCb = new ComboBox(typeList);
    scheduleDateDp = new DatePicker();
    scheduleTimeTf = new TextField();
    scheduleTitleTf = new TextField();
    scheduleTopicTf = new TextField();
    scheduleLinkTf = new TextField();
    scheduleCriteriaTf = new TextField();
    
    scheduleTypeCb.setPrefWidth(150);
    scheduleTimeTf.setPrefWidth(150);
    scheduleTitleTf.setPrefWidth(300);
    scheduleTopicTf.setPrefWidth(300);
    scheduleLinkTf.setPrefWidth(300);
    scheduleCriteriaTf.setPrefWidth(300);
    
    Button scheduleAddBt = new Button(addBtText);
   // scheduleAddBt.setPadding(new Insets(0,0,0,20));
    Button scheduleClearBt = new Button(clearBtText);
    
    //setButtonImage(scheduleAddBt,ADD_ICON.toString());
    scheduleAddBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.ADD_TOOLTIP.toString())));
    
    //setButtonImage(scheduleClearBt,CLEAR_ICON.toString());
    scheduleClearBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CLEAR_TOOLTIP.toString())));
    
    scheduleGp.add(scheduleAddHeader, 0, 0);
    scheduleGp.add(scheduleType, 0, 1);
    scheduleGp.add(scheduleDate, 0, 2);
    scheduleGp.add(scheduleTime, 0, 3);
    scheduleGp.add(scheduleTitle, 0, 4);
    scheduleGp.add(scheduleTopic, 0, 5);
    scheduleGp.add(scheduleLink, 0, 6);
    scheduleGp.add(scheduleCriteria, 0, 7);
    scheduleGp.add(scheduleAddBt, 0, 8);
    scheduleGp.add(scheduleTypeCb, 1, 1);
    scheduleGp.add(scheduleDateDp, 1, 2);
    scheduleGp.add(scheduleTimeTf, 1, 3);
    scheduleGp.add(scheduleTitleTf, 1, 4);
    scheduleGp.add(scheduleTopicTf, 1, 5);
    scheduleGp.add(scheduleLinkTf, 1, 6);
    scheduleGp.add(scheduleCriteriaTf, 1, 7);
    scheduleGp.add(scheduleClearBt, 1, 8);
    scheduleGp.setHgap(20);
    scheduleGp.setVgap(20);
    scheduleGp.setPadding(new Insets(40,0,20,20));
    scheduleLowerPane.getChildren().addAll(scheculeItemPane,scheduleTable,scheduleGp);
    scheduleLowerPane.setPadding(new Insets(20,0,0,0));
  
  // date picker 
  
  final Callback<DatePicker, DateCell> dayNoWeekend = new Callback<DatePicker, DateCell>(){
            @Override
            public DateCell call(DatePicker param) {
               return new DateCell(){
                   @Override
                   public void updateItem(LocalDate item, boolean empty){
                       super.updateItem(item, empty);
                       
                       if (item.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||item.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                           setDisable(true);
                           setStyle("-fx-background-color: #99ccff;");
                       }
                   }
               };
            }
  
  }; 
  scheduleDateDp.setDayCellFactory(dayNoWeekend);
  
  // -------- build Schedule Data Pane
    scheduleUpperPane.setStyle("-fx-border-color: #99ccff");
    scheduleLowerPane.setStyle("-fx-border-color: #99ccff");
    schedulePane.getChildren().addAll(scheduleHeader,scheduleUpperPane,scheduleLowerPane);
    ScrollPane scheduleScroll = new ScrollPane(schedulePane); // set scrollPane
    schedulePane.prefWidthProperty().bind(scheduleScroll.widthProperty()); // set bind width
    scheduleTab.setContent(scheduleScroll);
    
//---------------------Project Data Pane----------------------------------------------------------   
//--------------------------------------------------------------------------------------------------
    //  -----------build all box-----------
    projectHeader = new Label(props.getProperty(TAManagerProp.PROJECT_HEADER.toString()));
    projectPane = new VBox();
    projectUpperPane = new VBox();
    projectLowerPane = new VBox();
    teamHbox = new HBox();
    teamTable = new TableView(); // for teams table
    GridPane teamGp = new GridPane();      // for teams add/edit section
    studentHbox = new HBox();
    studentTable = new TableView();
    GridPane studentGp = new GridPane();
    
    // ------------ project upper pane ---------------
    // 1 HBox
    teamHeader = new Label(props.getProperty(TAManagerProp.PROJECT_TEAMS_HEADER.toString()));
    //teamHeader.setPadding(new Insets(20,0,20,0));
    Button teamRemoveBt = new Button();
    setButtonImage(teamRemoveBt,REMOVE_ICON.toString());
    teamRemoveBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.REMOVE_TOOLTIP.toString())));
    
    teamHbox.getChildren().addAll(teamHeader,teamRemoveBt);
    teamHbox.setPadding(new Insets(20,0,20,0));
    
    // 2 team table
    TableColumn teamNameCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_TEAMS_NAME_COL.toString()));
    TableColumn teamColorCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_TEAMS_COLOR_COL.toString()));
    TableColumn teamTextColorCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_TEAMS_TEXTCOLOR_COL.toString()));
    TableColumn teamLinkCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_TEAMS_LINK_COL.toString()));
    
    teamTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    ObservableList<Team> teamData = data.getTeam();
    teamTable.setItems(teamData);
    teamNameCol.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
    teamColorCol.setCellValueFactory(new PropertyValueFactory<Team, String>("color"));
    teamTextColorCol.setCellValueFactory(new PropertyValueFactory<Team, String>("textColor"));
    teamLinkCol.setCellValueFactory(new PropertyValueFactory<Team, String>("link"));
      
    
    teamNameCol.prefWidthProperty().bind(teamTable.widthProperty().multiply(.25));
    teamColorCol.prefWidthProperty().bind(teamTable.widthProperty().multiply(.25));
    teamTextColorCol.prefWidthProperty().bind(teamTable.widthProperty().multiply(.25));
    teamLinkCol.prefWidthProperty().bind(teamTable.widthProperty().multiply(.25));
    
    teamTable.getColumns().addAll(teamNameCol,teamColorCol,teamTextColorCol,teamLinkCol);
    teamTable.setPrefWidth(700);
    teamTable.setMaxWidth(700);
    teamTable.setMinWidth(700);
    
    // 3  team grid pane
    teamAddHeader = new Label(props.getProperty(TAManagerProp.PROJECT_TEAMS_ADD_HEADER.toString()));
    
    Label teamName = new Label(props.getProperty(TAManagerProp.PROJECT_TEAMS_NAME.toString()));
    Label teamColor = new Label(props.getProperty(TAManagerProp.PROJECT_TEAMS_COLOR.toString()));
    Label teamLink = new Label(props.getProperty(TAManagerProp.PROJECT_TEAMS_LINK.toString()));
    
    teamNameTf = new TextField();
    teamColorCp = new ColorPicker();
    teamTextColorCp = new ColorPicker();
    teamLinkTf = new TextField();
    
    teamNameTf.setPrefWidth(150);
    teamLinkTf.setPrefWidth(300);
    
    Button teamAddBt = new Button(addBtText);
    Button teamClearBt = new Button(clearBtText);
    //setButtonImage(teamAddBt,ADD_ICON.toString());
    teamAddBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.ADD_TOOLTIP.toString())));
    
    //setButtonImage(teamClearBt,CLEAR_ICON.toString());
    teamClearBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CLEAR_TOOLTIP.toString())));
    
    
    teamGp.add(teamAddHeader,0,0);
    teamGp.add(teamName,0,1);
    teamGp.add(teamColor,0,2);
    teamGp.add(teamLink,0,3);
    teamGp.add(teamAddBt,0,4);
    teamGp.add(teamNameTf,1,1);
    teamGp.add(teamColorCp,1,2);
    teamGp.add(teamTextColorCp,2,2);
    teamGp.add(teamLinkTf,1,3);
    teamGp.add(teamClearBt,1,4);
    teamGp.setHgap(20);
    teamGp.setVgap(20);
    teamGp.setPadding(new Insets(40,0,20,20));
    // 5 build upper pane
    projectUpperPane.getChildren().addAll(teamHbox,teamTable,teamGp);
    
    // ------------ project lower pane ---------------
    // 1 HBox
    studentHeader = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_HEADER.toString()));
    
    Button studentRemoveBt = new Button();
    setButtonImage(studentRemoveBt,REMOVE_ICON.toString());
    studentRemoveBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.REMOVE_TOOLTIP.toString())));
    
    studentHbox.getChildren().addAll(studentHeader,studentRemoveBt);
    studentHbox.setPadding(new Insets(20,0,20,0));
    
    
    // 2 student table
    TableColumn studentFirstNameCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_STUDENTS_FIRSTNAME_COL.toString()));
    TableColumn studentLastNameCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_STUDENTS_LASTNAME_COL.toString()));
    TableColumn studentTeamCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_STUDENTS_TEAM_COL.toString()));
    TableColumn studentRoleCol  = new TableColumn(props.getProperty(TAManagerProp.PROJECT_STUDENTS_ROLE_COL.toString()));
    
    studentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    ObservableList<Student> studentData = data.getStudent();
    studentTable.setItems(studentData);
    
    studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
    studentLastNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
    studentTeamCol.setCellValueFactory(new PropertyValueFactory<Student,String>("team"));
    studentRoleCol.setCellValueFactory(new PropertyValueFactory<Student,String>("role"));
    
    studentFirstNameCol.prefWidthProperty().bind(studentTable.widthProperty().multiply(.25));
    studentLastNameCol.prefWidthProperty().bind(studentTable.widthProperty().multiply(.25));
    studentTeamCol.prefWidthProperty().bind(studentTable.widthProperty().multiply(.25));
    studentRoleCol.prefWidthProperty().bind(studentTable.widthProperty().multiply(.25));
    
    studentTable.getColumns().addAll(studentFirstNameCol,studentLastNameCol,studentTeamCol,studentRoleCol);
    studentTable.setPrefWidth(700);
    studentTable.setMaxWidth(700);
    studentTable.setMinWidth(700);
    
    // 3 grip pane
    studentAddHeader = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_ADD_HEADER.toString()));
    Label studentFirstName = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_FIRSTNAME.toString()));
    Label studentLastName = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_LASTNAME.toString()));
    Label studentTeam = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_TEAM.toString()));
    Label studentRole = new Label(props.getProperty(TAManagerProp.PROJECT_STUDENTS_ROLE.toString()));
    
    studentFirstNameTf = new TextField();
    studentLastNameTf = new TextField();
    studentRoleTf = new TextField();
    
    studentFirstNameTf.setPrefWidth(150);
    studentLastNameTf.setPrefWidth(150);
    studentRoleTf.setPrefWidth(150);
    
    
    teamNameList = FXCollections.observableArrayList();
    studentTeamCb = new ComboBox(teamNameList);
    studentTeamCb.setPrefWidth(150);
    
    Button studentAddBt = new Button(addBtText);
    Button studentClearBt = new Button(clearBtText);
   // setButtonImage(studentAddBt,ADD_ICON.toString());
    studentAddBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.ADD_TOOLTIP.toString())));
    
    //setButtonImage(studentClearBt,CLEAR_ICON.toString());
    studentClearBt.setTooltip(new Tooltip(props.getProperty(TAManagerProp.CLEAR_TOOLTIP.toString())));
    
    
    studentGp.add(studentAddHeader, 0, 0);
    studentGp.add(studentFirstName, 0, 1);
    studentGp.add(studentLastName, 0, 2);
    studentGp.add(studentTeam, 0, 3);
    studentGp.add(studentRole, 0, 4);
    studentGp.add(studentAddBt, 0, 5);
    studentGp.add(studentFirstNameTf, 1, 1);
    studentGp.add(studentLastNameTf, 1, 2);
    studentGp.add(studentTeamCb, 1, 3);
    studentGp.add(studentRoleTf, 1, 4);
    studentGp.add(studentClearBt, 1, 5);
    studentGp.setHgap(20);
    studentGp.setVgap(20);
    studentGp.setPadding(new Insets(40,0,20,20));
    
    // 4 build lower pane
    projectLowerPane.getChildren().addAll(studentHbox,studentTable,studentGp);
    
    // ------------ build all pane together
    projectUpperPane.setStyle("-fx-border-color: #99ccff");
    projectLowerPane.setStyle("-fx-border-color: #99ccff");
    projectPane.getChildren().addAll(projectHeader,projectUpperPane,projectLowerPane);
    ScrollPane projectScroll = new ScrollPane(projectPane); // set scrollPane
    projectPane.prefWidthProperty().bind(projectScroll.widthProperty()); // set bind width
    projectTab.setContent(projectScroll);
    }
    
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    
    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public HBox getcomboBoxPane(){
       return CBox; 
    }
    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
    
    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }
    public ComboBox getStartTime(){
        return startBox;
    }
    
    public void setStartTime(String time){
        startBox.setValue(time);
    }
    
    public ComboBox getEndTime(){
        return endBox;
    }
    
    public void setEndTime(String time){
        endBox.setValue(time);
    }
    
    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    /*
    public static String buildCellText(int hour, String minute){
        int hr = hour;
        if(hr ==0)
            return "12" +":"+minute+"am";
        else if(hr<12)
            return ""+hr +":"+minute+"am";
        else if(hr==12)
            return "12" +":"+minute+"pm";
        else
            return ""+(hr - 12) + ":" + minute + "pm";
    
    }
    */
    public static String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    public TabPane getTabPane(){
        return tabPane;
    }
   /* course pane
    VBox coursePane = new VBox();
    VBox courseUpperPane = new VBox();
    VBox courseMiddlePane = new VBox();
    VBox courseLowerPane = new VBox();
    Label courseInfo
    Label siteTemplate
    Label pageStyle
    TableView courseMidTable
    Label sitePages
    Label courseExportDir
    Label subTemplateExportDir*/
    public Label getSitePage() {
        return sitePages;
    }
    
    public VBox getCoursePane() {
        return coursePane;
    }
    
    public VBox getCourseUpperPane() {
        return courseUpperPane;
    }
    
    public VBox getCourseMiddlePane() {
        return courseMiddlePane;
    }
    
     public VBox getCourseLowerPane() {
        return courseLowerPane;
    }
    
    public Label getCourseInfo() {
        return courseInfo;
    }
    
    public Label getSiteTemplate() {
        return siteTemplate;
    }
    
    public Label getPageStyle() {
        return pageStyle;
    }
    
    public TableView getCourseMiddleTable() {
        return courseMidTable;
    }

    public ComboBox getCourseSubjectCb() {
        return courseSubjectCb;
    }

    public ComboBox getCourseNumberCb() {
        return courseNumberCb;
    }

    public ComboBox getCourseSemesterCb() {
        return courseSemesterCb;
    }

    public ComboBox getCourseYearCb() {
        return courseYearCb;
    }

    public ComboBox getStyleSheetCb() {
        return styleSheetCb;
    }

    public TextField getCourseTitleTf() {
        return courseTitleTf;
    }

    public TextField getCourseNameTf() {
        return courseNameTf;
    }

    public TextField getCourseHomeTf() {
        return courseHomeTf;
    }
    
    public void getBannerImg(String path){
        javafx.scene.image.Image img = new javafx.scene.image.Image(path);
        image_yale.setImage(img);
    }
    
    public void getLeftImg(String path){
        javafx.scene.image.Image img = new javafx.scene.image.Image(path);
        image_yale2.setImage(img);
    }
    
    public void getRightImg(String path){
        javafx.scene.image.Image img = new javafx.scene.image.Image(path);
        image_yale_cs.setImage(img);
    }

     public Label getCourseExportDir() {
        return courseExportDir;
    }
    
    public Label getSubSiteTemplateDir() {
        return subSiteTemplateDir;
    }
    
    public void setBackCourseDirectory(){
        getCourseExportDir().setText(defaultExportDir);
        getSubSiteTemplateDir().setText(defaultTemplateDir);
    }
    
    public void setDefaultImg(){
        getBannerImg(yale_university);
        getLeftImg(yale_university);
        getRightImg(yale_department);
    }
    /* recitation pane
        VBox recitationPane = new VBox();
        VBox recitationUpperPane = new VBox();
        VBox recitationLowerPane = new VBox();
        HBox recitationHeaderPane = new HBox();
        TableView recitationTable
        Label recitationHeader
    */
    
    public Label getRecitationAddHeader(){
        return recitationAddHeader ;
    } 
    
    public VBox getRecitationPane() {
        return recitationPane;
    }
    
    public VBox getRecitationUpperPane() {
        return recitationUpperPane;
    }
    
    public VBox getRecitationLowerPane() {
        return recitationLowerPane;
    }
    
    public HBox getRecitationHeaderPane() {
        return recitationHeaderPane;
    }
    
    public TableView getRecitationTable() {
        return recitationTable;
    }
    
    public Label getrecitationHeader() {
        return recitationHeader;
    }
    
    public ObservableList<String> getSuperTANameList(){
        return superTANameList;
    }
    /* schedule pane
    VBox schedulePane = new VBox();
    VBox scheduleUpperPane = new VBox();
    HBox calendarPane = new HBox();
    VBox scheduleLowerPane = new VBox();
    HBox scheculeItemPane = new HBox();
    Label scheduleHeader
    Label calendarHeader
    Label scheduleItemHeader
    TableView scheduleTable
    Label scheduleAddHeader
    */
    
    public VBox getSchedulePane() {
        return schedulePane;
    }
    
    public VBox getScheduleUpperPane() {
        return scheduleUpperPane;
    }
    
    public VBox getScheduleLowerPane() {
        return scheduleLowerPane;
    }
    
    public HBox getCalendarPane() {
        return calendarPane;
    }
    
    public HBox getScheduleItemPane() {
        return scheculeItemPane;
    }
    
    public TableView getScheduleTable() {
        return scheduleTable;
    }
    
    public Label getScheduleHeader() {
        return scheduleHeader;
    }
    
    public Label getCalendarHeader() {
        return calendarHeader;
    }
    
    public Label getScheduleItemHeader() {
        return scheduleItemHeader;
    }
    
    public Label getScheduleAddHeader() {
        return scheduleAddHeader;
    }

    public DatePicker getMonPicker() {
        return monPicker;
    }

    public void setMonPicker(DatePicker monPicker) {
        this.monPicker = monPicker;
    }

    public DatePicker getFriPicker() {
        return friPicker;
    }

    public void setFriPicker(DatePicker friPicker) {
        this.friPicker = friPicker;
    }

    public DatePicker getScheduleDateDp() {
        return scheduleDateDp;
    }

    public void setScheduleDateDp(DatePicker scheduleDateDp) {
        this.scheduleDateDp = scheduleDateDp;
    }
    
    
    
    /* project pane
    Label projectHeader 
    VBox projectPane = new VBox();
    VBox projectUpperPane = new VBox();
    VBox projectLowerPane = new VBox();
    HBox teamHbox = new HBox();
    TableView teamTable = new TableView();
    HBox studentHbox = new HBox();
    TableView studentTable = new TableView();
    Label teamHeader
    Label teamAddHeader
    Label studentHeader
    Label studentAddHeader;
    */
    
    public Label getProjectHeader() {
        return projectHeader;
    }
    
    public Label getTeamHeader() {
        return teamHeader;
    }
    
    public Label getTeamAddHeader() {
        return teamAddHeader;
    }
    
    public Label getStudentHeader() {
        return studentHeader;
    }
    
    public Label getStudentAddHeader() {
        return studentAddHeader;
    }
    
     public VBox getProjectPane() {
        return projectPane;
    }
     
    public VBox getProjectUpperPane() {
        return projectUpperPane;
    }
    
    public VBox getProjectLowerPane() {
        return projectLowerPane;
    }
    
    public HBox getTeamHbox() {
        return teamHbox;
    }
    
    public HBox getstudentHbox() {
        return studentHbox;
    }
    
    public TableView getTeamTable() {
        return teamTable;
    }
    
    public ObservableList<String> getTeamNameList(){
        return teamNameList;
    }
    
    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
        controller.handleClearTA();
        enable = false;
        startBox.getSelectionModel().select(null);
        endBox.getSelectionModel().select(null);
        enable = true;
        
        // CD pane
        courseSubjectCb.setValue("");
        courseNumberCb.setValue("");
        courseSemesterCb.setValue("");
        courseYearCb.setValue("");
        
        courseTitleTf.clear();
        courseNameTf.clear();
        courseHomeTf.clear();
        
        // RD pane
        superTANameList.clear();
    
        recitationSectionTf.clear(); 
        recitationInstructorTf.clear(); 
        recitationDayTf.clear(); 
        recitationLocationTf.clear();
        recitationTA1Cb.setValue("");
        recitationTA2Cb.setValue("");
        
        // SD Pane
        monPicker.getEditor().clear();
        friPicker.getEditor().clear();
        scheduleDateDp.getEditor().clear();
        scheduleTypeCb.setValue("");
    
        scheduleTimeTf.clear();
        scheduleTitleTf.clear();
        scheduleTopicTf.clear(); ;
        scheduleLinkTf.clear();
        scheduleCriteriaTf.clear();
        
        // PD pane
        teamNameList.clear();
         teamNameTf.clear(); ;
         teamColorCp.setValue(Color.WHITE); ;
         teamTextColorCp.setValue(Color.WHITE); ;
         teamLinkTf.clear(); ;
         studentFirstNameTf.clear();
         studentLastNameTf.clear();
         studentRoleTf.clear();
         studentTeamCb.setValue("");
        
    }
    
    
    public void enableCombox(boolean t){
        enable = t;
    }
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        CSGData taData = (CSGData)dataComponent;
        reloadOfficeHoursGrid(taData);
    }

    public void reloadOfficeHoursGrid(CSGData dataComponent) {        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }
        
        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setFocusTraversable(true);
            p.setOnKeyPressed(e -> {
                controller.handleKeyPress(e.getCode());
            });
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
            p.setOnMouseExited(e -> {
                controller.handleGridCellMouseExited((Pane) e.getSource());
            });
            p.setOnMouseEntered(e -> {
                controller.handleGridCellMouseEntered((Pane) e.getSource());
            });
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        TAStyle taStyle = (TAStyle)app.getStyleComponent();
        taStyle.initOfficeHoursGridStyle();
    }
    
    public void addCellToGrid(CSGData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());        
    }
    
    public void setButtonImage(Button bt, String icon){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String path = "file:./images/" + props.getProperty(icon);
        javafx.scene.image.Image image = new javafx.scene.image.Image(path);
        bt.setGraphic(new ImageView(image));
    }
}
