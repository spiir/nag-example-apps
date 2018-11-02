const http = require('http');
const https = require('https');
require('dotenv').config()

const HOSTNAME = '127.0.0.1';
const PORT = 3000;
const NAG_HOST = 'api.nordicapigateway.com'
const NAG_PORT = 443

const server = http.createServer((request, result) => {
  if(request.url === '/init') {
    let post = https.request(postOptions(NAG_HOST, NAG_PORT, '/v1/authentication/initialize'), res => {
      var chunks = []
      res.on('data', (chunk) => {
        chunks.push(chunk);
      })
      res.on('end', () => {
        var body = Buffer.concat(chunks);
        result.statusCode = 200;
        result.setHeader('Content-Type', 'application/json');
        result.end(body.toString());
        console.log(new Date() ,body.toString());
      })
    })
  
    let payload = ''
    request.on('data', (data) => {
      payload += data;
    }).on('end', () => {
      payload = JSON.parse(payload);
      post.write(JSON.stringify({ 
        userHash: '1234567890',
        language: payload.language,
        redirectUrl: payload.redirectUrl,
      })
    );
    post.end()
    });
  } else if(request.url =='/tokens') {
    let post = https.request(postOptions(NAG_HOST, NAG_PORT, '/v1/authentication/tokens'), res => {
      var chunks = []
      res.on('data', (chunk) => {
        chunks.push(chunk);
      })
      res.on('end', () => {
        var body = Buffer.concat(chunks);
        result.statusCode = 200;
        result.setHeader('Content-Type', 'application/json');
        result.end(body.toString());
        console.log(new Date() ,body.toString());
      })
    })
  
    let payload = ''
    request.on('data', (data) => {
      payload += data;
    }).on('end', () => {
      payload = JSON.parse(payload);
      post.write(JSON.stringify({ 
        code: payload.code.toString(),
      })
    );
    post.end()
    });
  } else if(request.url == '/accounts') {
    let payload = ''
    request.on('data', (data) => {
      payload += data;
    }).on('end', () => {
      payload = JSON.parse(payload);
      console.log(getOptions(NAG_HOST, NAG_PORT, payload.token, '/v1/accounts'))
      let get = https.request(getOptions(NAG_HOST, NAG_PORT, payload.token, '/v1/accounts'), res => {
        var chunks = []
        res.on('data', (chunk) => {
          chunks.push(chunk);
        })
        res.on('end', () => {
          var body = Buffer.concat(chunks);
          result.statusCode = 200;
          result.setHeader('Content-Type', 'application/json');
          result.end(body.toString())
          console.log(new Date() ,body.toString());
        })
  
      })
      console.log(get.url)
      get.end()
    });


  } else {
    result.statusMessage = 200
    result.setHeader('Content-Type', 'application/json')
    result.end(JSON.stringify({"greetings":"From Aarhus with ❤"}))
  }
});

const postOptions = function(host, port, path) {
  return {
    host,
    port,
    path,
    method: 'POST',
    headers: {
      "X-Client-ID": process.env.CLIENT_ID,
      "X-Client-Secret": process.env.CLIENT_SECRET,
      "Content-Type": "application/json",
      "Cache-Control": "no-cache",
    }
  }
}

const getOptions = function(host, port, token, path) {
  return {
    host,
    port,
    path,
    method: 'GET',
    headers: {
      "Authorization": `Bearer ${token}`,
      "X-Client-ID": process.env.CLIENT_ID,
      "X-Client-Secret": process.env.CLIENT_SECRET,
      "Content-Type": "application/json",
      "Cache-Control": "no-cache",
    }
  }
}

server.listen(PORT, HOSTNAME, () => {
  console.log(`Server running at http://${HOSTNAME}:${PORT}/`);
});
