$(function() {	
	var cannedMap = {};
	cannedMap['wikipedia'] = 'https://en.wikipedia.org/wiki/Golf_ball';
	cannedMap['6813'] = 'http://web.mit.edu/6.813/www/sp16/';
	cannedMap['easybib'] = 'http://www.easybib.com/';
	cannedMap['aerodynamics'] = 'http://scitation.aip.org/content/aip/journal/jap/20/9/10.1063/1.1698540';
	
	document.getElementById("addNoteButton").addEventListener("click", addNote);
	var cannedFrame = document.getElementById("cannedFrame");
	var countdownContainer = document.getElementById("countdownContainer");
	var notesList = document.getElementById("notesList");
	var textArea = document.getElementById("noteInput");
	var siteKey = window.location.search.split("site=")[1];
	var notesHeight = $("#notesBarAll").height();
	var totalHeight = $('body').height();
	notesList.style.maxHeight = (totalHeight - notesHeight - 50) + "px";

	console.log(siteKey)
	if (siteKey in cannedMap) {
		console.log('sitekey: '+siteKey)
		console.log(cannedMap[siteKey])
		cannedFrame.src = cannedMap[siteKey];
	}

	$(window).on('resize', function(){
		totalHeight = $('body').height();
		notesList.style.maxHeight = (totalHeight - notesHeight - 50) + "px";
		// noteInput.style.width = 
	});

	//load the time remaining
	chrome.storage.local.get("sessionEndTime", function(items){
		sessionEndTime = items.sessionEndTime
		console.log("Session will end at:", sessionEndTime)
		var d = new Date()
		var currentTime = d.getTime()
		var msDiff = sessionEndTime-currentTime;
		if (msDiff > 0) {
			countdownContainer.style.visibility = 'visible';
			var min = (msDiff/1000/60) << 0;
	    var sec = (msDiff/1000) % 60;
			console.log(sessionEndTime, currentTime, msDiff, min, sec)
			countdown("countdown", min, sec);
		} 
	})

	$("#downloadNotesButton").click(function() {
		downloadNotes();
	});
	$("#download").click(function() {
		downloadNotes();
		chrome.storage.local.set({"InSession": "false"}, function(){
			console.log("Exited Session")
		});
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

function addNote() {
	var note_input = $("#noteInput").val();
	if (note_input != '') {
		var notesList = document.getElementById("notesList");
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

	//add the notes to storage
	chrome.storage.local.get("Notes", function(items){
		oldNotes = items.Notes
		newNotes = oldNotes + "\n" + note_input

		chrome.storage.local.set({"Notes": newNotes}, function(){
			console.log("New notes saved to storage:", newNotes)
		})
	});
	return true;
} 

function downloadNotes() {
	//get notes from text box
	var note_input = $("#noteInput").val();

	//add a text box element if not empty
	if (note_input != '') {
		var notesList = document.getElementById("notesList");
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
		newNotes = old_notes + "\n" + note_input
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