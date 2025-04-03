insert into users (id, email, password, full_name, tokens, role, is_deleted)
values (1, 'customer', '1234', 'Bob', 9, 'CUSTOMER', 0);

insert into books (id, title, author, release_year, "condition", is_deleted, donor_id)
values (1, 'title', 'author', 2000, 'condition', 0, 1);