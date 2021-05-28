function editorInit() {
    $('.editor').each(function (i, e) {
        editor(e);
    });
}

function editor(e) {
    if (typeof CKEDITOR !== "undefined") {
        var bodyClass = typeof editorMode !== "undefined" ? editorMode : "article-editor";
        var editor = CKEDITOR.replace(e, {
            extraPlugins: 'uploadimage,base64image,pastebase64,youtube',
            removePlugins: 'image,help,elementspath,about,source,iframe',
            removeDialogTabs: 'image:advanced;link:advanced',
            bodyClass: bodyClass,
            height: "30vh",
            contentsCss: bodyClass === "article-editor" ? [window.CKEDITOR_BASEPATH + 'skins/moono-lisa/editor_style.css'] : [ window.CKEDITOR_BASEPATH + 'skins/moono-lisa/document.css', window.CKEDITOR_BASEPATH + 'skins/moono-lisa/editor_style.css' ],
            allowedContent: true,
            toolbar: [
                { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
                { name: 'colors', items : [ 'TextColor','BGColor' ] },
                { name: 'document', items : [ 'NewPage','DocProps','Preview','Print','-','Templates' ] },
                { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
                { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-' ] },
                { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton',
                        'HiddenField' ] },
                '/',
                { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
                { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote',
                        '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
                { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
                { name: 'insert', items : [ 'base64image','Youtube','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','-' ] },
                { name: 'tools', items : [ 'ShowBlocks', 'Maximize' ] }
            ]
        });
        editor.on('change', function( evt ) {
            // getData() returns CKEditor's HTML content.
            let data = evt.editor.getData();
            let text = stripHtml(data).trim();
            $(e).val(text === '' ? text : data);
        });
    }
}

function editorHide() {
    CKEDITOR.instances.editor.setData('');
}

function showDialog() {
    $('body').addClass("show-dialog");
}

function hideDialog() {
    $('body').removeClass("show-dialog");
}

function stripHtml(html) {
    var tmp = document.createElement("DIV");
    tmp.innerHTML = html;
    return tmp.textContent || tmp.innerText || "";
}