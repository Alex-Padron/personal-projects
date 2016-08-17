$(function(){
	//load the current state from storage
	chrome.storage.local.get("InSession", function(items){
		InSession = items.InSession

		if (InSession == null || InSession == "false"){
			//not currently in a session, load the new session page
			window.location = "newTab.html"
		} else {
			//is a session, load the session tab
			window.location = "sessionTab.html"
		}
	});
});