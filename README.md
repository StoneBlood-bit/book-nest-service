# Book Nest service

## API

### Auth
- **registration** POST ``/auth/registration``
````
Request:
{
    "email": "alice@example.com",
    "password": "password",
    "fullName": "Alice"
}
````
````
Response: 
{
    "id": 5,
    "email": "alice@example.com",
    "fullName": "Alice"
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
### Favorites
- **add a book to favorites** POST ``/favorites``
````
Request:
{
    "bookId": 9
}
````
``The response contains 200 Status OK``
- **remove a book from favorites** DELETE ``/favorites/{bookId}`` 
````
The response contains 200 Status OK
````
- **get all books from favorites** GET ``/favorites``
````
Response:
[
    {
        "id": 10,
        "title": "updated title",
        "author": "updated author2",
        "condition": "Like New",
        "description": "Updated description",
        "slug": "updated-author2-updated-title-10",
        "format": "Hardcover",
        "genres": [
            "demo genre",
            "demo genre2"
        ],
        "releaseYear": 2015
    },
    {
        "id": 11,
        "title": "The Best Of Me",
        "author": "Nicholas SparKs",
        "condition": "Like New",
        "description": null,
        "slug": "nicholas-sparks-the-best-of-me-11",
        "format": null,
        "genres": [
            "Novella",
            "Fictions"
        ],
        "releaseYear": 0
    }
]
````
### Donated
- **get all donated books** GET ``/donated``
````
Response:
[
    {
        "id": 15,
        "title": "The Best Of Me",
        "author": "Nicholas SparKs",
        "condition": "Like New",
        "description": null,
        "slug": "nicholas-sparks-the-best-of-me-15",
        "format": null,
        "genres": [
            "Fictions",
            "Novella"
        ],
        "releaseYear": 0
    },
    {
        "id": 16,
        "title": "To test about donor",
        "author": "Nicholas SparKs",
        "condition": "Like New",
        "description": null,
        "slug": "nicholas-sparks-to-test-about-donor-16",
        "format": null,
        "genres": [
            "Fictions",
            "Novella"
        ],
        "releaseYear": 0
    }
]
````
### Received
- **get all received books** GET ``/received``
````
Response:
[]
````
### Titles
- **Get all book titles** GET ``/books/titles``
````
Response:
[
    "The Best Of Me",
    "The Best Of Me1",
    "The Best Of Me2",
    "The Best Of Me3"
]
````
### Shopping Cart
- **get shopping cart** GET ``/shopping-carts``
````
Response:
{
    "id": 6,
    "userId": 12,
    "books": [
        {
            "id": 15,
            "title": "The Best Of Me",
            "author": "Nicholas SparKs",
            "condition": "Like New",
            "description": null,
            "slug": "nicholas-sparks-the-best-of-me-15",
            "format": null,
            "genres": [
                "Novella",
                "Fictions"
            ],
            "releaseYear": 0
        }
    ]
}
````
- **add book to shopping cart** POST ``/shopping-carts/books/{bookId}`` \
``Response: 200 OK (Status)``
- **remove book from shopping cart** DELETE ``/shopping-carts/books/remove/{bookId}`` \
  ``Response: 200 OK (Status)``
### Order
- **create order** POST ``/orders``
````
Response:
{
    "id": 1,
    "userEmail": "will@example.com",
    "bookTitles": [
        "The Best Of Me"
    ],
    "orderStatus": "NEW",
    "createdAt": "2025-03-13T19:13:07.4736746"
}
````