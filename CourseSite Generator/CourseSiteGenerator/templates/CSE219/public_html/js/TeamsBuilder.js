// CONSTANTS
var IMG_PATH;
var LEAD_ROLE;
var PM_ROLE;
var DESIGN_ROLE;
var DATA_ROLE;
var MOBILE_ROLE;
var NONE;
var AVAILABLE_RED;
var AVAILABLE_GREEN;
var AVAILABLE_BLUE;

// DATA TO LOAD
var teams;
var teamNames;
var availableStudents;

function Team(initName, initRed, initGreen, initBlue, text_color) {
    this.name = initName;
    this.red = initRed;
    this.green = initGreen;
    this.blue = initBlue;
    this.students = new Array();
    this.text_color = text_color;
}

function Student(initLastName, initFirstName, initTeam, initRole) {
    this.lastName = initLastName;
    this.firstName = initFirstName;
    this.team = initTeam;
    this.role = initRole;
}

function initTeamsAndStudents() {
    IMG_PATH = "./images/students/";
    LEAD_ROLE = "Lead Programmer";
    PM_ROLE = "Project Manager";
    DESIGN_ROLE = "Lead Designer";
    DATA_ROLE = "Data Designer";
    MOBILE_ROLE = "Mobile Developer";
    NONE = "none";
    AVAILABLE_RED = 120;
    AVAILABLE_GREEN = 200;
    AVAILABLE_BLUE = 70;
    teams = new Array();
    teamNames = new Array();
    availableStudents = new Array();
    var dataFile = "./js/TeamsAndStudents.json";
    loadData(dataFile);
}

function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        setNavbars(json);
	loadTeams(json);
	loadStudents(json);
	initPage();
        initData(json);
    });
}
function initData(data)
{
    //1.
    var subject = data.subject;
    var number = data.number;
    var semester = data.semester;
    var year = data.year;
    var title = data.title_cd;
    
    var titleDataTBS = subject+" "+ number+" - "+ semester+" "+year+ "<br /> "+ title ;
    var elemTitle = document.getElementById("banner");
    elemTitle.innerHTML = titleDataTBS;
    //2.
    var instructorName = data.instructorName;
    var instructorHome = data.instructorHome;
    
    var aList = document.getElementsByTagName("a");
    var lastIndex = aList.length-1;
    var element = aList[lastIndex];
    
    element.innerHTML = instructorName;
    element.href = instructorHome;
    //3. left, right, banner
        
    var indexRight = aList.length-2;
    var indexLeft = aList.length-3;
    var indexBanner = 0;
    
    var eleLeft = aList[indexLeft];
    var eleRight = aList[indexRight];
    
    var eleBanner = aList[indexBanner];
    
    eleBanner.innerHTML ="<img class=\"sbu_navbar\" alt=\"Stony Brook University\" src=\""
        +  data.addrBannerImg  +   "\" />";
    
    eleLeft.innerHTML = "<img class=\"sunysb\" style=\"float:left\" src=\""  +   data.addrLeftImg 
            +  "\" alt=\"SBU\" />";
    
    eleRight.innerHTML ="<img style=\"float:right\" src=\""  
            + data.addrRightImg 
            +"\" alt=\"CS\" />";
    //4.
    var eleCss = document.getElementById("cssCBC");
    eleCss.href = data.styleSheet;
    
    
}
function setNavbars(data)
{
    for(var i = 0; i< data.site_pages.length; i++)
    {
        var rawSI = data.site_pages[i];
        var use = rawSI.use;
        var navbarTitle = rawSI.navbarTitle;
        if(use===false && navbarTitle.valueOf()!==("Home")  )
        {
            var id = navbarTitle.toLowerCase() +"_link";
            var element = document.getElementById(id);
            element.parentNode.removeChild(element);
        }   
    }
}
function loadTeams(data) {
    for (var i = 0; i < data.teams.length; i++) {
	var rawTeam = data.teams[i];
	var team = new Team(rawTeam.name, rawTeam.red, rawTeam.green, rawTeam.blue, rawTeam.text_color);
	teams[team.name] = team;
	teamNames[i] = team.name;
    }
}

function loadStudents(data) {
    var counter = 0;
    for (var i = 0; i < data.students.length; i++) {
	var rawStudent = data.students[i];
	var student = new Student(rawStudent.lastName, rawStudent.firstName, rawStudent.team, rawStudent.role);
	if (student.team == NONE)
	    availableStudents[counter++] = student;
	else {
	    var team = teams[student.team];
	    team.students[student.role] = student;
	}
    }
}

function initPage() {
    // FIRST ADD THE TEAM TABLES
    for (var i = 0; i < teamNames.length; i++) {
	var team = teams[teamNames[i]];
	var teamText =
		"<table style='background:rgb("
		+ team.red + ","
		+ team.green + ","
		+ team.blue + ")'>\n"
		+ "<tr>\n"
		+ "<td colspan='4' style='color:" + team.text_color + "'><strong>" + team.name + "</strong><br /></td>\n"
		+ "</tr>\n"
		+ "<tr>\n";
	teamText += addStudentToTeam(team.students[LEAD_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[PM_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[DESIGN_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[DATA_ROLE], team.text_color);
        if (team.students[MOBILE_ROLE]) {
            teamText += addStudentToTeam(team.students[MOBILE_ROLE], team.text_color);
        }
	teamText += "</tr>\n"
		+ "</table><br />\n";
	$("#teams_tables").append(teamText);
    }

    // THEN ADD THE REMAINING AVAILABLE STUDENTS
/*    var teamText =
	    "<table style='background:rgb("
	    + AVAILABLE_RED + ","
	    + AVAILABLE_GREEN + ","
	    + AVAILABLE_BLUE + ")'>\n"
		+ "<tr>\n"
		+ "<td colspan='4' style='font-size:16pt'><strong>No Team Yet</strong><br /><br /></td>\n"
		+ "</tr>\n"
		+ "<tr>\n";
    for (var i = 0; i < availableStudents.length; ) {
	teamText += "<tr>\n";
	for (var j = 0; j < 4; j++) {
	    if (i < availableStudents.length) {
		teamText += addStudentToTeam(availableStudents[i]);
		i++;
	    }
	}
	teamText += "</tr>\n";
    }
    teamText += "</table><br />\n";
    $("#teams_tables").append(teamText);
    */
}

function addStudentToTeam(student, text_color) {
    var text = "<td style='color:" + text_color + "'>\n"
	    + "<img src='" + IMG_PATH + cleanText(student.lastName)
	    + cleanText(student.firstName) + ".JPG' "
	    + "alt='" + student.firstName + " " + student.lastName + "' />\n"
	    + "<br clear='all' /><strong>" + student.firstName + " " + student.lastName;
    if (student.role != NONE) {
	text +=	"<br />" + student.role; 
    }
    text += "<br /><br /></strong></td>\n";
    return text;
}


function cleanText(text) {
    return text.replace("-", "").replace(" ", "");
}