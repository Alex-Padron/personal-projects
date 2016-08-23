$(function() {
	// add canned links
	setCannedLinks();
	//load stored data from newSessionModal.js
	var stored_sites;
	chrome.storage.local.get("ValidURLS",function (items){
	 		stored_sites = items.ValidURLS;
	 		setURLs(JSON.parse(stored_sites));
	});

	//make the save button actually save the notes
	saveButton = $("#sessionSaveButton")

	saveButton.on('click', function(){
		console.log("clicked save button")
		saveNotes()
	});
	
	//load the stored data to the saved notes section
	loadNotes()

	//load the time remaining
	chrome.storage.local.get("sessionEndTime", function(items){
		time = items.sessionEndTime
		console.log("Session will end at:", time)
	})

	$("#downl").click(function(){
		downloadNotes();
	})

	//load the time remaining
	var countdownContainer = document.getElementById("countdownContainer");
	chrome.storage.local.get("sessionEndTime", function(items){
		sessionEndTime = items.sessionEndTime
		var d = new Date()
		var currentTime = d.getTime()
		var msDiff = sessionEndTime-currentTime;
		if (msDiff > 0) {
			countdownContainer.style.visibility = 'visible';
			var min = (msDiff/1000/60) << 0;
	    var sec = (msDiff/1000) % 60;
			countdown("countdown", min, sec);
		}
	}); 

	$("#download").click(function() {
		downloadNotes();
		console.log("downloaded notes yes we did, about to exit session")
		chrome.storage.local.set({"InSession": "false"}, function(){
			console.log("Exited Session")
		});
		window.location = "newTab.html";
	});

});

function countdown(elementName, minutes, seconds) {
  var element, endTime, hours, mins, msLeft, time;

  function twoDigits(n) {
    return (n <= 9 ? "0" + n : n);
  }

  function updateTimer() {
    msLeft = endTime - (+new Date);
    if ( msLeft < 1000 ) {
    	// tell the user the session has ended
      element.innerHTML = "session's over!";
      $('#timesUpModal').modal('show');
    } else {
      time = new Date( msLeft );
      hours = time.getUTCHours();
      mins = time.getUTCMinutes();
      element.innerHTML = (hours ? hours + ':' + twoDigits( mins ) : mins) + ':' + twoDigits( time.getUTCSeconds() );
      setTimeout( updateTimer, time.getUTCMilliseconds() + 500 );
    }
  }

  element = document.getElementById(elementName);
  endTime = (+new Date) + 1000 * (60*minutes + seconds) + 500;
  updateTimer();
}


function loadNotes() {
	textbox = $("#savedNotesSession");
	default_message = "This is where your saved notes will appear so you can read them later"

	chrome.storage.local.get("Notes", function(items){
		notes = items.Notes 
		console.log(notes)
		if (notes != null && notes != "") {
			//load the notes to the textbox
			console.log("loaded notes:", notes)
			textbox.get(0).innerText = notes
		} else {
			//load the default text 
			console.log("loading default text")
			textbox.get(0).innerText = default_message
		}
	});
}

function setCannedLinks() {
	// get a reference to the (currently empty) table
	site_container = $("#permittedWebsitesContainer").get(0);
	// add canned websites
	row1 = site_container.insertRow(0);
	row1.innerHTML = "<a href='approvedSite.html?site=wikipedia'>https://en.wikipedia.org/wiki/Golf_ball</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";
	row2 = site_container.insertRow(0);
	row2.innerHTML = "<a href='approvedSite.html?site=6813'>http://web.mit.edu/6.813/www/sp16/</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";
	row3 = site_container.insertRow(0);
	row3.innerHTML = "<a href='approvedSite.html?site=easybib'>http://www.easybib.com/</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";
	row4 = site_container.insertRow(0);
	row4.innerHTML = "<a href='approvedSite.html?site=aerodynamics'>http://scitation.aip.org/content/aip/j...</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";	
}

function setURLs(stored_sites){
	console.log("session loading stored sites:", stored_sites);
	//get a reference to the (currently empty) table
	site_container = $("#permittedWebsitesContainer").get(0);
	//find the number of elements to add
	length = stored_sites.length;

	//make and add the inner html to each element in the table
	for (i = 0; i < length; i++) {
		row = site_container.insertRow(0);
		row.innerHTML = "<a style='pointer-events: none; cursor: default;''>" + stored_sites[i][0] + "</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";
	}
}

function goToCannedLink(link){
	console.log("going to canned link: " + link);
	
}

function saveNotes() {
	console.log("Saving notes")
	//get the new notes
	newNotesBox = $("#sessionNewNotes")
	console.log(newNotesBox)
	new_notes = newNotesBox.val();
	console.log("New notes are:", new_notes)
	//load the old notes
	chrome.storage.local.get("Notes", function(items){
		stored_notes = items.Notes;
		textbox = $("#savedNotesSession");

		var resulting_notes
		if (stored_notes != null) {
			resulting_notes = new_notes + "\n \n" + stored_notes
		} else {
			resulting_notes = new_notes
		}
		console.log("new notes are:", resulting_notes)
		//store the result back in chrome
		chrome.storage.local.set({"Notes": resulting_notes}, function(){
			console.log("notes saved")

			//update the saved notes text box
			textbox = $("#savedNotesSession")

			textbox.get(0).innerText = resulting_notes

			//clear the new notes box
			newNotesBox.val('')
		});
	});
}

function downloadNotes() {
	//get notes from text box
	var note_input = $("#noteInput").val();

	//add a text box element if not empty
	if (note_input != '') {
		var notesList = document.getElementById("savedNotesSession");
		$("#noteInput").val('');
		// Add the note
		var node = document.createElement("LI");
	  var textnode = document.createTextNode(note_input);
	  node.appendChild(textnode);
	  notesList.appendChild(node);
		$("#noteInput").val('');
		notesList.scrollTop = notesList.scrollHeight;
		notesList.style.backgroundColor = "#eee";
		notesList.style.minHeight= "20px";
		notesList.style.padding= "10px 15px 10px 30px";
	}
	$("#noteInput").focus();

	//load notes from storage
	chrome.storage.local.get("Notes", function(items) {
		old_notes = items.Notes
		newNotes = note_input + "\n" + old_notes
		//download the sum total of notes
		download(newNotes)
		//save the resulting notes in storage
		chrome.storage.local.set({"Notes": newNotes}, function(){
			console.log("New notes saved to storage:", newNotes)
		});
	});
}

function download(notes) {
	var textFileAsBlob = new Blob([notes], {type:'text/plain'});
	var filename = "refocusNotes";
	var downloadLink = document.createElement("a");
	downloadLink.download = filename;
	downloadLink.innerHTML = "Download File";
	if (window.URL != null) {
		// Chrome allows the link to be clicked
		// without actually adding it to the DOM.
		downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
	} else {
		// Firefox requires the link to be added to the DOM
		// before it can be clicked.
		downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
		downloadLink.onclick = destroyClickedElement;
		downloadLink.style.display = "none";
		document.body.appendChild(downloadLink);
	}
	downloadLink.click();
}