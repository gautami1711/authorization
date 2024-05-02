# Authorization App
## Implementing role based access to APIs



This project is to demonstate the role based access of Rest Apis using Jwt as mechanism to secure the endpoints.



## Features

- Open apis to register and login.
- Register api to add a user and login to get token which will be used to validate the endpoints that require token
- Two Types of roles present- ADMIN and USER for now(can be customized further based on needs)
- Admin has access to all endpoints, while user has accees to the endpoints that concern the respective user only.

## Endpoints

# Accessible to all
- /user/register- register a user to the app
- /auth/login- to get the token to access the apis

# Accessible only to ADMIN user
- /role/create - to change the type of roles that can be offered by the application
- /user/id/{id}- get the user by id
- /user- to get all the users


# Accessible by both ADMIN and USER user
- /user/update/{id}-  to update a user details except for it's role.(can introduce this if required since currently we don't know the functionality or the type of application this can be used in we can skip this )
- /user/delete/id/{id}- to delete a user(a user with "USER" role can only delete his own account but a user with "ADMIN" can delete anyone)

# Other Endpoints
- /user/createadmin- requires a secret key to create a user with admin access. used in scernio of the first user of the app, so that we have an admin to hit other endpoints


## Requirements to run the Application
- `_Java17_` installed in the system
- Ide of choice(intellij, eclipse)
- `_Mysql_` installed in the system
- `_MySql workbench_` installed in the local system to have visual access to the data we are storing and fetching
- change the path in `_logback.xml_` to the desired location for saving logs in the local system
- key provided in the app resources
- `_lombok_` installation to process the lombok annotation used.

## Installation Instructions
- `_clone_` the repo in the IDE of choice(pref- intellij)
- change the mysql database name(or create a db with the name already in the yml)
- update the `_username_` and `_password_` for mysql connection on local system
- the repo contains the jar of the latest code, just `_run_` the application and we can access the endpoints now.



