# WSDL Java Sample Project

## Architecture

### Production Setup

```
+-------------------+         +--------------------------+         +-------------------+
|                   |         |                          |         |                   |
|  WSDL SOAP Client |         |  Product BFF Search      |         |  order_api        |
|                   |         |  Service (BFF)           |         |  (WSDL Service)   |
+---------+---------+         +-----------+--------------+         +---------+---------+
          |                               |                                  |
          | 1. Request (SOAP)             |                                  |
          +------------------------------>|                                  |
          |                               |                                  |
          |                               | 2. Request (SOAP)                |
          |                               +--------------------------------->|
          |                               |                                  |
          |                               |                3. Response (SOAP)|
          |                               |<---------------------------------+
          | 4. Response (SOAP)            |                                  |
          +<------------------------------+                                  |
```

### Contract Test Setup

```
+-------------------------+         +--------------------------+         +----------------------+
|                         |         |                          |         |                      |
| Specmatic Contract Test |         |  Product BFF Search      |         |  Specmatic Mock      |
|   (WSDL of BFF)         |         |  Service (BFF)           |         |  (WSDL of order_api) |
+-----------+-------------+         +-----------+--------------+         +----------+-----------+
            |                                   |                                   |
            | 1. Request (SOAP)                 |                                   |
            +---------------------------------->|                                   |
            |                                   |                                   |
            |                                   | 2. Request (SOAP)                 |
            |                                   +---------------------------------->|
            |                                   |                                   |
            |                                   |                3. Response (SOAP) |
            |                                   |<----------------------------------+
            | 4. Response (SOAP)                |                                   |
            +<----------------------------------+                                   |
            |                          
            |--------------------+
            | 5. Assert response |
            | against WSDL spec  |
            +<-------------------+
```

## Running Contract Test and Mock Programmatically

Please refer to [ContractTest.kt](src/test/kotlin/com/component/orders/ContractTest.kt)

Once you run the test the report should be available at `build/reports/specmatic/html/index.html`.

## Running the Specmatic WSDL Mock Server from Command Line using Docker
1. On Unix and PowerShell terminals:
```shell
docker run --rm --network host -v "$(pwd):/usr/src/app" specmatic/specmatic mock
```

2. On Windows CMD Prompt:
```shell
docker run --rm --network host  -v "%cd%:/usr/src/app" specmatic/specmatic mock
```

Now we can verify if the stub server is running
1. Open SOAPUI
2. Import the wsdl in `wsdls/order_api.wsdl`
3. Remember to set the endpoint URL to host `localhost` and port `9000`
4. Send the following request using SOAPUI:

```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns0:CreateOrder xmlns:ns0="http://www.example.com/orders">
            <productid>123</productid>
            <count>1</count>
        </ns0:CreateOrder>
    </S:Body>
</S:Envelope>
```

And you should get a response like this:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <OrderId>
            <id>10</id>
        </OrderId>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

As you can notice, id `10` is returned only when the `productid` is `123` and `count` is `1`. This response is based on example file [`create_order.json`](wsdls/order_api_examples/create_order.json).
Please refer to documentation for format of the [example file](https://docs.specmatic.io/documentation/soap.html#examples-as-mock-data).

You can also run the same WSDL spec as a test against the mock server.

### Start the bff app
```shell
./gradlew bootRun
```

1. On Unix and PowerShell terminals:
```shell
docker run --rm --network host -v "$(pwd):/usr/src/app" specmatic/specmatic test
```

2. On Windows CMD Prompt:
```shell
docker run --rm --network host -v "%cd%:/usr/src/app" specmatic/specmatic test
```

The HTML API Coverage Report will be available at `build/reports/specmatic/html/index.html`.

