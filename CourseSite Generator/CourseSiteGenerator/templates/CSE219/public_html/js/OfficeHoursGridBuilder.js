// DATA TO LOAD
var startHour;
var endHour;
var daysOfWeek;
var officeHours;
var undergradTAs;

function buildOfficeHoursGrid() {
    var dataFile = "./js/OfficeHoursGridData.json";
    loadData(dataFile, loadOfficeHours);
    var dataFile2 = "./js/TeamsAndStudents.json";
    loadData(dataFile2, loadTeamsAndStudents);
}

function loadData(jsonFile, callback) {
    $.getJSON(jsonFile, function(json) {
        callback(json);
    });
}

function loadOfficeHours(json) {
    initDays(json);
    addTableHTML();
    addUndergradTAs(json);
    addOfficeHours(json);
}
function loadTeamsAndStudents(json)
{
    setNavbars(json);
    initData(json);
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
        if(use===false && navbarTitle.valueOf()!==("Syllabus")  )
        {
            var id = navbarTitle.toLowerCase() +"_link";
            var element = document.getElementById(id);
            element.parentNode.removeChild(element);
        }   
    }
}
function addTableHTML()
{
    var myList = document.getElementsByTagName("p");
    var elementTBC = myList[7];
    var newElement = document.createElement("table");
    newElement.innerHTML = "";
    newElement.setAttribute("id", "gradTable");
    elementTBC.parentNode.replaceChild(newElement, elementTBC);
    
}

function initDays(data) {
    // GET THE START AND END HOURS
    startHour = parseInt(data.startHour);
    endHour = parseInt(data.endHour);

    // THEN MAKE THE TIMES
    daysOfWeek = new Array();
    daysOfWeek[0] = "MONDAY";
    daysOfWeek[1] = "TUESDAY";
    daysOfWeek[2] = "WEDNESDAY";
    daysOfWeek[3] = "THURSDAY";
    daysOfWeek[4] = "FRIDAY";    
}

function addUndergradTAs(data) {
    var tas = $("#undergrad_tas");
    //var tasPerRow = 4;
    var numTAs = data.undergrad_tas.length;
    
    
    
    var undergradText = "";
        undergradText = "<tr>";
    var currentColU = 0;
        
    var gradText = "";
        gradText = "<tr>";
    var currentColG = 0;
    
    for (var i = 0; i < data.undergrad_tas.length; ) 
    {
        
        var undergrad = data.undergrad_tas[i].undergrad;
        
        if(undergrad)
        {
            if(currentColU == 4)
            {
                undergradText += "</tr>";
                tas.append(undergradText);
                
                undergradText = "";
                undergradText = "<tr>";
                currentColU = 0;
                
            }
                
            
                undergradText += buildTACell(i, numTAs, data.undergrad_tas[i]);
                
                currentColU++;
                
                
            
        }
        else
        {
            if(currentColG == 4)
            {
                gradText += "</tr>";
                var ele = document.getElementById("gradTable");
                ele.innerHTML += gradText;
                
                 gradText = "";
                 gradText = "<tr>";
                 currentColG = 0;
                
            }
                gradText +=buildGradTACell(i, numTAs, data.undergrad_tas[i]);
                currentColG++;
                
                
        }
                //if next round is ending round
                //for undergrad tas
                if(i+1 >= data.undergrad_tas.length)
                {
                    if(currentColU!=4)
                    {
                        for(var q=0; q<4-currentColU; q++)
                        {
                            undergradText +="<td></td>";
                        }
                    }
                    undergradText +=  "</tr>";
                    tas.append(undergradText);
                //for grad tas
                    if(currentColG!=4)
                    {
                        for(var q=0; q<4-currentColG; q++)
                        {
                            gradText +="<td></td>";
                        }
                    }
                    gradText +=  "</tr>";
                    ///tas.append(undergradText);
                    var ele = document.getElementById("gradTable");
                    ele.innerHTML += gradText;
                }
                
        i++;
   
    }
    
    
}
function buildTACell(counter, numTAs, ta) {
    if (counter >= numTAs)
        return "<td></td>";

    var name = ta.name;
    var abbrName = name.replace(/\s/g, '');
    var email = ta.email;
    var text = "<td class='undergrad_tas'><img width='100' height='100'"
                + " src='./images/tas/" + abbrName + ".JPG' "
                + " alt='" + name + "' /><br />"
                + "<strong>" + name + "</strong><br />"
                + "<span class='email'>" + email + "</span><br />"
                + "<br /><br /></td>";
    return text;
}
function buildGradTACell(counter, numTAs, ta) {
    if (counter >= numTAs)
        return "<td></td>";

    var name = ta.name;
    var abbrName = name.replace(/\s/g, '');
    var email = ta.email;
    var text = "<td class='grad_tas'><img width='100' height='100'"
                + " src='./images/tas/" + abbrName + ".JPG' "
                + " alt='" + name + "' /><br />"
                + "<strong>" + name + "</strong><br />"
                + "<span class='email'>" + email + "</span><br />"
                + "<br /><br /></td>";
    return text;
}
function addOfficeHours(data) {
    for (var i = startHour; i < endHour; i++) {
        // ON THE HOUR
        var textToAppend = "<tr>";
        var amPm = getAMorPM(i);
        var displayNum = i;
        if (i > 12)
            displayNum = displayNum-12;
        textToAppend += "<td>" + displayNum + ":00" + amPm + "</td>"
                    + "<td>" + displayNum + ":30" + amPm + "</td>";
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_00" + amPm
                                + "\" class=\"open\"></td>";
        }
        textToAppend += "</tr>"; 
        
        // ON THE HALF HOUR
        var altAmPm = amPm;
        if (displayNum === 11)
            altAmPm = "pm";
        var altDisplayNum = displayNum + 1;
        if (altDisplayNum > 12)
            altDisplayNum = 1;
                    
        textToAppend += "<tr>";
        textToAppend += "<td>" + displayNum + ":30" + amPm + "</td>"
                    + "<td>" + altDisplayNum + ":00" + altAmPm + "</td>";
            
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_30" + amPm
                                + "\" class=\"open\"></td>";
        }
        
        textToAppend += "</tr>";
        var cell = $("#office_hours_table");
        cell.append(textToAppend);
    }
    
    // NOW SET THE OFFICE HOURS
    for (var i = 0; i < data.officeHours.length; i++) {
	var id = data.officeHours[i].day + "_" + data.officeHours[i].time;
	var name = data.officeHours[i].name;
	var cell = $("#" + id);
	if (name === "Lecture") {
	    cell.removeClass("open");
	    cell.addClass("lecture");
	    cell.html("Lecture");
	}
	else {
	    cell.removeClass("open");
	    cell.addClass("time");
            if (cell.html().toString().length == 0)
                cell.append(name);
            else
        	cell.append("<br />" + name);
	}
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}