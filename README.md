# ProgrammingProject  

Backend api endpoints
-----------------------------------------
*address = http://34.70.170.35:8080/  
*service = test  

replace text with : infront of it with values e.g. "/:username/" would be "/andrew/"  

**Login process**  
first send the username and password for authenticating:  
>GET--address/service/login/:userName/:password  
  
this will return a string to be used as an Identifier in all user related services where ":userId" is  
  
Then to log out by calling:  
>GET--address/service/logout/:userId
  
**Regester process**  
send the username and password to check if the user exists:  
>POST--address/service/regester/:userName/:password  
  
this will not log the user in although, login api will still need to run  
  
**user functions**  
gets a list of all shares  
>GET--address/service/top/:userId  
response: jsonlist  
[  
    {  
        "symbol": text,  
        "company": text,  
        "price": text(parsable to double),  
        "uAmount": integer(amount of this stock user ownes)  
    },...  
]  
  
gets the current user balance  
>GET--address/service/userCash/:userId  
response : double (user balance) 
  
Runs a Stock purchase for user  
>POST--address/service/userPurchase/:userId  
request: json  
{  
	"sym":text (symbol of stock to buy),  
	"amount":integer (amount to buy)  
}  
returns 200 if success  
  
Runs a stock sale for the user
>POST--address/service/userSellShare/:userid  
request: json  
{  
	"sym":text (symbol of stock to sell),  
	"amount":integer (amount to sell)  
}  
returns 200 if success  
  
gets all the sales and purchases the user has conducted
>GET--address/service/userTransactionHistory/:userId  
result: jsonarray  
[  
    {  
        "symbol": text (Symbol of stock bought or sold),  
        "type": text (Either SELL or PURCHASE),  
        "amount": integer (amount of stock transferred),  
        "value": double (value of transaction),  
        "date/time": text (date and time of transaction formatted "yyyy-MM-dd hh:mm:ss")  
    },...  
]  
  
Local Installation guide
-----------------------------------------
Run sparkjava.jar found in \sparkjava\out\artifacts\sparkjava_jar for the following API's to work  
prompt command : "java -jar sparkjava.jar" 
  
*address = http://localhost:8080/  
*service = test  
  
GitHub : https://github.com/s3656260/ProgrammingProject  
  
Live site link https://take-stock-258303.appspot.com/#/  
*note it may have issues with some scripts, if prompted allow unsecure scripts    
