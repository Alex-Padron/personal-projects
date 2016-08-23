$(function(){
	if (window.location.hostname == "www.facebook.com"){
		console.log("im in fbbbb")
		$('#timesUpModal').modal('show');
	} else {
		console.log("ur boring af")
	}
	
});


//width of sidebar
var width = '250px';
sidebar();

//doesn't work now, but eventually make window 
// resizing work with sidebar
document.addEventListener("resize", sidebar);

function sidebar() {
  //resolve html tag, which is more dominant than <body>
  var html;
  if (document.documentElement) {
    html = $(document.documentElement); //just drop $ wrapper if no jQuery
  } else if (document.getElementsByTagName('html') && document.getElementsByTagName('html')[0]) {
    html = $(document.getElementsByTagName('html')[0]);
  } else if ($('html').length > -1) {//drop this branch if no jQuery
    html = $('html');
  } else {
    alert('no html tag retrieved...!');
    throw 'no html tag retrieved son.';
  }

  //position
  if (html.css('position') === 'static') { //or //or getComputedStyle(html).position
    html.css('position', 'relative');//or use .style or setAttribute
  }

  //right offset
  var currentWidth = html.css('width');
  if (currentWidth === 'auto') {
    currentWidth = 1280;
    console.log("auto")
  } else {
    currentWidth = parseFloat($('html').css('width')); //parseFloat removes any 'px' and returns a number type
  }
  console.log(html.css('width'))
  console.log(currentWidth)
  console.log(parseFloat(width))
  console.log(currentWidth-parseFloat(width))
  html.css(
    'width',     //make sure we're -adding- to any existing values
    currentWidth - parseFloat(width) + 'px'
  );
  console.log(html.css('width'))

  // var iframeId = 'someSidebar';
  // if (document.getElementById(iframeId)) {
  //   alert('id:' + iframeId + 'taken please dont use this id!');
  //   throw 'id:' + iframeId + 'taken please dont use this id!';
  // }
  // html.append(
  //   '<iframe id="'+iframeId+'" scrolling="no" allowtransparency="false" '+
  //     'style="position: fixed; width: '+width+';border:none;z-index: 2147483647; top: 0px;'+
  //            'height: 100%;right: 0px;align: right">'+
  //   '</iframe>'
  // );
  // document.getElementById(iframeId).contentDocument.head.innerHTML =
  //   '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous"> \
  //   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous"> \
  //   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>';
  // document.getElementById(iframeId).contentDocument.body.innerHTML =
  //   '<style type="text/css">\
  //     body {          \
  //       height: 100%; \
  //       width: '+width+';        \
  //       border-style: solid; \
  //       margin: 0; \
  //       padding: 10px; \
  //       z-index: 2147483647;\
  //     }                     \
  //   </style>                \
  //   <h4>NOTES:</h4> \
  //   <ol id="notesList"></ol> \
  //   <textarea rows="4" cols="29" id="noteInput"></textarea> \
  //   <br> \
  //   <button id="addNoteButton" type="button" class="btn btn-default" style="float:right" onclick="addNote()">Add Note</button> \
  //   <br> \
  //   <a download href="https://pdos.csail.mit.edu/6.824/notes/l-bitcoin.txt"><button id="downloadNotesButton" type="button" class="btn btn-default">Download Notes</button></a>';

};

