/*
  https://dba.stackexchange.com/questions/76788/create-a-database-with-charset-utf-8/76789
  https://mariadb.com/kb/en/library/unicode/
 */
CREATE DATABASE POCHETTE_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE POCHETTE_DB;

CREATE TABLE LINKS (
	idLink INTEGER PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	url VARCHAR(255) NOT NULL,
	creationDate DATE NOT NULL,
	consumed BOOLEAN NOT NULL,
	idType INTEGER NOT NULL
);

CREATE TABLE TYPES (
	idType INTEGER PRIMARY KEY AUTO_INCREMENT,
	label VARCHAR(255) NOT NULL
);
ALTER TABLE LINKS ADD CONSTRAINT FOREIGN KEY (idType) REFERENCES TYPES (idType);

INSERT INTO TYPES(label) VALUES('ARTICLE'),('DOCUMENTATION'),('MUSIQUE'),('VIDEO'),('MOOC');
INSERT INTO LINKS(title,url,creationDate,consumed,idType)
VALUES('Documentation OCaml','http://caml.inria.fr/pub/docs/manual-ocaml/',NOW(),FALSE, 2),
('Detecting use case of GADT with OCaml', 'http://mads-hartmann.com/ocaml/2015/01/05/gadt-ocaml.html',NOW(),FALSE, 1),
('JJC De Mondoville Dominus Regnavit Mov. 4&5/6','https://www.youtube.com/watch?v=xm1_HdBWmL8',NOW(),FALSE, 4),
('Shahmen Poison','https://soundcloud.com/blessxshahmen/poison-2',NOW(),FALSE, 3),
('OCaml Mooc','https://www.fun-mooc.fr/courses/course-v1:parisdiderot+56002+session03/about',NOW(),FALSE, 2);
