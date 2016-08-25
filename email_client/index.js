geometry_b3 = 
[["ZiniAlimi@sbcglobal.net", "Alimi, Kimete"],
["paul.bruno@mac.com","Bruno, Andrew T"],
["rsdenike1@hotmail.com","Denike, Alyssa C"],
["afata@lakelandschools.org","Fata, Anthony C (T.J.)"],
["aefata@sbcglobal.net","Fata, Anthony C (T.J.)"],
["egarnecho@gmail.com","Garnecho-Lopez, Davin L"],
["kiggles74@aol.com","Guariglia, Shaelyn D"],
["moneyismyshot@aol.com","Guariglia, Shaelyn D"],
["maritza1169@hotmail.com","Halas, Scot T"],
["hayden13mid@att.net","Hayden, Christopher E"],
["llambropoulos@sbcglobal.net","Lambropoulos, Stylianos (Stelios)"],
["CTMaglio@aol.com","Maglio, Julianna D"],
["donna_marciante@sbcglobal.net","Marciante, Jennifer"],
["martinson23@gmail.com","Martinson, William E"],
["nas714@msn.com","Nason, Ryan"],
["fernandapesarini@hotmail.com","Pesarini, Ana Clara A"],
["pesarini_stenio@yahoo.com","Pesarini, Ana Clara A"],
["Quinn0056@att.net","Quinn, Kerrigan A"],
["KevinQ@us.ibm.com","Quinn, Kerrigan A"],
["kwroe2211@gmail.com","Roe, Thomas S"],
["milkshakecel@aol.com","Stelzel, Andrew R"],
["allycat726@gmail.com","Turner, Jillian S"],
["lvitrit@gmail.com","Vitrit, Joseph M"],
["billiewatson1999@yahoo.com","Watson, Morgan C"],
["SandyZheng203@yahoo.com", "Zheng, Vicki"]]

geometry_honors_1 = 
[["alexander.f.padron@gmail.com", "Alex"], 
["padron.robin@newfairfieldschools.org", "Mom"], 
["robin.padron@gmail.com", "Also Mom"]]

algebra_1 = 
[["alexander.f.padron@gmail.com", "Alex"], 
["padron.robin@newfairfieldschools.org", "Mom"], 
["scott.padron1.25@gmail.com", "Scott"]]
    
geometry = 
[["padron.robin@newfairfieldschools.org", "Mom"], 
["robin.padron@gmail.com", "Also Mom"], 
["scott.padron1.25@gmail.com", "Scott"]]

not_prepared = "Dear Parent, ::Your child was not prepared for class <DATE>. Since spare items are generally not provided, coming to class unprepared might prevent your child from participating fully in the class. ::Please make sure that your child has all the required supplies, and please remind him or her to bring them to class."

no_homework = "Dear Parent, ::Your child did not turn in his homework that was due <DATE>. ::Completing homework is critical for student success. Strugglig students have many resources available to them. The Student Journal has a concise summary of the section notes; the textbook has clear, worked-out examples. The online version of the textbook has a video of each example, for students who find following the textbook challenging. Students can also get extra help during their study hall by going to the Learning Center. In addition, I am almost always available for extra help before and after school, as well as during my office hours."

$(function() {
    error = "None"
    ip = "<IPADDR>"
    students = []
});

function fileToList(str) {
    list = str.split("\n")
    console.log(list)
}

function setClass(cls) {
    if (cls != "None") {
	clearStudents()
	if (cls == "geometry honors 1") {
	    students = geometry_honors_1.slice(0)
	} else if (cls == "algebra 1") {
	    students = algebra_1.slice(0)
	} else if (cls == "geometry") {
	    students = geometry.slice(0)
	} else if (cls == "geometry b3") {
	    students = geometry_b3.slice(0)
	}
	loadStudents(students)
    }
}
function setError(text) {
    error = text
    $("#errorDisplay").html("Selected Error: " + text)
}

function showEmailStatus(sent_any, failed_any) {
    status_text = $("#status").get(0)
    if (!sent_any && !failed_any) {
	status_text.innerHTML = "Status: Nothing Sent"
    } else if (sent_any && !failed_any) {
	status_text.innerHTML = "Status: Email Sent Successfully"
    } else if (!sent_any && failed_any) {
	status_text.innerHTML = "Status: Sending failed"
    } else {
	status_text.innerHTML = "Status: Bad state - some emails succeeded and some failed"
    }
}

function getError(error) {
    if (error == "no homework") {
	return no_homework
    } else if (error == "not prepared") {
	return not_prepared
    } else {
	return error
    }
}
function sendEmails() {
    if (error != "None") {
	status_text = $("#status").get(0)
	console.log(status_text)
	status_text.innerHTML = "Status: pending"
	sent_any = false
	failed_any = false
	for (i = 0; i < students.length; i++) {
	    student = students[i]
	    if (student.sendEmail) {
		var http = new XMLHttpRequest();
		var url = "http://" + ip + ":8080";
		var params = student.email + ":" + getError(error).replace("<DATE>", $("#date").get(0).value);
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		try {
		    http.send(params);
		    sent_any = true
		} catch(err) {
		    failed_any = true
		}
	    }
	}
	console.log("sent any is ", sent_any, " failed any is ", failed_any)
	showEmailStatus(sent_any, failed_any)
	setStudentsToFalse()
    }
}

function colorString(bool) {
    if (bool) {
	return "red"
    } else {
	return "green"
    }
}

function makeHTML(index) {
    student = students[index]
    return "</p><button style=\"color: " + colorString(student.sendEmail) 
	+ "\" onclick=\"clickIndex(" + index + ")\">" + student.name + "</button>"
}

function clickIndex(index) {
    showEmailStatus(false,false)
    students[index].sendEmail = !students[index].sendEmail
    $("#studentContainer").get(0).rows[index].innerHTML = makeHTML(index)
}

function loadStudents(students) {
    for (i = 0; i < students.length; i++) {
	students[i] = {name: students[i][1], email: students[i][0], sendEmail: false}
    }
    studentContainer = $("#studentContainer").get(0)
    for (i = 0; i < students.length; i++) {
	row = studentContainer.insertRow(-1);
	row.innerHTML = makeHTML(i)
    }
}

function clearStudents() {
    studentContainer = $("#studentContainer").get(0)
    for (i = 0; i < students.length; i++) {
	studentContainer.deleteRow(0)
    }
}

function setStudentsToFalse() {
    for (i = 0; i < students.length; i++) {
	students[i].sendEmail = false
	$("#studentContainer").get(0).rows[i].innerHTML = makeHTML(i)
    }
}
