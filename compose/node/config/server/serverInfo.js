function serverInfo() {
  const os = require("os");

  const format = (bytes) => {
    return (bytes / (1024 * 1024)).toFixed(2) + " MB";
  };

  const totalMemory = os.totalmem();
  const freeMemory = os.freemem();
  const maxMemory = Math.max(
    os.totalmem() - os.freemem(),
    os.totalmem() * 0.15
  );

  console.log(
    "========================== Memory Info =========================="
  );
  console.log("Free memory: " + format(freeMemory));
  console.log("Allocated memory: " + format(totalMemory - freeMemory));
  console.log("Max memory: " + format(maxMemory));
  console.log(
    "Total free memory: " +
      format(freeMemory + (maxMemory - (totalMemory - freeMemory)))
  );
  console.log(
    "================================================================="
  );
  console.log("Using Node.js " + process.version);
  console.log("Started in: " + new Date().toLocaleString());
  console.log(
    "=================================================================\n"
  );
}

module.exports = { serverInfo };
