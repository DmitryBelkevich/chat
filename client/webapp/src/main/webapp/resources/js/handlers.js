/**
 * components
 */

var messagesOutputTextArea = document.getElementById("messagesOutputTextArea");
var messagesInputTextArea = document.getElementById("messagesInputTextArea");
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

var sendListener = function () {
    var str = messagesInputTextArea.value;

    if (str == "")
        return;

    sendMessage(str);
};

/**
 * add listeners
 */

sendButton.addEventListener("click", sendListener);