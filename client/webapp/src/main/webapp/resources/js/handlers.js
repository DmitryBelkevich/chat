/**
 * components
 */

var sendButton = document.getElementById("sendButton");
var messagesTextArea = document.getElementById("messagesTextArea");
var messagesInputTextArea = document.getElementById("messagesInputTextArea");

/**
 * handlers
 */

var sendMessage = function (str) {
    socket.send(str);

    messagesInputTextArea.focus();
    messagesInputTextArea.value = "";
};

var getMessage = function (str) {
    messagesTextArea.innerHTML += str + "\n";

    // scroll to bottom
    var magnet = 200;

    if (messagesTextArea.scrollTop >= (messagesTextArea.scrollHeight - messagesTextArea.offsetHeight - magnet))
        messagesTextArea.scrollTop = messagesTextArea.scrollHeight - messagesTextArea.offsetHeight;
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