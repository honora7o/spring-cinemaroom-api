# spring-cinemaroom-api
Basic cinema room RESTful API made using SpringBoot, SOLID principles and layered architecture.

# Usage

Has different endpoints to request and retrieve information about a standard cinema room. 

Uses JDBC, H2, SpringBoot and its repositories tools for all its database and REST related needs.

# Examples

<details>
<summary>GET: /seats</summary>

Sends request for available seats in the cinema room in the following format (prices are set to 10 for every seat that is of row <= 4 and 8 to all others):

Response body (Http status: 200):
```json
{
    "total_rows": 9,
    "total_columns": 9,
    "available_seats": [
        {
            "row": 1,
            "column": 2,
            "price": 10
        },
        {
            "row": 1,
            "column": 3,
            "price": 10
        },
        {
            "row": 1,
            "column": 5,
            "price": 10
        },
        {
            "row": 1,
            "column": 8,
            "price": 10
        },
        {
            "row": 1,
            "column": 9,
            "price": 10
        },
        {
            "row": 2,
            "column": 1,
            "price": 10
        },
        {
            "row": 2,
            "column": 3,
            "price": 10
        },
        {
            "row": 2,
            "column": 4,
            "price": 10
        },
        {
            "row": 2,
            "column": 6,
            "price": 10
        },
        {
            "row": 2,
            "column": 8,
            "price": 10
        },
        {
            "row": 2,
            "column": 9,
            "price": 10
        },
        {
            "row": 3,
            "column": 1,
            "price": 10
        },
        {
            "row": 3,
            "column": 2,
            "price": 10
        },
        {
            "row": 3,
            "column": 3,
            "price": 10
        },
        {
            "row": 3,
            "column": 4,
            "price": 10
        },
        {
            "row": 3,
            "column": 6,
            "price": 10
        },
        {
            "row": 3,
            "column": 7,
            "price": 10
        },
        {
            "row": 3,
            "column": 8,
            "price": 10
        },
        {
            "row": 3,
            "column": 9,
            "price": 10
        },
        {
            "row": 4,
            "column": 1,
            "price": 10
        },
        {
            "row": 4,
            "column": 2,
            "price": 10
        },
        {
            "row": 4,
            "column": 3,
            "price": 10
        },
        {
            "row": 4,
            "column": 6,
            "price": 10
        },
        {
            "row": 4,
            "column": 7,
            "price": 10
        },
        {
            "row": 4,
            "column": 8,
            "price": 10
        },
        {
            "row": 4,
            "column": 9,
            "price": 10
        },
        {
            "row": 5,
            "column": 1,
            "price": 8
        },
        {
            "row": 5,
            "column": 2,
            "price": 8
        },
        {
            "row": 5,
            "column": 3,
            "price": 8
        },
        {
            "row": 5,
            "column": 4,
            "price": 8
        },
        {
            "row": 5,
            "column": 6,
            "price": 8
        },
        {
            "row": 5,
            "column": 8,
            "price": 8
        },
        {
            "row": 5,
            "column": 9,
            "price": 8
        },
        {
            "row": 6,
            "column": 1,
            "price": 8
        },
        {
            "row": 6,
            "column": 2,
            "price": 8
        },
        {
            "row": 6,
            "column": 3,
            "price": 8
        },
        {
            "row": 6,
            "column": 4,
            "price": 8
        },
        {
            "row": 6,
            "column": 6,
            "price": 8
        },
        {
            "row": 6,
            "column": 7,
            "price": 8
        },
        {
            "row": 6,
            "column": 8,
            "price": 8
        },
        {
            "row": 6,
            "column": 9,
            "price": 8
        },
        {
            "row": 7,
            "column": 1,
            "price": 8
        },
        {
            "row": 8,
            "column": 1,
            "price": 8
        },
        {
            "row": 8,
            "column": 2,
            "price": 8
        },
        {
            "row": 8,
            "column": 3,
            "price": 8
        },
        {
            "row": 8,
            "column": 4,
            "price": 8
        },
        {
            "row": 8,
            "column": 5,
            "price": 8
        },
        {
            "row": 8,
            "column": 8,
            "price": 8
        },
        {
            "row": 9,
            "column": 6,
            "price": 8
        },
        {
            "row": 9,
            "column": 7,
            "price": 8
        },
        {
            "row": 9,
            "column": 8,
            "price": 8
        }
    ]
}
```  
  
</details>

<details>
<summary>POST: /purchase</summary>

POST a Json body to send a purchase request for a specific seat.
Will check for seat validity and return appropriate HTTP Status.
If seat is valid, will return the bought ticket with a randomly generated token that can be used for refunding it and remove it from available seats to be purchased.

Request body:
```json
{
    "row": 4,
    "column": 1
}
```

Response body (Http status: 200):
```json
{
    "token": "ddef8209-798f-4232-80fa-9131f43c0718",
    "ticket": {
        "row": 4,
        "column": 1,
        "price": 10
    }
}
```

Response body when ticket has already been purchased or seat position is invalid (Http status: 400):
```json
{
    "error": "The ticket has been already purchased!"
}
```

```json
{
    "error": "The number of a row or a column is out of bounds!"
}
```  
  
</details>

<details>
<summary>POST: /return</summary>

POST a Json body to send a refund request for a specific seat assigned by a token set on the previous purchase.
Will check for token validity and return appropriate HTTP Status.
If token is valid and assigned to a seat, will refund that seat and make it available for purchase again.

Request body:
```json
{
    "token": "ddef8209-798f-4232-80fa-9131f43c0718"
}
```

Response body (Http status: 200):
```json
{
    "returned_ticket": {
        "row": 4,
        "column": 1,
        "price": 10
    }
}
```

Response body when token is invalid (Http status: 400):
```json
{
    "error": "Wrong token!"
}
```
  
</details>

<details>
<summary>GET: /stats?password=password</summary>

Send request with response locked behind password (sent through parameters) and checks for password validity.
If password is valid, returns response with revenue in the below format:

Response body when password is valid (Http status: 200):
```json
{
    "current_income": 260,
    "number_of_available_seats": 51,
    "number_of_purchased_tickets": 30
}
```
Response body when password is invalid (Http status: 401):
```json
{
    "error": "The password is wrong!"
}
```
  
</details>
