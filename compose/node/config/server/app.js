const moment = require("moment");
const express = require("express");
const { serverInfo } = require("./serverInfo");
const app = express();
const port = process.env.NODE_PORT || 5000;
let data = moment().format("DD/MM/YYYY HH:mm:ss");

function start() {
  serverInfo();

  app.listen(port, () => {
    console.log(
      `Servidor API Node utilizando Express iniciado na porta ${port}` +
        ". Serviço iniciado em: " +
        data
    );
  });

  function sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }
}

app.get("/", async (req, res) => {
  console.log("Hello World!");
});

module.exports = { app, start };
