INSERT INTO users (email, password, full_name, tokens, role) VALUES
    ('user1@example.com', 'hashedpassword1', 'John Doe', 5, 'USER'),
    ('user2@example.com', 'hashedpassword2', 'Jane Smith', 10, 'USER');

INSERT INTO genres (name) VALUES
    ('Fiction'),
    ('Dystopian');

INSERT INTO books (title, author, release_year, condition, description, slug, format, image, donor_id, receiver_id)
VALUES 
    ('The Great Gatsby', 'F. Scott Fitzgerald', 1925, 'Good', 'A classic novel set in the Roaring Twenties.', 'the-great-gatsby', 'Hardcover', NULL, 1, NULL),
    ('1984', 'George Orwell', 1949, 'Like New', 'A dystopian novel about totalitarianism.', '1984', 'Paperback', NULL, 2, NULL);

INSERT INTO books_genres (book_id, genre_id) VALUES
    (1, 1),
    (2, 2);
