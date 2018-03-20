SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS Cards;
DROP TABLE IF EXISTS files;
DROP TABLE IF EXISTS Door_Ctrl_Logs;
DROP TABLE IF EXISTS Members;
DROP TABLE IF EXISTS Admin;



/* Create Tables */

CREATE TABLE Cards
(
	UUID varchar(100) NOT NULL,
	PRIMARY KEY (UUID)
);


CREATE TABLE Door_Ctrl_Logs
(
	ctrl_time timestamp DEFAULT NOW() NOT NULL,
	id varchar(25) NOT NULL,
	PRIMARY KEY (ctrl_time)
);


CREATE TABLE files
(
	file_name varchar(50) NOT NULL,
	file_size int NOT NULL,
	path varchar(50) NOT NULL,
	upload_time timestamp DEFAULT NOW() NOT NULL,
	ctrl_time timestamp DEFAULT NOW() NOT NULL,
	PRIMARY KEY (file_name)
);


CREATE TABLE Members
(
	id varchar(25) NOT NULL,
	passwd varchar(255) NOT NULL,
	name varchar(25),
	PRIMARY KEY (id)
);



/* Create Foreign Keys */

ALTER TABLE files
	ADD FOREIGN KEY (ctrl_time)
	REFERENCES Door_Ctrl_Logs (ctrl_time)
	ON UPDATE CASCADE
	ON DELETE CASCADE
;


ALTER TABLE Door_Ctrl_Logs
	ADD FOREIGN KEY (id)
	REFERENCES Members (id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
;



