/*
 * INDEXBAR.JS
 * 
 * Basic Index Bar Structure
 * 	
 * 	<< 1 ... 7 8 9 10 11 ... 36 >>
 *
 * 	From the left:
 * 		--Left Arrow Anchor:		Links to the previous page
 * 		--Left number Anchor:		Links to to the first page
 * 		--Left dots span:		Encapsulates numbers between 1 and (current page number) - 2
 * 		--Left Proximate page anchors:	Links to pages closest to current page; range: (current page number) - 2
 * 		--Current page anchor: 		Center number in the index bar; denotes current page
 * 		--Right Proximate page anchors:	Links to pages closest to current page; range: (current page number + 2
 * 		--Right dots span:		Encapsulates numbers between (current page number) + 2 and right number
 * 		--Right number anchor:		Links to the last page
 * 		--Right Arrow Anchor:		Links to the next page
 *
 * 	Basic Generating Algorithm:
 * 		--IF (Right number) = 1 //Only one page
 * 			--Hide Left Arrow Anchor
 * 			--Hide Left Dots Span
 * 			--Hide Right Dots Span
 * 			--Hide Right Number Anchor
 * 			--Hide Right Arrow Anchor
 * 		--ELSE //More than one page
 * 			--IF (Current number - 2) <= 2 //Checking to see if 1 would be proximate
 * 				--Hide Left Dots span
 * 			--IF (Current number + 2) >= (Right number - 1) //Checking to see if the last page would be proximate
 * 				--Hide Right Dots span
 * 			--IF (Current number) = 1 //Checking to see if the current page is the first page
 * 				--Hide Left Arrow Anchor
 * 			--ELSE IF (Current number) = (Right number) //Checking to see if the current page is the last page
 * 				--Hide Right Arrow Anchor
 * 			--ELSE 
 * 				--SWITCH (current number - 2)
 * 					--CASE 0:
 * 						--Don't Generate any Left Proximate Anchors
 * 					--CASE 1:
 * 						--Generate a (current number - 1)  Left Proximate Anchor
 * 					--DEFAULT:
 * 						--Generate (current number - 2) and (current number - 1) Left Proximate
 * 						Anchors
 * 				--SWITCH (current number + 2)
 * 					--CASE (Right number + 1):
 * 						--Don't Generate any Right Proximate Anchors
 * 					--CASE (Right number):
 * 						--Generate a (current number + 1) Right Proximate Anchor
 * 					--DEFAULT:
 * 						--Generate (current number + 1) and (current number + 2) Right Proximate
 * 						Anchors
 */

debugger;
const PROXIMATE = 2; //Proximate constant; controls how many anchors are proximate to the current page
const LEFT_NUM = 1; //Left number; 1 in most cases
const RIGHT_NUM = 5; //TODO: Determine a way to get the number of pages of posts, bascially just then number of pages of posts
var current_page = 5; //TODO: Determine a way to get the current page

var indexBar = document.getElementById("indexbar"); //Get the indexbar div

/*
 * MAIN COMPONENT CONSTRUCTION
 */

var leftArrowAnchor = document.createElement("a");
leftArrowAnchor.setAttribute('id', 'leftarrowanchor');
leftArrowAnchor.setAttribute('href', 'linkToPreviousPage.html'); //TODO: Put in href that actually links to previous page
leftArrowAnchor.innerHTML = "<<";

var rightArrowAnchor = document.createElement("a");
rightArrowAnchor.setAttribute('id', 'rightarrowanchor');
rightArrowAnchor.setAttribute('href', 'linkToNextPage.html'); //TODO: Put in href that actually links to next page
rightArrowAnchor.innerHTML = ">>";

var leftNumber = document.createElement("a");
leftNumber.setAttribute('id', 'leftnumber');
leftNumber.setAttribute('href', 'linkToFirstPage.html'); //TODO: Put in href that actually links to the first page
leftNumber.innerHTML = LEFT_NUM;

var rightNumber = document.createElement("a");
rightNumber.setAttribute('id', 'rightnumber');
rightNumber.setAttribute('href', 'linkToLastPage.html'); //TODO: Put in href that actually links to the last page
rightNumber.innerHTML = RIGHT_NUM;

var leftDotsSpan = document.createElement("span");
leftDotsSpan.setAttribute('id', 'leftdotsspan');
leftDotsSpan.innerHTML = "...";

var rightDotsSpan = document.createElement("span");
rightDotsSpan.setAttribute('id', 'rightdotsspan');
rightDotsSpan.innerHTML = "...";

var currentSpan = document.createElement("span"); //Span with current page number; corresponds to current page anchor above
currentSpan.setAttribute('id', 'currentspan');
currentSpan.innerHTML = current_page;

/*
 * COMPONENT ATTACHMENT
 */ 
    
debugger;
indexBar.appendChild(leftArrowAnchor);
indexBar.appendChild(leftNumber);
indexBar.appendChild(leftDotsSpan);

for (var i = (current_page - PROXIMATE); i < current_page; ++i) { //Put in left proximate anchors
    var proximate_anchor = document.createElement("a");
    proximate_anchor.setAttribute('class', 'leftproximate');
    proximate_anchor.setAttribute('href', 'linkToiPagesAgo.html'); //TODO: Put in href that actually links to current_page - i
    proximate_anchor.innerHTML = i;
    indexBar.appendChild(proximate_anchor);
}

indexBar.appendChild(currentSpan);

for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) { //Put in right proximate anchors
    var proximate_anchor = document.createElement("a");
    proximate_anchor.setAttribute('class', 'rightproximate');
    proximate_anchor.setAttribute('href', 'linkToiPagesAhead.html'); //TODO: Put in href that actually links to current_page + i
    proximate_anchor.innerHTML = i;
    indexBar.appendChild(proximate_anchor);
}

indexBar.appendChild(rightDotsSpan);
indexBar.appendChild(rightNumber);
indexBar.appendChild(rightArrowAnchor);

var leftProximateAnchors = document.querySelectorAll('a.leftproximate');
var rightProximateAnchors = document.querySelectorAll('a.rightproximate');
/*
 * COMPONENT HIDING/SHOWING (ALGORITHM)
 */

if (RIGHT_NUM == 1) {
    leftArrowAnchor.style.display = "none";
    leftNumber.style.display = "none";
    leftDotsSpan.style.display = "none";
    currentSpan.style.display = "inline";
    rightDotsSpan.style.display = "none";
    rightNumber.style.display = "none";
    rightArrowAnchor.style.display = "none";
    var j = 0;
    for (var i = (current_page - PROXIMATE); i < current_page; ++i) {
	if (i <= LEFT_NUM) {
	    leftProximateAnchors[j].style.display = "none";
	}
	++j;
    }
    j = 0; //Used to keep track of the rightProximateAnchors array
    for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) {
	if (i >= RIGHT_NUM) {
	    rightProximateAnchors[j].style.display = "none";
	}
	++j;
    }
}
else {
    if ((current_page - PROXIMATE) <= (LEFT_NUM + 1)) {
	leftDotsSpan.style.display = "none";
    }
    if ((current_page + PROXIMATE) >= (RIGHT_NUM - 1)) {
	rightDotsSpan.style.display = "none";
    }
    if (current_page == LEFT_NUM) {
	leftArrowAnchor.style.display = "none";
	leftNumber.style.display = "none";
    }
    else if (current_page == RIGHT_NUM) {
	rightArrowAnchor.style.display = "none";
	rightNumber.style.display = "none";
    }
    var j = 0;
    for (var i = (current_page - PROXIMATE); i < current_page; ++i) {
	if (i <= LEFT_NUM) {
	    leftProximateAnchors[j].style.display = "none";
	}
	++j;
    }
    j = 0; //Used to keep track of the rightProximateAnchors array
    for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) {
	if (i >= RIGHT_NUM) {
	    rightProximateAnchors[j].style.display = "none";
	}
	++j;
    }
}
