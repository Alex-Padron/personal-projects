
$(function() {
    focus = "None"
    tinkering_time = 0
    developing_time = 0
    reading_time = 0
    inc_timer()
});

function start_timer(new_focus) {
    console.log("setting new focus " + new_focus)
    focus = new_focus
}

function display_times() {
    $("#Tinkering").get(0).innerHTML = "Tinkering time: " + tinkering_time
    $("#Reading").get(0).innerHTML = "Reading time: " + reading_time
    $("#Developing").get(0).innerHTML = "Developing time: " + developing_time
}

function inc_timer() {
    switch(focus) {
    case "Tinkering":
	tinkering_time += 1
	break;
    case "Reading":
	reading_time += 1
	break;
    case "Developing":
	developing_time += 1
	break;
    }
    display_times()
    setTimeout(inc_timer, 1000)
}
