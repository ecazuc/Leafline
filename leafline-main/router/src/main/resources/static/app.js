const stompClient = new StompJs.Client();

stompClient.onConnect = (frame) => {
  setConnected(true);
  console.log('Connected: ' + frame);
  stompClient.subscribe('/domain/' + ($("#domain").val() || 'acme') + '/messages', (message) => {
    showMessage(message.body);
  });
};

stompClient.onWebSocketError = (error) => {
  console.error('WebSocket error: ', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#messages").html("");
}

function connect() {
  stompClient.configure({
    brokerURL: $("#url").val() || 'wss://cpoo-router.mightycode.tech/router'
  });
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  setConnected(false);
  console.log("Disconnected");
}

function sendMessage() {
  stompClient.publish({
    destination: "/router/route",
    body: JSON.stringify({
      'from': ($("#from").val() || 'alice@acme'),
      'to': ($("#to").val() || 'bob@acme'),
      'type': 'text/plain',
      'body': ($("#body").val() || 'This is the message')
    })
  });
}

function showMessage(message) {
  $("#messages").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendMessage());
});
