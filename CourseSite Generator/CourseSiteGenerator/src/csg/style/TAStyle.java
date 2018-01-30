package csg.style;

import djf.AppTemplate;
import djf.components.AppStyleComponent;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;

/**
 * This class manages all CSS style for this application.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class TAStyle extends AppStyleComponent {
    // FIRST WE SHOULD DECLARE ALL OF THE STYLE TYPES WE PLAN TO USE
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";
    public static String CLASS_CLEAR_BUTTON = "clear_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_COMBOBOX_PANE = "combo_box_pane";
    //public static String CLASS_START_TIME = "start_time";
    //public static String CLASS_END_TIME = "end_time";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";

    // FOR HIGHLIGHTING CELLS, COLUMNS, AND ROWS
    public static String CLASS_HIGHLIGHTED_GRID_CELL = "highlighted_grid_cell";
    public static String CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN = "highlighted_grid_row_or_column";
    
    // all Header label
    public static String PANE_HEADER_LABEL = "pane_header_label";
    public static String PANE_SUBHEADER_LABEL = "pane_subheader_label";
    public static String PANE = "pane";
    public static String HBOX = "hbox";
    public static String TABPANE = "tabpane";
    
    // COURSE DETAIL
    /*
    VBox coursePane = new VBox();
    VBox courseUpperPane = new VBox();
    VBox courseMiddlePane = new VBox();
    VBox courseLowerPane = new VBox();
    Label courseInfo
    Label siteTemplate
    Label pageStyle
    TableView courseMidTable
    */
    public static String COURSE_PANE = "course_pane";
    public static String COURSE_UPPER_PANE = "course_upper_pane";
    public static String COURSE_MIDDLE_PANE = "course_middle_pane";
    public static String COURSE_LOWER_PANE = "course_lower_pane";
    //public static String COURSE_INFO = "course_info";
    //public static String SITE_TEMPLATE = "site_template";
    //public static String PAGE_STYLE = "page_style";
    public static String COURSE_MIDDLE_TABLE = "course_middle_table";
    
    // recitation data
    /* recitation pane
        VBox recitationPane = new VBox();
        VBox recitationUpperPane = new VBox();
        VBox recitationLowerPane = new VBox();
        HBox recitationHeaderPane = new HBox();
        TableView recitationTable
        Label recitationHeader*/
    //public static String RECITATION_PANE = "course_pane";
    //public static String COURSE_PANE = "course_pane";
    //public static String COURSE_PANE = "course_pane";
    //public static String COURSE_PANE = "course_pane";
    //public static String COURSE_PANE = "course_pane";
    
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
    Label stude*/
    
    
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public TAStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initTAWorkspaceStyle();
    }

    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initTAWorkspaceStyle() {
        // LEFT SIDE - THE HEADER
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getTAsHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = workspaceComponent.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE);
        for (TableColumn tableColumn : taTable.getColumns()) {
            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
        }

        // LEFT SIDE - THE TA DATA ENTRY
        workspaceComponent.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE);
        workspaceComponent.getNameTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        workspaceComponent.getEmailTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        workspaceComponent.getAddButton().getStyleClass().add(CLASS_ADD_TA_BUTTON);
        workspaceComponent.getClearButton().getStyleClass().add(CLASS_CLEAR_BUTTON);
        

        // RIGHT SIDE - THE HEADER
        workspaceComponent.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        workspaceComponent.getOfficeHoursSubheaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
       // workspaceComponent.getStartTime().getStyleClass().add(CLASS_START_TIME);
        //workspaceComponent.getEndTime().getStyleClass().add(CLASS_END_TIME);
        workspaceComponent.getcomboBoxPane().getStyleClass().add(CLASS_COMBOBOX_PANE);
        
        workspaceComponent.getTabPane().getStyleClass().add(TABPANE);
        
       // COURSE DETAIL STYLE
       /*
    VBox coursePane = new VBox();
    VBox courseUpperPane = new VBox();
    VBox courseMiddlePane = new VBox();
    VBox courseLowerPane = new VBox();
    Label courseInfo
    Label siteTemplate
    Label pageStyle
    TableView courseMidTable
    */
       workspaceComponent.getCoursePane().getStyleClass().add(PANE);
       workspaceComponent.getCourseUpperPane().getStyleClass().add(PANE);
       workspaceComponent.getCourseMiddlePane().getStyleClass().add(PANE);
       workspaceComponent.getCourseLowerPane().getStyleClass().add(PANE);
       workspaceComponent.getCourseInfo().getStyleClass().add(PANE_HEADER_LABEL);
       workspaceComponent.getSitePage().getStyleClass().add(PANE_SUBHEADER_LABEL);
       workspaceComponent.getSiteTemplate().getStyleClass().add(PANE_SUBHEADER_LABEL);
       workspaceComponent.getPageStyle().getStyleClass().add(PANE_SUBHEADER_LABEL);
      // workspaceComponent.getCourseMiddleTable().getStyleClass().add(CLASS_TA_TABLE);
       
       
    // RECITATION DATA STYLE
    /* recitation pane
        VBox recitationPane = new VBox();
        VBox recitationUpperPane = new VBox();
        VBox recitationLowerPane = new VBox();
        HBox recitationHeaderPane = new HBox();
        TableView recitationTable
        Label recitationHeader*/
    
    workspaceComponent.getRecitationPane().getStyleClass().add(PANE);
    workspaceComponent.getRecitationUpperPane().getStyleClass().add(PANE);
    workspaceComponent.getRecitationLowerPane().getStyleClass().add(PANE);
    workspaceComponent.getRecitationHeaderPane().getStyleClass().add(HBOX);
    workspaceComponent.getrecitationHeader().getStyleClass().add(PANE_HEADER_LABEL);
    workspaceComponent.getRecitationAddHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    //TableView <> recitationTable = workspaceComponent.getRecitationTable();
    //workspaceComponent.getRecitationTable().getStyleClass().add(CLASS_TA_TABLE);
    //for (TableColumn recitationtableColumn : recitationTable.getColumns()) {
    //        recitationtableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
    //    }
    
    // SCHEDULE DATA STYLE
    
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
    workspaceComponent.getSchedulePane().getStyleClass().add(PANE);
    workspaceComponent.getCalendarPane().getStyleClass().add(PANE);
    workspaceComponent.getScheduleUpperPane().getStyleClass().add(PANE);
    workspaceComponent.getScheduleLowerPane().getStyleClass().add(PANE);
    workspaceComponent.getScheduleItemPane().getStyleClass().add(HBOX);
    workspaceComponent.getScheduleHeader().getStyleClass().add(PANE_HEADER_LABEL);
    workspaceComponent.getScheduleItemHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getCalendarHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getScheduleAddHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    //workspaceComponent.getScheduleTable().getStyleClass().add(CLASS_TA_TABLE);
       
    // PROJECT DATA STYLE
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
    Label studentAddHeader*/
    workspaceComponent.getProjectPane().getStyleClass().add(PANE);
    workspaceComponent.getProjectUpperPane().getStyleClass().add(PANE);
    workspaceComponent.getProjectLowerPane().getStyleClass().add(PANE);
    workspaceComponent.getProjectHeader().getStyleClass().add(PANE_HEADER_LABEL);
    workspaceComponent.getTeamHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getStudentHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getTeamAddHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getStudentAddHeader().getStyleClass().add(PANE_SUBHEADER_LABEL);
    workspaceComponent.getTeamHbox().getStyleClass().add(HBOX);
    workspaceComponent.getstudentHbox().getStyleClass().add(HBOX);
    
    //workspaceComponent.getScheduleTable().getStyleClass().add(CLASS_TA_TABLE);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        TAWorkspace workspaceComponent = (TAWorkspace)app.getWorkspaceComponent();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) {
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
}