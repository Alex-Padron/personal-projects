/*
Code to load the valid urls from memory into the new session modal.
We don't have to handle removing here because this is dealt with by the
"website" class
*/
$(function() {

	//load stored data from newSessionModal.js
	var stored_sites;
	chrome.storage.local.get("ValidURLS",function (items){
	 		stored_sites = items.ValidURLS;
	 		setURLs(JSON.parse(stored_sites));
	});

	//make the new session button clear notes
	$("#StartNewSession").click(function() {
		chrome.storage.local.set({"Notes": ""}, function(){
			console.log("Notes reinitialized")
		})

		chrome.storage.local.set({"InSession": "true"}, function(){
			console.log("Entered Session")

			//start the timer
			session_time = $("#usr_time").val()*60000;
			d = new Date()
			
			chrome.storage.local.set({"sessionEndTime": JSON.stringify(d.getTime() + session_time)}, function(){
				console.log("session end time stored");
			})
		})
	});
});

function setURLs(stored_sites){
	console.log("session loading stored sites:", stored_sites);
	//get a reference to the (currently empty) table
	site_container = $("#permittedWebsitesContainer").get(0);
	//find the number of elements to add
	length = stored_sites.length;
	//make and add the inner html to each element in the table
	for (i = 0; i < length; i++) {
		row = site_container.insertRow(0);
		button_id = stored_sites[i][0]
		row.innerHTML = "<a id=\"" + button_id + "\" href='" + stored_sites[i][1] + "'>" + stored_sites[i][0] + "</a></td><td><a href=\"#\" class=\"website\"><span class=\"glyphicon glyphicon-remove\" style=\"color:\#F55E61\"></span></a>";
	}
}