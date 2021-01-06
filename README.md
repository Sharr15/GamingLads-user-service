# GamingLads-user-service
This service takes care of the authentication and authorization.

## api endpoints
##### post jwt to validated user
````http request
POST/user/signIn/user
````
Returns a responseEntity with status OK and a valid jwt if succeeded or CONFLICT if failed

##### post jwt to validated admin
````http request
POST/user/signIn/admin
````
Returns a responseEntity with status OK and a valid jwt if succeeded or CONFLICT if failed
This is a work in progress 

##### post user credentials to database and send request to profile service
````http request
POST/user/signUp
````
Returns a responseEntity with status OK if succeeded or CONFLICT if failed
