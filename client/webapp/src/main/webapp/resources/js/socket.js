var host = "127.0.0.1";
var port = "9999";
var socket = new WebSocket("ws://" + host + ":" + port);

socket.onopen = function() {
    alert("Соединение установлено.");
};

socket.onclose = function(event) {
    if (event.wasClean) {
        alert('Соединение закрыто чисто');
    } else {
        alert('Обрыв соединения'); // например, "убит" процесс сервера
    }
    alert('Код: ' + event.code + ' причина: ' + event.reason);
};

socket.onmessage = function(event) {
    alert("Получены данные " + event.data);
};

socket.onerror = function(error) {
    alert("Ошибка " + error.message);
};