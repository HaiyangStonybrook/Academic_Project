// DATA TO LOAD
var work;
var daysOfWeek;
var redInc;
var greenInc;
var blueInc;

function Work(hSemester, hProjects) {
    this.semester = hSemester;
    this.projects = hProjects;
}
function Project(hName, hStudents, hLink) {
    this.name = hName;
    this.students = hStudents;
    this.link = hLink;
}
function initProjects() {
    var dataFile = "./js/ProjectsData.json";
    loadData(dataFile);
    var dataFile2 = "./js/TeamsAndStudents.json";
    loadData2(dataFile2);
}
function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadJSONData(json);
        addProjects();
    });
}
function loadData2(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        setNavbars(json);
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
        if(use===false && navbarTitle.valueOf()!==("Projects")  )
        {
            var id = navbarTitle.toLowerCase() +"_link";
            var element = document.getElementById(id);
            element.parentNode.removeChild(element);
        }   
    }
}
function loadJSONData(data) {
    // LOAD Projects DATA
    work = new Array();
    for (var i = 0; i < data.work.length; i++) {
        var workData = data.work[i];
        var wProjects = new Array();
        for (var j = 0; j < workData.projects.length; j++) {
            var projectData = workData.projects[j];
            var pStudents = new Array();
            for (var k = 0; k < projectData.students.length; k++) {
                pStudents[k] = projectData.students[k];
            }
            var project = new Project(projectData.name, pStudents, projectData.link);
            wProjects[j] = project;
        }
        wWork = new Work(data.work[i].semester, wProjects);
        work[i] = wWork;
    }
}

function addProjects() {
    var div = $("#project_tables");
    for (var i = 0; i < work.length; i++) {
        var wWork = work[i];
        var text = "<h3>" + wWork.semester + " Projects</h3>"
                + "<table><tbody>";        
        var projects = wWork.projects;
        for (var j = 0; j < projects.length; j+=2) {
            var project = projects[j];
            text += "<tr>";
            text += getProjectCell(project);
            if ((j + 1) < projects.length) {
                project = projects[j + 1];
                text += getProjectCell(project);
            }
            text += "</tr>";        
        }
        text += "</tbody></table><br /><br />";
        div.append(text);
    }
}
function getProjectCell(project) {
    var text = "<td><a href=\""
            + project.link
            + "\"><img src=\"./images/projects/"
            + project.name.replace(/\s/g, '')
            + ".png\" /></a><br />"
            + "<a href=\""
            + project.link
            + "\">" + project.name + "</a><br />"
            + "by ";
    for (var k = 0; k < project.students.length; k++) {
        var t = project.students[k];
        
        //if ((k + 1) < project.students.length)
        //    text += ", ";
        text += t;
    }
    text += "<br /><br /></td>";
    return text;
}