// DATA TO LOAD
var hws;
var daysOfWeek;
var redInc;
var greenInc;
var blueInc;

function HW(hDate, hTime, hTitle, hTopic, hLink, hCriteria) {
    this.date = hDate;
    this.time = hTime;
    this.title = hTitle;
    this.topic = hTopic;
    this.link = hLink;
    this.criteria = hCriteria;
}
function ScheduleDate(sMonth, sDay) {
    this.month = sMonth;
    this.day = sDay;
}
function initHWs() {
    redInc = 10;
    greenInc = 10;
    blueInc = 5;
    
    daysOfWeek = new Array(7);
    daysOfWeek[0]=  "Sunday";
    daysOfWeek[1] = "Monday";
    daysOfWeek[2] = "Tuesday";
    daysOfWeek[3] = "Wednesday";
    daysOfWeek[4] = "Thursday";
    daysOfWeek[5] = "Friday";
    daysOfWeek[6] = "Saturday";
    
    var dataFile = "./js/ScheduleDataInHWs.json";
    loadData(dataFile);
    var dataFile2 = "./js/TeamsAndStudents.json";
    loadData2(dataFile2);
}
function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
	loadJSONData(json);
        addHWs();
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


function loadJSONData(data) {    
    // LOAD HWs DATA
    hws = new Array();
    for (var i = 0; i < data.hws.length; i++) {
        var hwData = data.hws[i];
        var hwDate = new ScheduleDate(hwData.month, hwData.day);
        var hw = new HW(hwDate, hwData.time, hwData.title, hwData.topic, hwData.link, hwData.criteria);
        hws[i] = hw;
    }
}

function setNavbars(data)
{
    for(var i = 0; i< data.site_pages.length; i++)
    {
        var rawSI = data.site_pages[i];
        var use = rawSI.use;
        var navbarTitle = rawSI.navbarTitle;
        if(use===false && navbarTitle.valueOf()!==("HWs")  )
        {
            var id = navbarTitle.toLowerCase() +"_link";
            var element = document.getElementById(id);
            element.parentNode.removeChild(element);
        }   
    }
}

function addHWs() {
    var tBody = $("#hws");
    var red = 240;
    var green = 240;
    var blue = 255;
    for (var i = 0; i < hws.length; i++) {
        var hw = hws[i];
        var day = hw.date.day;
        var month = hw.date.month;
        var dayOfWeek = getDayOfWeek(day,month);
        
        // THE FIRST CELL
        var textToAppend = "<tr class=\"hw\" style=\"background-color:rgb(" + red + "," + green + "," + blue + ")\">"
                            + "<td class=\"hw\" style=\"padding-right: 60px\">"
                                + "<br />";
        if (hw.link.valueOf() === "none".valueOf()) {
            textToAppend += hw.title;
        }
        else {
            textToAppend += "<a href=\"" + hw.link + "\">" + hw.title + "</a>";
        }
        textToAppend += " - " + hw.topic + "<br /><br /></td>";
        
        // THE SECOND CELL
        textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
                        + "<br />" + dayOfWeek + ", " + month + "/" + day
                        + "<br /><br /><br />"
                        + "</td>";
                       
        textToAppend += "</tr>";
        tBody.append(textToAppend);
        red -= redInc;
        green -= greenInc;
        blue -= blueInc;
    }
}
function getDayOfWeek(gDay, gMonth) {
    var date = new Date();
    date.setDate(gDay);
    date.setMonth(gMonth-1);
    return daysOfWeek[date.getDay()];
}