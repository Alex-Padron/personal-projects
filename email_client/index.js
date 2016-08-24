$(function() {
    error = "None"
    students = [["Alex", "alexander.f.padron@gmail.com"], ["Mom", "padron.robin@newfairfieldschools.org"], ["Also Mom", "robin.padron@gmail.com"], ["Scott", "scott.padron1.25@gmail.com"]]
    ip = "<IPADDR>"
    for (i = 0; i < students.length; i++) {
	students[i] = {name: students[i][0], email: students[i][1], sendEmail: false}
    }
    loadStudents(students)
    not_prepared = "Dear Parent, ::Your child was not prepared for class <DATE>. Since spare items are generally not provided, coming to class unprepared might prevent your child from participating fully in the class. ::Please make sure that your child has all the required supplies, and please remind him or her to bring them to class."

    no_homework = "Dear Parent, ::Your child did not turn in his homework that was due <DATE>. ::Completing homework is critical for student success. Strugglig students have many resources available to them. The Student Journal has a concise summary of the section notes; the textbook has clear, worked-out examples. The online version of the textbook has a video of each example, for students who find following the textbook challenging. Students can also get extra help during their study hall by going to the Learning Center. In addition, I am almost always available for extra help before and after school, as well as during my office hours."
});

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
    $("#studentContainer").get(0).rows[students.length - index - 1].innerHTML = makeHTML(index)
}

function loadStudents(students) {
    studentContainer = $("#studentContainer").get(0)
    length = students.length
    for (i = 0; i < length; i++) {
	row = studentContainer.insertRow(0);
	row.innerHTML = makeHTML(i)
    }
}

function setStudentsToFalse() {
    for (i = 0; i < students.length; i++) {
	students[i].sendEmail = false
	$("#studentContainer").get(0).rows[students.length - i - 1].innerHTML = makeHTML(i)
    }
}
