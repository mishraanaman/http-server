const http = require("http");

const PORT = 3000;

const friends = [
  {
    id: 0,
    name: "Sir Isaac Newton",
  },
  {
    id: 2,
    name: "Sir Albert Einstein",
  },
  {
    id: 3,
    name: "Sir John Jacobs",
  },
];

const server = http.createServer((req, res) => {
  const items = req.url.split("/");
  if (req.method === "POST" && items[1] === "friends") {
    req.on("data", (data) => {
      const friend = data.toString();
      console.log("Request:", friend);
      friends.push(JSON.parse(friend));
    });
    req.pipe(res); //Piping read stream to write stream
  } else if (req.method === "GET" && items[1] === "friends") {
    // res.writeHead(200, {
    //   "Content-Type": "text/json",
    // });
    res.statusCode = 200;
    res.setHeader("Content-Type", "application/json");
    if (items.length === 3) {
      const itemIndex = Number(items[2]);
      res.end(JSON.stringify(friends[itemIndex]));
    } else {
      res.end(JSON.stringify(friends));
    }
  } else if (req.method === "GET" && items[1] === "messages") {
    res.write("<html>");
    res.write("<body>");
    res.write("<li>Hello Isaac</li>");
    res.write("<li>What are your thoughts on Astronomy</li>");
    res.write("</ul>");
    res.write("</body>");
    res.end();
  } else {
    res.statusCode = 404;
  }
  res.end();
});

server.listen(PORT, () => {
  console.log(`Listening on port ${PORT}`);
});

//Go through Same Origin Policy
//Go through Cross Origin Policy
