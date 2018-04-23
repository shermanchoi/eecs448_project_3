function setTextArea() {
    let textA = document.getElementById("postcontent");
    scediter.create(textA, {format: 'bbcode', style: 'minified/themes/content/default.min.css'});
}

setTextArea();