# nag-example-apps

Sample apps showcasing the possible use cases with the Nordic API Gateway SDK.

## Getting started

The sample app is called QuickSprout and is a app that enables users to get an overview of their savings. It is still a work in progress. For now it is only possible to authenticate with banks and list the user's accounts.

Find the Swagger documentation at https://api.nordicapigateway.com/docs/index.html.

### Sign up for Nordic API Gateway

1. Go to https://www.nordicapigateway.com/ and sign up
2. Obtain your `Client ID` and `Client Secret` (find them at https://developer.nordicapigateway.com/apps)

### Setup Node project

If you have not already implemented services to communicate with Nordic API Gateway, you can use the test server implementation found in `node/api-test-server`.

1. Open the project in your favorite JavaScript editor (we use Visual Studio Code at Spiir)
2. Create an environment file (`.env`) and paste the following code into it:

```
CLIENT_ID=<Client ID>
CLIENT_SECRET=<Client ID>
```

- `CLIENT_ID` – This variable contains the client ID (found at https://developer.nordicapigateway.com/apps)
- `CLIENT_SECRET` – This variable contains the client secret (found at https://developer.nordicapigateway.com/apps)

3. Run `npm install` and then `node index.js` to start the server.

This simple server returns an URL that is used to load the authentication flow from the Nordic API Gateway.

The server implements the following endpoints:

### init ([documentation](https://api.nordicapigateway.com/docs/index.html#operation/Initialize))

```
curl -X POST \
  http://localhost:3000/init \
  -H 'Content-Type: application/json' \
  -d '{"redirectUrl": <REDIRECT_URL>}'
```

### tokens ([documentation](https://api.nordicapigateway.com/docs/index.html#operation/Tokens))

```
curl -X POST \
  http://localhost:3000/tokens \
  -H 'Content-Type: application/json' \
  -d '{"code": <CODE>}'
```

### accounts ([documentation](https://api.nordicapigateway.com/docs/index.html#operation/GetAccountsV2))

```
curl -X POST \
  http://localhost:3000/accounts \
  -H 'Content-Type: application/json' \
  -d '{
	"token": <ACCESS_TOKEN>
}'
```

### create-payment ([documentation](https://docs.nordicapigateway.com/#/payment-initiation/payments/))

```
curl -X POST \
  http://localhost:3000/create-payment \
  -H 'Content-Type: application/json' \
  -d '{
        "redirectUrl": <REDIRECT_URL>,
        "language": <LANGUAGE>,
        "userHash": "1234567890",
        "request": {
            "message": "Message for the receiver of the payment",
            "transactionText": "Test payment",
            "destination": {
                "iban": {
                    "ibanNumber": "FI000001"
                }
            },
            "amount": {
                "value": "1.00"
                "currency": "DKK"
            },
            "execution": {
                "type": "Instant",
            }
        }
      }'
```

### Setup Xcode project

1. Open Xcode and select the project file
2. Select 'QuickSprout' under 'Targets' and select 'Build Settings'
3. Set `NAG_API_URL` found under 'User Defined' to match your configuration
4. Start the application

### Setup Android project

1. Open Android Studio and select 'Open project' and browse the project folder
2. Make sure that you have configured network settings for the application, add unsecure connections to `/app/src/main/res/xml/network_security_config.xml`
3. Start the application

## License

MIT License

Copyright (c) 2019 Spiir A/S

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
