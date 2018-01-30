package csg.workspace;

import djf.controller.AppFileController;
import djf.ui.AppGUI;
import static csg.TAManagerProp.*;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import csg.TAManagerApp;
import csg.TAManagerProp;
import csg.data.CSGData;
import csg.data.TeachingAssistant;
import csg.file.CSGFiles;
import csg.file.TimeSlot;
import csg.style.TAStyle;
import static csg.style.TAStyle.CLASS_HIGHLIGHTED_GRID_CELL;
import static csg.style.TAStyle.CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN;
import static csg.style.TAStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE;
import csg.transaction.ComboBox_Trans;
import csg.transaction.ComboBox_Wrap;
import csg.transaction.DeleteTA_Trans;
import csg.transaction.DeleteTa_Wrap;
import csg.transaction.EndComboBox_Trans;
import csg.transaction.EndComboBox_Wrap;
import csg.transaction.ToggleTa_Trans;
import csg.transaction.ToggleTa_Wrap;
import csg.transaction.Trans;
import csg.transaction.UnderGrad_Trans;
import csg.transaction.UnderGrad_Wrap;
import csg.transaction.UpdateTa_Trans;
import csg.transaction.UpdateTa_Wrap;
import csg.transaction.addTA_Trans;
import csg.transaction.addTA_Wrap;
import csg.transaction.jTPS_Transaction;
import csg.workspace.TAWorkspace;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file toolbar.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class TAController {

    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    TAManagerApp app;
    public static Trans tps;
    static DeleteTa_Wrap DeleteTA_wrap;
    static UpdateTa_Wrap UpdateTa_oldta;
    static UpdateTa_Wrap UpdateTa_newta;
    static ToggleTa_Wrap ToggleTa_ta;
    static ComboBox_Wrap startComboBox;
    static EndComboBox_Wrap endComboBox;
    static UnderGrad_Wrap undergrad_wrap;
    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(TAManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        tps = new Trans(initApp);

    }

    /**
     * This helper method should be called every time an edit happens.
     */
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = app.getGUI();
        gui.getFileController().markAsEdited(gui);
    }

    /**
     * This method responds to when the user requests to add a new TA via the
     * UI. Note that it must first do some validation to make sure a unique name
     * and email address has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String addText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()); //UPDATE_BUTTON_TEXT
        String updateText = props.getProperty(TAManagerProp.UPDATE_BUTTON_TEXT.toString());
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        EmailValidator validator = new EmailValidator();
        TableView taTable = workspace.getTATable();
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        CSGData data = (CSGData) app.getDataComponent();

        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        //PropertiesManager props = PropertiesManager.getPropertiesManager();

        if (taTable.getSelectionModel().isEmpty()) {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            
            // DID THE USER NEGLECT TO PROVIDE A TA NAME?
            if (name.isEmpty()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));
            } // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
            else if (email.isEmpty()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));
            } // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
            else if (data.containsTA(name, email)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
            } else if (!validator.validate(email)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(INVALID_EMAIL_TITLE), props.getProperty(INVALID_EMAIL_MESSAGE));
            } // EVERYTHING IS FINE, ADD A NEW TA
            else {
                // ADD THE NEW TA TO THE DATA
                data.addTA(name, email, false);
                TeachingAssistant ta = new TeachingAssistant(name,email, false);  // underGrad
                // add transaction 
                addTA_Wrap addTA_ta = new addTA_Wrap(ta, app);
                jTPS_Transaction tr = new addTA_Trans(addTA_ta);
                tps.addTransaction(tr);
                // CLEAR THE TEXT FIELDS
                nameTextField.setText("");
                emailTextField.setText("");

                // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
                //nameTextField.requestFocus();

                // WE'VE CHANGED STUFF
                markWorkAsEdited();
            }
        } else {
            Object selectedItem = taTable.getSelectionModel().getSelectedItem();
            TeachingAssistant ta = (TeachingAssistant) selectedItem;
            String oldName = ta.getName();
            String oldEmail = ta.getEmail();

            String newName = nameTextField.getText();
            String newEmail = emailTextField.getText();

            if (newName.isEmpty()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));
            } // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
            else if (newEmail.isEmpty()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));
            } else if (!validator.validate(newEmail)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(INVALID_EMAIL_TITLE), props.getProperty(INVALID_EMAIL_MESSAGE));
            } else {
                ArrayList<String> cellKey = new ArrayList<String>();
                TeachingAssistant oldTa = new TeachingAssistant(oldName,oldEmail, false); // underGrad
                
                TeachingAssistant newTa = new TeachingAssistant(newName,newEmail,false); // underGrad
                // add transaction 
                UpdateTa_oldta = new UpdateTa_Wrap(ta,cellKey,app );  // oldta
                UpdateTa_newta = new UpdateTa_Wrap(newTa,cellKey,app ); // newta
                String UpdateTa_oldname = UpdateTa_oldta.getTA().getName();
                String UpdateTa_newname = UpdateTa_newta.getTA().getName();
                jTPS_Transaction tr = new UpdateTa_Trans(UpdateTa_oldta, UpdateTa_newta );  // put old ta in transaction
                tps.addTransaction(tr);
                
                // ADD THE NEW TA TO THE DATA
                data.removeTA(oldName);
                data.addTA(newName, newEmail, false);  // underGrad
                
                
                HashMap map = data.getOfficeHours();
                for (Object k : map.keySet()) {
                    String key = (String) k;
                    StringProperty prop = (StringProperty) map.get(key);
                    String text = prop.getValue();

                    if (text.equals(oldName)
                            || (text.contains(oldName + "\n"))
                            || (text.contains("\n" + oldName))) {
                        cellKey.add(key);
                    }
                }
                
               
                // CLEAR THE TEXT FIELDS
                nameTextField.setText("");
                emailTextField.setText("");
                workspace.getAddButton().setText(addText);
                
                // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
                //nameTextField.requestFocus();

                HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
                for (Label label : labels.values()) {
                    if (!newName.equals(oldName)) {
                        if (label.getText().equals(oldName)
                                || (label.getText().contains(oldName + "\n"))
                                || (label.getText().contains("\n" + oldName))) {
                            String newLabel = label.getText().replaceAll(oldName, newName);
                            label.setText(newLabel);
                        }
                    }
                }
                // WE'VE CHANGED STUFF
                markWorkAsEdited();
            }
        }
    }

    /**
     * handle clear button
     */
    public void handleClearTA() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String addText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()); //UPDATE_BUTTON_TEXT
        String updateText = props.getProperty(TAManagerProp.UPDATE_BUTTON_TEXT.toString());
        
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        //String name = nameTextField.getText();
        //String email = emailTextField.getText();
        nameTextField.clear();
        emailTextField.clear();
        taTable.getSelectionModel().clearSelection();
        workspace.getClearButton().setDisable(true);
        nameTextField.requestFocus();
        workspace.getAddButton().setText(addText);
    }

    /**
     * This function provides a response for when the user presses a keyboard
     * key. Note that we're only responding to Delete, to remove a TA.
     *
     * @param code The keyboard code pressed.
     */
    public void handleKeyPress(KeyCode code) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String addText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()); //UPDATE_BUTTON_TEXT
        String updateText = props.getProperty(TAManagerProp.UPDATE_BUTTON_TEXT.toString());
        // DID THE USER PRESS THE DELETE KEY?

        if (code == KeyCode.DELETE) {
            // GET THE TABLE

            TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
            TableView taTable = workspace.getTATable();
            CSGData data = (CSGData) app.getDataComponent();
            TextField nameTextField = workspace.getNameTextField();
            TextField emailTextField = workspace.getEmailTextField();

            // IS A TA SELECTED IN THE TABLE?
            Object selectedItem = taTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // GET THE TA AND REMOVE IT
                TeachingAssistant ta = (TeachingAssistant) selectedItem;
                String taName = ta.getName();

                ArrayList<String> cellKey = new ArrayList<String>();
                HashMap map = data.getOfficeHours();
                for (Object k : map.keySet()) {
                    String key = (String) k;
                    StringProperty prop = (StringProperty) map.get(key);
                    String text = prop.getValue();

                    if (text.equals(taName)
                            || (text.contains(taName + "\n"))
                            || (text.contains("\n" + taName))) {
                        cellKey.add(key);
                    }
                }

                DeleteTA_wrap = new DeleteTa_Wrap(ta, cellKey, app);
                jTPS_Transaction tr = new DeleteTA_Trans(DeleteTA_wrap);
                tps.addTransaction(tr);

                data.removeTA(taName);

                // AND BE SURE TO REMOVE ALL THE TA'S OFFICE HOURS
                HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
                for (Label label : labels.values()) {
                    String text = label.getText();
                    if (label.getText().equals(taName)
                            || (label.getText().contains(taName + "\n"))
                            || (label.getText().contains("\n" + taName))) {
                        data.removeTAFromCell(label.textProperty(), taName);
                    }
                }
                Object selectedItem2 = taTable.getFocusModel().getFocusedItem();
                if (selectedItem2 != null) {
                    TeachingAssistant ta2 = (TeachingAssistant) selectedItem2;
                    String name = ta2.getName();
                    String email = ta2.getEmail();
                    nameTextField.setText(name);
                    emailTextField.setText(email);
                    workspace.getAddButton().setText(updateText);
                }

                if (selectedItem2 == null) {
                    this.handleClearTA();
                }
                // WE'VE CHANGED STUFF
                markWorkAsEdited();
            }

        }
    }

    /**
     * This function provides a response for when the user clicks on the office
     * hours grid to add or remove a TA to a time slot.
     *
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();

        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant) selectedItem;
            String taName = ta.getName();
            CSGData data = (CSGData) app.getDataComponent();
            String cellKey = pane.getId();

            ToggleTa_ta = new ToggleTa_Wrap(ta, app, cellKey);
            jTPS_Transaction tr = new ToggleTa_Trans(ToggleTa_ta);
            tps.addTransaction(tr);
            
            // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
            data.toggleTAOfficeHours(cellKey, taName);

            // WE'VE CHANGED STUFF
            markWorkAsEdited();
        }
    }

    
    public void handleUndergradToggle(TeachingAssistant ta){
    
        undergrad_wrap = new UnderGrad_Wrap(ta.getName(), app);
        jTPS_Transaction tr = new UnderGrad_Trans(undergrad_wrap);
            tps.addTransaction(tr);
    }
    
    // load name and email to textfield
    public void handleSelectTA() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String addText = props.getProperty(TAManagerProp.ADD_BUTTON_TEXT.toString()); //UPDATE_BUTTON_TEXT
        String updateText = props.getProperty(TAManagerProp.UPDATE_BUTTON_TEXT.toString());
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();

        // loda name and email from select TA
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (!taTable.getSelectionModel().isEmpty()) {
            TeachingAssistant ta = (TeachingAssistant) selectedItem;
            String name = ta.getName();
            String email = ta.getEmail();
            nameTextField.setText(name);
            emailTextField.setText(email);
            workspace.getAddButton().setText(updateText);
        }
    }

    void handleGridCellMouseExited(Pane pane) {
        String cellKey = pane.getId();
        CSGData data = (CSGData) app.getDataComponent();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();

        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }
    }

    void handleGridCellMouseEntered(Pane pane) {
        String cellKey = pane.getId();
        CSGData data = (CSGData) app.getDataComponent();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();

        // THE MOUSED OVER PANE
        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_CELL);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }
    }

    // Start time handler
    public void handleStartTime(Object oldTime, Object newTime) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        CSGFiles file = (CSGFiles) app.getFileComponent();

        int old_initStartHour = data.getStartHour(); // 9
        boolean deleteTA = true;

        String old_initStartHourStr = file.parseToTime("" + old_initStartHour); // initial old start time "9:00am"
        String oldTimeStr = (String) oldTime;  // param 1 
        String newTimeStr = (String) newTime;  // param 2
        Object endTime = workspace.getEndTime().getSelectionModel().getSelectedItem(); // get the endTime form endTime comboBox
        String endTimeStr = (String) endTime;
        //if(endTimeStr!=null){

        if (oldTime != null && newTime != null) {
            int endTimeInt = file.parseToInteger(endTimeStr); // int for endtime 20
            int oldTimeInt = file.parseToInteger(oldTimeStr); // int for oldtime 9
            int newTimeInt = file.parseToInteger(newTimeStr); // int for newtime 13

            if (newTimeInt >= endTimeInt) { // start > end, wrong
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(COMBO_BOX_TIME_ERROR_TITLE), props.getProperty(COMBO_BOX_TIME_ERROR_MESSAGE));
                 workspace.enableCombox(false);
                Platform.runLater(() -> workspace.getStartTime().getSelectionModel().select(old_initStartHourStr));
                 workspace.enableCombox(true);
                
            } else { // start < end , right 
                if (newTimeInt <= old_initStartHour) {
                    deleteTA = true;
                } else {                              //if(newTimeInt>old_initStartHour){ //newtime is after 9, may cut ta
                    boolean ask = false;
                    int removeRows = (newTimeInt - old_initStartHour) * 2; // contain :00 and :30 for each hour
                    for (int i = 1; i < removeRows; i++) {    // row start at 1
                        for (int j = 2; j <= 6; j++) {           // col 2-6 
                            String cellKey = "" + j + "_" + i;
                            StringProperty prop = data.getOfficeHours().get(cellKey);
                            String cellText = prop.getValue();
                            if (!cellText.equals("")) {
                                ask = true;
                                break;
                            }
                        }
                    }
                    if (ask == true) {
                        AppYesNoCancelDialogSingleton yesNo = AppYesNoCancelDialogSingleton.getSingleton();
                        yesNo.show(props.getProperty(COMBO_BOX_CUT_TA_TITLE), props.getProperty(COMBO_BOX_CUT_TA_MESSAGE));
                        String choice = yesNo.getSelection();
                        if (choice.equals(AppYesNoCancelDialogSingleton.YES)) {
                            deleteTA = true;
                        } else if (choice.equals(AppYesNoCancelDialogSingleton.NO)) {
                            // deleteTA = false
                            deleteTA = false;
                        } else {
                            deleteTA = false;
                        }
                    }
                }
                if (deleteTA == true) {
                     markWorkAsEdited();
                    //TAData oldData = data; 
                    ArrayList<TimeSlot> reBuildOfficeHour = TimeSlot.buildOfficeHoursList(data);
                    data.setStartHour(newTimeInt);
                    workspace.enableCombox(false);
                    workspace.resetWorkspace();
                    workspace.reloadOfficeHoursGrid(data);
                    
                    for (TimeSlot slot : reBuildOfficeHour) {
                        String time = slot.getTime();
                        time = time.replace("_", ":");
  
                        int timeSlot = file.parseToInteger(time);
                        if (timeSlot >= newTimeInt && timeSlot <= endTimeInt) {
                            data.addOfficeHoursReservation(slot.getDay(), slot.getTime(), slot.getName());
                        }
                    }
                    workspace.enableCombox(true);
                    startComboBox = new ComboBox_Wrap(reBuildOfficeHour, app, oldTimeInt, newTimeInt); // old is 9, new is any
                    jTPS_Transaction tr = new ComboBox_Trans(startComboBox);
                    tps.addTransaction(tr);
                } else {       // don't change office hour, start box value is init time 
                     workspace.enableCombox(false);
                    Platform.runLater(() -> workspace.getStartTime().getSelectionModel().select(old_initStartHourStr));
                     workspace.enableCombox(true);
                    //Platform.runLater(()->workspace.);
                }

            }
        }
    }

    public void handleEndTime(Object oldTime, Object newTime) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        CSGData data = (CSGData) app.getDataComponent();
        CSGFiles file = (CSGFiles) app.getFileComponent();

        int old_initEndHour = data.getEndHour(); // 8
        boolean deleteTA = true;

        String old_initEndHourStr = file.parseToTime("" + old_initEndHour); // initial old end time "20:00pm"
        String oldTimeStr = (String) oldTime;  // param 1
        String newTimeStr = (String) newTime;  // param 2
        Object old_initStartTime = workspace.getStartTime().getSelectionModel().getSelectedItem(); // get the endTime form endTime comboBox
        String old_initStartTimeStr = (String) old_initStartTime;

        if (oldTimeStr != null && newTimeStr != null) {
            int old_initStartTimeInt = file.parseToInteger(old_initStartTimeStr); // 9 int for startTime
            int oldTimeInt = file.parseToInteger(oldTimeStr); // int for oldtime  // 20
            int newTimeInt = file.parseToInteger(newTimeStr); // int for newtime  // 14

            if (newTimeInt <= old_initStartTimeInt) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(COMBO_BOX_TIME_ERROR_TITLE), props.getProperty(COMBO_BOX_TIME_ERROR_MESSAGE));
                workspace.enableCombox(false);
                Platform.runLater(() -> workspace.getEndTime().getSelectionModel().select(old_initEndHourStr));
                workspace.enableCombox(true);
                
            } else { // start time <= end time
                if (newTimeInt >= old_initEndHour) { // new end time > old end time
                    deleteTA = true;
                } else {      // new end time < old end time
                    boolean ask = false;
                    int removeRows = (old_initEndHour - newTimeInt) * 2; // contain :00 and :30 for each hour
                    for (int i = 1; i < removeRows; i++) {    // row start at 1
                        for (int j = 2; j <= 6; j++) {           // col 2-6 
                            String cellKey = "" + j + "_" + i;
                            StringProperty prop = data.getOfficeHours().get(cellKey);
                            String cellText = prop.getValue();
                            if (!cellText.equals("")) {
                                ask = true;
                                break;
                            }
                        }
                    }
                    if (ask = true) {
                        AppYesNoCancelDialogSingleton yesNo = AppYesNoCancelDialogSingleton.getSingleton();
                        yesNo.show(props.getProperty(COMBO_BOX_CUT_TA_TITLE), props.getProperty(COMBO_BOX_CUT_TA_MESSAGE));
                        String choice = yesNo.getSelection();
                        if (choice.equals(AppYesNoCancelDialogSingleton.YES)) {
                             deleteTA = true;
                        } else if (choice.equals(AppYesNoCancelDialogSingleton.NO)) {
                            // deleteTA = false
                            deleteTA = false;
                        } else {
                            deleteTA = false;
                        }
                    }
                }

                if (deleteTA = true) {
                    markWorkAsEdited();
                    ArrayList<TimeSlot> reBuildOfficeHour = TimeSlot.buildOfficeHoursList(data);
                    data.setEndHour(newTimeInt);
                    workspace.enableCombox(false);
                    workspace.resetWorkspace();
                    workspace.reloadOfficeHoursGrid(data);
                    for (TimeSlot slot : reBuildOfficeHour) {
                        String time = slot.getTime();
                        time = time.replace("_", ":");
                        int timeSlot = file.parseToInteger(time);
                        if (timeSlot >= old_initStartTimeInt && timeSlot <= newTimeInt) {
                            data.addOfficeHoursReservation(slot.getDay(), slot.getTime(), slot.getName());
                        }   
                    }
                    
                    workspace.enableCombox(true);
                    endComboBox = new EndComboBox_Wrap(reBuildOfficeHour, app, oldTimeInt, newTimeInt); // old is 9, new is any
                    jTPS_Transaction tr = new EndComboBox_Trans(endComboBox);
                    tps.addTransaction(tr);
                }else {       // don't change office hour, start box value is init time 
                    workspace.enableCombox(false);
                    Platform.runLater(() -> workspace.getEndTime().getSelectionModel().select(old_initEndHour));
                    workspace.enableCombox(true);    
                    //Platform.runLater(()->workspace.);
                }
            }
        }
    }
}
