# User Guide for Contact Management System

## Start Application

You can start the application with the following command:

./mvnw spring-boot:run

Home URL : http://localhost:8080/index.html

h2-console : http://localhost:8080/h2-console

The embedded H2 database will be started along with the application.

## Users

Users can have two roles: *ADMIN* or *USER*.

### Initial Users

The initial admin user and user are added via the `data.sql` as mentioned below:

- **Admin Role:**
  - Username: admin
  - Password: admin
- **User Role:**
  - Username: user1
  - Password: user1

## Contacts

Two contacts have also been added initially through `data.sql`.

Once logged in, you can perform the following operations based on your role:

### Admin Role Operations:

1. **View All System Users:**
	GET http://localhost:8080/persons


2. **Add New System User with Role User:**
	You have the privilege to add a new user with the role "User". Send a POST request with the following details:
	POST http://localhost:8080/persons
	Request Body:
		{
		"name": "user2",
		"password": "user2"
		}


3. **CRUD Operations for Contacts:**
   As an admin, you have full access to perform CRUD operations on all contacts.

   - **Create Contact:**
     ```
     POST http://localhost:8080/contacts
     ```
     Request Body:
     ```json
     {
         "name": "user3",
         "address": "user2 address",
         "phoneNumber": "123"
     }
     ```

   - **Read All Contacts:**
     ```
     GET http://localhost:8080/contacts
     ```

   - **Update Contact by ID:**
     ```
     POST http://localhost:8080/contacts/update
     ```
     Request Body:
     ```json
     {
         "id": 1,
         "name": "John Doe",
         "address": "456 Oak St, Town, Country",
         "phoneNumber": "987-654-3210"
     }
     ```

   - **Delete Contact by ID:**
     ```
     DELETE http://localhost:8080/contacts/{id}
     ```

	
### User Role Operations:

1. **View All Contacts:**
   GET http://localhost:8080/contacts

	

2. **View a Particular Contact Based on ID:**
GET http://localhost:8080/contacts/{id}
	
# Swagger UI
The swagger ui can be accessed via the url <http://localhost:8080/swagger-ui.html>. 	

## Spring security configuration

The class `SecurityConfiguration` creates two configuration beans for Spring Security.

* `passwordEncoder()` will configure bcrypt with 12 rounds as password hash algorithm. You can also generate a hash 
  of a given password on <https://bcrypt-generator.com/>.
* `filterChain(HttpSecurity)` configures:
  * CSRF is disabled for easier access on this *learning* project.
  * Default HTTP Basic as authentication method.
  * Session management is stateless.
  * Frames for the same origin are allowed. This is needed to use the h2-console ui.
  * Allowing unauthenticated access to the swagger ui.
  * Allowing unauthenticated access to the h2 console.
  * Allowing unauthenticated GET-access to `/contacts` and `/contacts/*`.
  * Restricting POST-access to `/users` to the `ADMIN` role.
  * Restricting any other URL to an authenticated user.

The class `UserDetailsServiceImpl` will replace the default behavior of Spring Security with loading users from the
database. For a given username a `User` entity is loaded from the database and passed with the password hash and
the role as a Spring Security `User` object implementing the `UserDetails` interface.

