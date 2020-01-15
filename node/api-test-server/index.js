const http = require("http");
const https = require("https");
var url = require("url");
require("dotenv").config();

const HOSTNAME = "127.0.0.1";
const PORT = 3000;
const NAG_HOST = "api.nordicapigateway.com";
const NAG_PORT = 443;

const server = http.createServer((request, result) => {
  if (request.url === "/init") {
    let post = https.request(
      postOptions(NAG_HOST, NAG_PORT, "/v1/authentication/initialize"),
      res => {
        var chunks = [];
        res.on("data", chunk => {
          chunks.push(chunk);
        });
        res.on("end", () => {
          var body = Buffer.concat(chunks);
          result.statusCode = 200;
          result.setHeader("Content-Type", "application/json");

          result.end(body.toString());
          console.log(new Date(), body.toString());
        });
      }
    );

    let payload = "";
    request
      .on("data", data => {
        payload += data;
      })
      .on("end", () => {
        payload = JSON.parse(payload);
        post.write(
          JSON.stringify({
            userHash: "1234567890",
            language: payload.language,
            redirectUrl: payload.redirectUrl
          })
        );
        post.end();
      });
  } else if (request.url == "/tokens") {
    let post = https.request(
      postOptions(NAG_HOST, NAG_PORT, "/v1/authentication/tokens"),
      res => {
        var chunks = [];
        res.on("data", chunk => {
          chunks.push(chunk);
        });
        res.on("end", () => {
          var body = Buffer.concat(chunks);
          result.statusCode = 200;
          result.setHeader("Content-Type", "application/json");
          result.end(body.toString());
          console.log(new Date(), body.toString());
        });
      }
    );

    let payload = "";
    request
      .on("data", data => {
        payload += data;
      })
      .on("end", () => {
        payload = JSON.parse(payload);
        post.write(
          JSON.stringify({
            code: payload.code.toString()
          })
        );
        post.end();
      });
  } else if (request.url == "/accounts") {
    let payload = "";
    request
      .on("data", data => {
        payload += data;
      })
      .on("end", () => {
        payload = JSON.parse(payload);
        console.log(
          getOptions(NAG_HOST, NAG_PORT, payload.token, "/v2/accounts")
        );
        let get = https.request(
          getOptions(NAG_HOST, NAG_PORT, payload.token, "/v2/accounts"),
          res => {
            var chunks = [];
            res.on("data", chunk => {
              chunks.push(chunk);
            });
            res.on("end", () => {
              var body = Buffer.concat(chunks);
              result.statusCode = 200;
              result.setHeader("Content-Type", "application/json");
              console.log(new Date(), body.toString());
              result.end(body.toString());
            });
          }
        );
        console.log(get.url);
        get.end();
      });
  } else if (request.url.includes("/accounts/transactions")) {
    var url_parts = url.parse(request.url, true);
    var query = url_parts.query;
    var id = query.id;
    var pagingToken = query.pagingToken;
    console.log("pagingToken: " + pagingToken);
    var fromDate = "2010-01-01";
    let payload = "";
    request
      .on("data", data => {
        payload += data;
      })
      .on("end", () => {
        payload = JSON.parse(payload);
        geturl = "/v2/accounts/" + id + "/transactions?fromDate=" + fromDate;
        if (pagingToken != null) {
          geturl += "&pagingToken=" + pagingToken;
        }
        options = getOptions(NAG_HOST, NAG_PORT, payload.token, geturl);
        console.log();
        let get = https.request(options, res => {
          var chunks = [];
          res.on("data", chunk => {
            chunks.push(chunk);
          });
          res.on("end", () => {
            var body = Buffer.concat(chunks);
            result.statusCode = 200;
            result.setHeader("Content-Type", "application/json");
            console.log(new Date(), body.toString());
            result.end(body.toString());
          });
        });
        console.log(get.url);
        get.end();
      });
  } else if (request.url == "/create-payment") {
    let post = https.request(
      postOptions(NAG_HOST, NAG_PORT, "/v1/payments/create"),
      res => {
        var chunks = [];
        res.on("data", chunk => {
          chunks.push(chunk);
        });
        res.on("end", () => {
          var body = Buffer.concat(chunks);
          result.statusCode = 200;
          result.setHeader("Content-Type", "application/json");
          result.end(body.toString());
          console.log(new Date(), body.toString());
        });
      }
    );

    let payload = "";
    request
      .on("data", data => {
        payload += data;
      })
      .on("end", () => {
        payload = JSON.parse(payload);
        post.write(
          JSON.stringify({
            userHash: "1234567890",
            language: payload.language,
            redirectUrl: payload.redirectUrl,
            request: {
              transactionText: "Test payment",
              destination: {
                iban: {
                  ibanNumber: "FI000001"
                }
              },
              message: "Message for the receiver of the payment",
              amount: { value: "1.00", currency: "DKK" },
              execution: {
                type: "Instant"
              }
            }
          })
        );
        post.end();
      });
  } else {
    result.statusMessage = 200;
    result.setHeader("Content-Type", "application/json");
    result.end(JSON.stringify({ greetings: "From Aarhus with ❤" }));
  }
});

const postOptions = function(host, port, path) {
  return {
    host,
    port,
    path,
    method: "POST",
    headers: {
      "X-Client-ID": process.env.CLIENT_ID,
      "X-Client-Secret": process.env.CLIENT_SECRET,
      "Content-Type": "application/json",
      "Cache-Control": "no-cache"
    }
  };
};

const getOptions = function(host, port, token, path) {
  return {
    host,
    port,
    path,
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "X-Client-ID": process.env.CLIENT_ID,
      "X-Client-Secret": process.env.CLIENT_SECRET,
      "Content-Type": "application/json",
      "Cache-Control": "no-cache"
    }
  };
};

server.listen(PORT, HOSTNAME, () => {
  console.log(`Server running at http://${HOSTNAME}:${PORT}/`);
});
