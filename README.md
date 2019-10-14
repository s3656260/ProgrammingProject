# ProgrammingProject

Backend api endpoints
-----------------------------------------
Run the java program found in "ProgrammingProject/sparkjava/src/main/java/app.java" for the following API's to work  

*address = http://localhost:4567/  
*service = test  

replace text with : infront of it with values e.g. "/:username/" would be "/tom/"  

**Login process**  
first send the username and password for authenticating:  
>GET--address/service/login/:userName/:password  
  
this will return a string to be used as an Identifier in all user related services where ":userId" is  
  
Then to log out by calling:  
>GET--address/service/login/:userId
  
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
  
>POST--address/service/userSellShare/:userid  
request: json  
{  
	"sym":text (symbol of stock to sell),  
	"amount":integer (amount to sell)  
}  
returns 200 if success  
  
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
