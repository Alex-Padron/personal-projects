$(function() {
	setTimeout(function(){ 
		$('#unapprovedModal').modal('show');
	}, 1000);

	$("#editlist").click(function(){
		goSessionTab();
	});
});

function goSessionTab() {
	window.location.replace("sessionTab.html");
}