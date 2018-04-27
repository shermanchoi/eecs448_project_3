var editButton = document.getElementById("editbutton");
var saveButton = document.getElementById("savebutton");
var cancelButton = document.getElementById("cancelbutton");
var bioText = document.getElementById("biotext");
var bioTextContent = bioText.textContent;
var bioEdit = document.getElementById("bioedit");
bioEdit.value = bioTextContent.trim();
editButton.addEventListener('click', editClick, false);
cancelButton.addEventListener('click', cancelClick, false);

function editClick() {
    bioText.style.display = "none";
    editButton.style.display = "none";
    bioEdit.style.display = "block";
    cancelButton.style.display = "inline";
    saveButton.style.display = "inline";
}

function cancelClick() {
    bioEdit.style.display = "none";
    cancelButton.style.display = "none";
    saveButton.style.display = "none";
    bioText.style.display = "block";
    editButton.style.display = "inline";
}


