$(function() {

	//allows the add website button to add sites
    $("#addWebsite").click(function() {
		addWebsite();
	});

    //make it so that any of the add website text fields 
    //have an enter button
	$("#userWebsite").keypress(function(e){
		var key = e.which;
		if (key == 13) {
			addWebsite();
			return false;
		}
	});

	//this is the class for all of the website inputs
	//when they get removed, we want to update the UI
	//as well as remove the valid site from storage
	$(document).on('click', '.website', function () {

		//get the text name of the website we are removing
		html = $( this ).closest('tr').get(0).innerHTML
		text = html.replace(/<(?:.|\n)*?>/gm, '');
		//remove that website from storage
		removeWebsite(text)

  		$( this ).closest('tr').remove();
  		return false;
	});

	$('input[name=notimer]').change(function(){

	    if($(this).is(':checked'))
	    {
	    	console.log("checked")
	    	$("#usr_time").attr("disabled", true);
	    	$("#usr_time").val('');
	    	$("#usr_time").css("background-color","#D3D3D3");
	    }
	    else
	    {
	        console.log("not checked")
	        $("#usr_time").attr("disabled", false);
	        $("#usr_time").css("background-color","white");
	    }    

	});
});

//removes a given text name from chrome storage
function removeWebsite(text) {
	//remove the website from internal storage
	chrome.storage.local.get("ValidURLS",function (items){
			console.log("getting from storage");
	 		stored_sites = items.ValidURLS;
	 		removeSiteFromStorage(JSON.parse(stored_sites), text)
	});
}

//callback function for removeWebsite, after we get the current data from storage to update
function removeSiteFromStorage(stored_sites, text) {
	console.log("calling remove site")
	new_sites = []
	//add to new sites any data that doesn't match the text
	for (i = 0; i < stored_sites.length; i++) {
		if (stored_sites[i][0] != text) {
			new_sites.push(stored_sites[i])
		}
	}
	//replace the old storage with new sites
	chrome.storage.local.set({"ValidURLS": JSON.stringify(new_sites)}, function() {
		console.log("storage successful")
	});
}

//add a website, both the button and to storage
function addWebsite() {
	var user_input = $("#userWebsite").val();
	$("#userWebsite").val('');
	console.log("adding user input", user_input)
	// Prepend the website
	$("#permittedWebsitesContainer").prepend("<tr><td><a id=\"" +  user_input+"\" href='"+user_input+"'>"+user_input+"</a></td><td><a href='#' class='website'><span class='glyphicon glyphicon-remove' style='color:#F55E61'></span></a></td></tr>")
	console.log("added permittedWebsitesContainer")
	$("#userWebsite").val('');
	$("#userWebsite").focus();

	//textbox = $("#" + user_input)

	console.log("adding to storage")
	//add the website to the internal storage
	chrome.storage.local.get("ValidURLS",function (items){
			console.log("getting from storage");
	 		stored_sites = items.ValidURLS;
	 		if (stored_sites != null) {
		 		setURL(JSON.parse(stored_sites), user_input);
	 		}else {
	 			setURL([], user_input);
	 		}
	});

	return true;
}

function addSite(user_input) {
	console.log("adding user input", user_input)
	// Prepend the website
	$("#permittedWebsitesContainer").prepend("<tr><td><a id=\"" +  user_input+"\" href='"+user_input+"'>"+user_input+"</a></td><td><a href='#' class='website'><span class='glyphicon glyphicon-remove' style='color:#F55E61'></span></a></td></tr>")
	console.log("added permittedWebsitesContainer")

	console.log("adding to storage")
	//add the website to the internal storage
	chrome.storage.local.get("ValidURLS",function (items){
			console.log("getting from storage");
	 		stored_sites = items.ValidURLS;
	 		if (stored_sites != null) {
		 		setURL(JSON.parse(stored_sites), user_input);
	 		}else {
	 			setURL([], user_input);
	 		}
	});

	return true;
}
//add a url to chrome storage
function setURL(stored_sites, user_input){
	console.log("adding to previous values:", stored_sites);
	tup = [user_input[0], user_input[1]];
	stored_sites.push(tup);

	str = JSON.stringify(stored_sites);

	chrome.storage.local.set({"ValidURLS": str},function (){
    	console.log("ValidURLS stored as:", str);
	});
}
