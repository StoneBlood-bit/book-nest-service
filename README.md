# Book Nest service

## API

### Auth
- **registration** POST ``/auth/registration``
````
Request:
{
    "email": "alice@example.com",
    "password": "password",
    "firstName": "Alice",
    "lastName": "Snow"
}
````
````
Response: 
{
    "id": 5,
    "email": "alice@example.com",
    "firstName": "Alice",
    "lastName": "Snow"
}
````
- **login** POST ``/auth/login``
````
Request:
{
    "email": "alice@example.com",
    "password": "password"
}
````
````
Response:
{
    "token": "value"
}
````

### OAuth
- **google** GET ``/auth/callback/google?code=``
````
Response:
{
    "token": "value"
}
````
- **facebook** GET ``/auth/callback/facebook?code=``
````
Response:
{
    "token": "value"
}
````

### User
- **get my profile** GET ``/users/me``
````
Response:
{
    "id": 5,
    "email": "alice@example.com",
    "firstName": "Alice",
    "lastName": "Snow",
    "tokens": 0
}
````
### Genre
- **add a new genre** POST ``/genres``
````
Request:
{
    "name": "Fictions"
}
````
````
Response:
{
    "id": 2,
    "name": "Fictions"
}
````
- **get a genre by id** GET ``/genres/{id}``
````
Response:
{
    "id": 2,
    "name": "Fictions"
}
````
- **get all genres** GET ``/genres``
````
Response:
{
    "content": [
        {
            "id": 2,
            "name": "Fictions"
        },
        {
            "id": 3,
            "name": "Young readers"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 12,
        "sort": {
            "sorted": false,
            "empty": true,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "first": true,
    "size": 12,
    "number": 0,
    "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
    },
    "numberOfElements": 2,
    "empty": false
}
````
- **delete a genre by id** DELETE ``/genres/{id}``

### Book
- **add a new book** POST ``/books``
````
Request:
{
    "title": "The Best Of Me",
    "author": "Nicholas SparKs",
    "genreIds": [4, 5]
    "condition": "Like New",
    "description": "book for a tests"
}
````
````
Response:
{
    "id": 16,
    "title": "The Best Of Me",
    "author": "Nicholas SparKs",
    "condition": "Like New",
    "genres": [
        "Novella",
        "Fictions"
    ],
    "format": "Hardcover"
    "slug": "nicholas-sparks-the-best-of-me-16",
    "releaseYear": 0
}
````
- **get a book by id** GET ``/books/{id}``
````
Response:
{
    "id": 16,
    "title": "The Best Of Me",
    "author": "Nicholas SparKs",
    "condition": "Like New",
    "genres": ["Fictions"],
    "format": "Hardcover"
    "slug": "nicholas-sparks-the-best-of-me-16",
    "releaseYear": 0
}
````
- **get all books** GET ``/books?genre=Fictions&condition=Like New&format=Hardcover&sort=title:asc``
````
Response:
{
    "content": [
        {
            "id": 16,
            "title": "The Best Of Me",
            "author": "Nicholas SparKs",
            "condition": "Like New",
            "genres": [
                "Novella",
                "Fictions"
            ],
            "format": "Hardcover"
            "slug": "nicholas-sparks-the-best-of-me-16",
            "releaseYear": 0
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 12,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 9,
    "size": 12,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 9,
    "first": true,
    "empty": false
}
````
- **delete book by id** DELETE ``/book/{id}``

- **update a book** PUT ``/book/{id}``
````
Request: (form-data)

title: The Best Of Me
author: Nicholas Sparks
releaseYear: 2011
condition: Like New
description: new description
file: image.png
genreIds: 2, 3
format: Hardcover
````
````
Response:
{
    "id": 1,
    "title": "The Best Of Me",
    "author": "Nicholas Sparks",
    "releaseYear": 2011,
    "condition": "Like New",
    "description": "New description",
    "image": "iVBORw0KGgoIApL152CVJVSf8O..." //Base64
    "format": "Hardcover"
    "genres": [
        "Novella",
        "Fictions"
    ]
    "slug": "nicholas-sparks-the-best-of-me-1"
}
````
### Image
- **get image by book`s id** GET ``/books/image/{booksId}``
````
The response contains an image in png format
````
