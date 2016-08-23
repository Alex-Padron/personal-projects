$(function() {
    error = "None"
    students = [["Alex", "alexander.f.padron@gmail.com"], ["Mom", "padron.robin@newfairfieldschools.org"], ["Also Mom", "robin.padron@gmail.com"]]
    ip = "192.168.1.4"
    tmp = students
    students = new Array(students.length)
    for (i = 0; i < students.length; i++) {
	students[i] = [tmp[i], false]
	console.log("creating student ", students[i], " from ", tmp[i]);
    }
    loadStudents(students)
});

function setError(text) {
    error = text
    $("#errorDisplay").html("Selected Error: " + text)
}

function sendEmails() {
    if (error != "None") {
	for (i = 0; i < students.length; i++) {
	    if (students[i][1]) {
		var http = new XMLHttpRequest();
		var url = "http://" + ip + ":8080";
		var params = students[i][0][1] + "," + error.replace("<DATE>", $("#date").get(0).value);
		http.open("POST", url, true);
		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		http.send(params);
	    }
	}
	setStudentsToFalse()
    }
}

function makeHTML(student, index) {
    return "<p style=\"font-size: 50%\">" + student[0][0] + " " + student[1] + "</p><button onclick=\"clickIndex(" + index + ")\">change</button>"
}

function clickIndex(index) {
    students[index][1] = !students[index][1]
    $("#studentContainer").get(0).rows[students.length - index - 1].innerHTML = makeHTML(students[index], index)
}

function loadStudents(students) {
    studentContainer = $("#studentContainer").get(0)
    length = students.length
    for (i = 0; i < length; i++) {
	row = studentContainer.insertRow(0);
	row.innerHTML = makeHTML(students[i], i)
    }
}

function setStudentsToFalse() {
    for (i = 0; i < students.length; i++) {
	students[i][1] = false
	$("#studentContainer").get(0).rows[students.length - i - 1].innerHTML = makeHTML(students[i], i)
    }
}
