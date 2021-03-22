const initWebSocket = () => {
  const wsClient = new WebSocket('ws://localhost:8080/kingdom-updated')

  wsClient.onmessage = event => {
    const message = JSON.parse(event.data)
    console.log(message)
  }
}

window.onload = initWebSocket()
