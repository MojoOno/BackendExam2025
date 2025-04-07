The following README is written by me, Frederik Michael Franck, but have been through chatGPT to make it look nice and easy on the eye for the reader.

This is the prompt i gave it;
```
"Jeg har skrevet denne README.md selv, og jeg vil gerne beholde ord for ord hvad der står deri.
Det jeg gerne vil have dig til er at formatere det, så det ser lækkert ud når man som læser ser den.
Du må altså ikke ændre i det jeg har skrevet, eller tilføje noget nyt - tak!"
```
# Setup

> Remember to add a `config.properties` file in the `resources` directory.  
> The `config.properties` file should contain the following properties:

```
DB_NAME  
DB_USERNAME=postgres  
DB_PASSWORD  
SECRET_KEY=minimum32characterslong  
ISSUER=  
TOKEN_EXPIRE_TIME=3600000
```

> The `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `ISSUER`, and `TOKEN_EXPIRE_TIME` properties should be filled in with the appropriate values.  
> The `SECRET_KEY` property should be a minimum of 32 characters long.

---

# Task 2: JPA and DAOS

## 2.4.2 - Generics

The `GenericDAO` class is a generic interface that provides basic CRUD operations for any entity type. It uses Java Generics to allow for type-safe operations on different entity classes.  
The `GenericDAO` implements from the `CrudDAO` interface, which defines the basic CRUD operations.

---

# Task 3: API Endpoints

The API endpoints are defined in the `routes` package.  
The main entry point is the `Main` class, which sets up the server and initializes the routes.

Below is the output of the manual endpoint tests in the `demo.http` file.

---

## 3.3.2 - OUTPUT

### `GET http://localhost:7070/api/skilessons`

```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 09:47:21
Content-Type: application/json
Content-Encoding: gzip
Content-Length: 506
```

<details>
<summary>Click to view response</summary>

```json
[
  {
    "id": 1,
    "name": "Beyond the Wall",
    "price": 1500.0,
    "level": "advanced",
    "startTime": [
      2025,
      4,
      7
    ],
    "endTime": [
      2025,
      4,
      9
    ],
    "longitude": "-75.1234",
    "latitude": "60.5678",
    "instructor": {
      "id": 1,
      "firstName": "Jon",
      "lastName": "Snow",
      "email": "jon.snow@thewall.com",
      "phone": 11112222,
      "yearsOfExperience": 5.0
    }
  },
  {
    "id": 2,
    "name": "Night Watch Basics",
    "price": 800.0,
    "level": "beginner",
    "startTime": [
      2025,
      4,
      10
    ],
    "endTime": [
      2025,
      4,
      11
    ],
    "longitude": "-75.1250",
    "latitude": "60.5699",
    "instructor": {
      "id": 1,
      "firstName": "Jon",
      "lastName": "Snow",
      "email": "jon.snow@thewall.com",
      "phone": 11112222,
      "yearsOfExperience": 5.0
    }
  },
  {
    "id": 3,
    "name": "Assassin Agility",
    "price": 1200.0,
    "level": "intermediate",
    "startTime": [
      2025,
      4,
      2
    ],
    "endTime": [
      2025,
      4,
      3
    ],
    "longitude": "12.3456",
    "latitude": "55.1234",
    "instructor": {
      "id": 2,
      "firstName": "Arya",
      "lastName": "Stark",
      "email": "needle.master@winterfell.com",
      "phone": 22223333,
      "yearsOfExperience": 3.0
    }
  },
  {
    "id": 4,
    "name": "Silent Steps",
    "price": 1000.0,
    "level": "beginner",
    "startTime": [
      2025,
      3,
      28
    ],
    "endTime": [
      2025,
      3,
      29
    ],
    "longitude": "12.3466",
    "latitude": "55.1244",
    "instructor": {
      "id": 2,
      "firstName": "Arya",
      "lastName": "Stark",
      "email": "needle.master@winterfell.com",
      "phone": 22223333,
      "yearsOfExperience": 3.0
    }
  },
  {
    "id": 5,
    "name": "Wine & Wisdom",
    "price": 600.0,
    "level": "beginner",
    "startTime": [
      2025,
      4,
      14
    ],
    "endTime": [
      2025,
      4,
      15
    ],
    "longitude": "10.5678",
    "latitude": "50.1234",
    "instructor": {
      "id": 3,
      "firstName": "Tyrion",
      "lastName": "Lannister",
      "email": "hand@casterlyrock.com",
      "phone": 33334444,
      "yearsOfExperience": 2.0
    }
  },
  {
    "id": 6,
    "name": "Advanced Slope Politics",
    "price": 1300.0,
    "level": "advanced",
    "startTime": [
      2025,
      4,
      21
    ],
    "endTime": [
      2025,
      4,
      22
    ],
    "longitude": "10.5688",
    "latitude": "50.1244",
    "instructor": {
      "id": 3,
      "firstName": "Tyrion",
      "lastName": "Lannister",
      "email": "hand@casterlyrock.com",
      "phone": 33334444,
      "yearsOfExperience": 2.0
    }
  }
]
```

</details>

---

### `GET http://localhost:7070/api/skilessons/3`
```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 09:50:28
Content-Type: application/json
Content-Length: 299
```

```json
{
  "id": 3,
  "name": "Assassin Agility",
  "price": 1200.0,
  "level": "intermediate",
  "startTime": [
    2025,
    4,
    2
  ],
  "endTime": [
    2025,
    4,
    3
  ],
  "longitude": "12.3456",
  "latitude": "55.1234",
  "instructor": {
    "id": 2,
    "firstName": "Arya",
    "lastName": "Stark",
    "email": "needle.master@winterfell.com",
    "phone": 22223333,
    "yearsOfExperience": 3.0
  }
}
```

---

### `POST http://localhost:7070/api/skilessons`
```
HTTP/1.1 201 Created
Date: Mon, 07 Apr 2025 09:52:41
Content-Type: application/json
Content-Length: 179
```

```json
{
  "id": 7,
  "name": "Beginner Slope Basics",
  "price": 250.0,
  "level": "beginner",
  "startTime": [
    2023,
    12,
    1
  ],
  "endTime": [
    2023,
    12,
    2
  ],
  "longitude": "10.1234",
  "latitude": "56.7890",
  "instructor": null
}
```

---

### `PUT http://localhost:7070/api/skilessons/7`
```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 09:53:28
Content-Type: application/json
Content-Length: 186
```

```json
{
  "id": 7,
  "name": "Beginner Slope Basics (kids)",
  "price": 100.0,
  "level": "beginner",
  "startTime": [
    2023,
    12,
    1
  ],
  "endTime": [
    2023,
    12,
    2
  ],
  "longitude": "10.1234",
  "latitude": "56.7890",
  "instructor": null
}
```

---

### `DELETE http://localhost:7070/api/skilessons/7`

```
HTTP/1.1 204 No Content
Date: Mon, 07 Apr 2025 09:54:00
Content-Type: application/json
```
```
<Response body is empty>
```
---

### `PUT http://localhost:7070/api/skilessons/7/instructors/1`
```
Date: Mon, 07 Apr 2025 09:55:33
Content-Type: application/json
Content-Length: 869
```

```json
[
  {
    "id": 1,
    "name": "Beyond the Wall",
    "price": 1500.0,
    "level": "advanced",
    "startTime": [
      2025,
      4,
      7
    ],
    "endTime": [
      2025,
      4,
      9
    ],
    "longitude": "-75.1234",
    "latitude": "60.5678",
    "instructor": {
      "id": 1,
      "firstName": "Jon",
      "lastName": "Snow",
      "email": "jon.snow@thewall.com",
      "phone": 11112222,
      "yearsOfExperience": 5.0
    }
  },
  {
    "id": 2,
    "name": "Night Watch Basics",
    "price": 800.0,
    "level": "beginner",
    "startTime": [
      2025,
      4,
      10
    ],
    "endTime": [
      2025,
      4,
      11
    ],
    "longitude": "-75.1250",
    "latitude": "60.5699",
    "instructor": {
      "id": 1,
      "firstName": "Jon",
      "lastName": "Snow",
      "email": "jon.snow@thewall.com",
      "phone": 11112222,
      "yearsOfExperience": 5.0
    }
  },
  {
    "id": 7,
    "name": "Beginner Slope Basics",
    "price": 250.0,
    "level": "beginner",
    "startTime": [
      2023,
      12,
      1
    ],
    "endTime": [
      2023,
      12,
      2
    ],
    "longitude": "10.1234",
    "latitude": "56.7890",
    "instructor": {
      "id": 1,
      "firstName": "Jon",
      "lastName": "Snow",
      "email": "jon.snow@thewall.com",
      "phone": 11112222,
      "yearsOfExperience": 5.0
    }
  }
]
```

---
### `GET http://localhost:7070/api/skilessons/level?level=advanced`
```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 10:16:06
Content-Type: application/json
Content-Length: 322
```

```json
[
  {
    "id": 1,
    "name": "Beyond the Wall",
    "price": 1500.0,
    "level": "advanced",
    "startTime": [
      2025,
      4,
      7
    ],
    "endTime": [
      2025,
      4,
      9
    ],
    "longitude": "-75.1234",
    "latitude": "60.5678"
  },
  {
    "id": 6,
    "name": "Advanced Slope Politics",
    "price": 1300.0,
    "level": "advanced",
    "startTime": [
      2025,
      4,
      21
    ],
    "endTime": [
      2025,
      4,
      22
    ],
    "longitude": "10.5688",
    "latitude": "50.1244"
  }
]
```

---

### `POST http://localhost:7070/api/skilessons/populate`
```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 09:45:39
Content-Type: application/json
Content-Length: 32
```

```
Database populated successfully.
```
---
### `GET http://localhost:7070/api/skilessons/totalprice`
```
HTTP/1.1 200 OK
Date: Mon, 07 Apr 2025 08:24:20 GMT
Content-Type: application/json
Content-Length: 118
```

```json
[
  {
    "instructorId": 1,
    "totalPrice": 2300.0
  },
  {
    "instructorId": 2,
    "totalPrice": 2200.0
  },
  {
    "instructorId": 3,
    "totalPrice": 1900.0
  }
]
```

## 3.3.5 - Why use PUT?

The goal is to add a instructor to a skiLesson. When we add that instructor we don’t want to create a new skiLesson, and then add the instructor.  
What we want to do is to update an already existing skiLesson with an instructor. In order to do that we use the PUT request instead of the POST request.

---

#  TASK 8: Security

Added `ADMIN` privilege to `create`, `update`, and `delete` endpoints.  
Everything else has been set to `ANYONE` for the purpose of making testing my program easier with a limited amount of time given.

If this was a "real" application, further security would be added to the endpoints, but as I just want to show that security is implemented, I’ve only added the ADMIN to those three endpoints.

> ✅ To test this:
> 1. Run the `populate` endpoint to populate the DB with entities
> 2. Go to `demoSecurity.http` and run the `register` and `login` endpoints
> 3. Go back to `demo.http` and run the `create`, `update` and `delete` endpoints
> 4. If you are not logged in, they will return `Unauthorized` error message.