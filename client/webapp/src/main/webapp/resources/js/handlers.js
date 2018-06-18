/**
 * components
 */

var sendButton = document.getElementById("sendButton");
var messagesTextArea = document.getElementById("messagesTextArea");
var inputTextArea = document.getElementById("inputTextArea");

/**
 * handlers
 */

var sendMessage = function (str) {
    socket.send(str);

    inputTextArea.focus();
    inputTextArea.value = "";
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
    var str = inputTextArea.value;

    if (str == "")
        return;

    sendMessage(str);
};

/**
 * add listeners
 */

sendButton.addEventListener("click", sendListener);