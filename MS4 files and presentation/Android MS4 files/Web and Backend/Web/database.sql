FLUSH PRIVILEGES;
ALTER USER "root"@"localhost" IDENTIFIED BY "password";
Create database my_database;
Use my_database;

Create table Users(
    email varchar(100),
    pass varchar(50),
    contactName varchar(30),
    contactNo varchar(15),
    emergencypass varchar(4),
    callpolice int NOT NULL DEFAULT 0,
    sendlocation int NOT NULL DEFAULT 0,
    alarm int NOT NULL DEFAULT 0,
    record int NOT NULL DEFAULT 0,
    Primary Key(email)
);

Insert into Users values
    ("faridayousry@aucegypt.edu", "Farida","Khaled","+201211063345", "1234" , 0, 1, 0, 0);
