CREATE TABLE authors (
 id int(11) unsigned NOT NULL AUTO_INCREMENT,
 name varchar(255) NOT NULL,
 birthplace varchar(255) NOT NULL,
 description varchar(5500) NOT NULL,
 PRIMARY KEY (id)
);

CREATE TABLE books (
 id int(11) unsigned NOT NULL AUTO_INCREMENT,
 title varchar(255) NOT NULL,
 author int(11) NOT NULL,
 description varchar(5000) NOT NULL,
 number_read int(11) NOT NULL DEFAULT '0',
 number_opened int(11) NOT NULL DEFAULT '0',
 PRIMARY KEY (id)
);