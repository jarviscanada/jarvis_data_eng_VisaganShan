# Introduction

# SQL Queries

###### Table Setup (DDL)

```sql
CREATE TABLE cd.members
  (
     memid         INTEGER NOT NULL,
     surname       VARCHAR(200) NOT NULL,
     firstname     VARCHAR(200) NOT NULL,
     address       VARCHAR(300) NOT NULL,
     zipcode       INTEGER NOT NULL,
     telephone     VARCHAR(20) NOT NULL,
     recommendedby INTEGER,
     joindate      TIMESTAMP NOT NULL,
     CONSTRAINT members_pk PRIMARY KEY (memid),
     CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES
     cd.members(memid) ON DELETE SET NULL
  ); 
```

```sql
CREATE TABLE cd.facilities
  (
     facid              INTEGER NOT NULL,
     NAME               VARCHAR(100) NOT NULL,
     membercost         NUMERIC NOT NULL,
     guestcost          NUMERIC NOT NULL,
     initialoutlay      NUMERIC NOT NULL,
     monthlymaintenance NUMERIC NOT NULL,
     CONSTRAINT facilities_pk PRIMARY KEY (facid)
  ); 
```

```sql
CREATE TABLE cd.bookings
  (
     bookid    INTEGER NOT NULL,
     facid     INTEGER NOT NULL,
     memid     INTEGER NOT NULL,
     starttime TIMESTAMP NOT NULL,
     slots     INTEGER NOT NULL,
     CONSTRAINT bookings_pk PRIMARY KEY (bookid),
     CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES
     cd.facilities(facid),
     CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES
     cd.members(memid)
  ); 
```
###### Question 1: Show all members 

```sql
SELECT * FROM cd.members
```

###### Questions 2: Lorem ipsum...

```sql
SELECT blah blah blah
```

