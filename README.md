# WSDL Java Sample Project

## Stub out the downstream dependency

```shell
docker run -v "$(pwd)/wsdls:/wsdls" -v "$(pwd)/wsdls/order_api_examples:/order_api_examples" -p 8090:9000 specmatic/specmatic stub "/wsdls/order_api.wsdl"
```

OR

```shell
java -jar specmatic.jar stub --port 8090 ./wsdls/order_api.wsdl
```

## Hit the downstream dependency stub from SOAPUI

1. Open SOAPUI
2. Import the wsdl in `wsdls/order_api.wsdl`
3. Use the following request from SOAPUI:

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

## Start the downstream dependency

```shell
./gradlew clean bootRun
```

## Hit the system under test stub from SOAPUI

1. Open SOAPUI
2. Import the wsdl in `wsdls/product_bff_search.wsdl`
3. Use the following request from SOAPUI:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://www.example.com/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:OrderRequest>
         <productid>123</productid>
         <count>1</count>
      </ord:OrderRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
