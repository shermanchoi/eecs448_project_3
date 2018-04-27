var editButton = document.getElementById("editbutton");
editButton.addEventListener('click', editClick, false);

function editClick() {
    var bioText = document.getElementById("biotext");
    var bioTextContent = bioText.textContent;
    var bioEdit = document.getElementById("bioedit");
    bioEdit.value = bioTextContent.trim();

    bioText.style.display = "none";
    bioEdit.style.display = "block";
}


