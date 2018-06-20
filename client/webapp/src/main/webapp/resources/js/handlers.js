/**
 * components
 */

var messagesOutputTextArea = document.getElementById("messagesOutputTextArea");
var messagesInputTextArea = document.getElementById("messagesInputTextArea");
var usersOutputTextArea = document.getElementById("usersOutputTextArea");
var sendButton = document.getElementById("sendButton");

/**
 * handlers
 */

var sendMessage = function (str) {
    socket.send(str);

    messagesInputTextArea.focus();
    messagesInputTextArea.value = "";
};

var getMessage = function (str) {
    messagesOutputTextArea.innerHTML += str + "\n";

    // scroll to bottom
    var magnet = 200;

    if (messagesOutputTextArea.scrollTop >= (messagesOutputTextArea.scrollHeight - messagesOutputTextArea.offsetHeight - magnet))
        messagesOutputTextArea.scrollTop = messagesOutputTextArea.scrollHeight - messagesOutputTextArea.offsetHeight;
};

/**
 * listeners
 */

var sendMessageActionListener = function () {
    var str = messagesInputTextArea.value;
    if (str.trim() == "")
        return;

    sendMessage(str);
};

var sendMessageKeyListener1 = function (e) {
    var keyCode = e.keyCode ? e.keyCode : e.which;

    if (keyCode == 13) {//Enter
        e.preventDefault();

        var str = messagesInputTextArea.value;
        if (str.trim() == "")
            return;

        sendMessage(str);
    }
};

/**
 * add listeners
 */

sendButton.addEventListener("click", sendMessageActionListener);
messagesInputTextArea.addEventListener("keydown", sendMessageKeyListener1);