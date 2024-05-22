# Introduction

# SQL Queries

### Table Setup (DDL)

#### Members Table
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
#### Facilities Table
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

#### Bookings Table
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

### SQL Questions
#### Modifying Data
###### Question 1: https://pgexercises.com/questions/updates/insert.html
```sql
INSERT INTO cd.facilities
            (facid,
             NAME,
             membercost,
             guestcost,
             initialoutlay,
             monthlymaintenance)
VALUES      (9,
             'Spa',
             20,
             30,
             100000,
             800) 
```

###### Questions 2: https://pgexercises.com/questions/updates/insert3.html
```sql
INSERT INTO cd.facilities
            (facid,
             NAME,
             membercost,
             guestcost,
             initialoutlay,
             monthlymaintenance)
SELECT (SELECT Max(facid)
        FROM   cd.facilities)
       + 1,
       'Spa',
       20,
       30,
       100000,
       800; 
```

###### Questions 3: https://pgexercises.com/questions/updates/update.html
```sql
UPDATE cd.facilities
SET    initialoutlay = 10000
WHERE  facid = 1; 
```

###### Questions 4: https://pgexercises.com/questions/updates/updatecalculated.html
```sql
UPDATE cd.facilities
SET 
membercost = (SELECT membercost FROM cd.facilities WHERE facid = 0)* 1.1,
guestcost = (SELECT guestcost FROM cd.facilities WHERE facid = 0) * 1.1
WHERE facid = 1;
```

###### Questions 5: https://pgexercises.com/questions/updates/delete.html
```sql
DELETE FROM cd.bookings;
```

###### Questions 6: https://pgexercises.com/questions/updates/deletewh.html
```sql
DELETE FROM cd.members
WHERE memid = 37;
```
#### Basics
###### Questions 1: https://pgexercises.com/questions/basic/where2.html
```sql
SELECT facid,
       NAME,
       membercost,
       monthlymaintenance
FROM   cd.facilities
WHERE  membercost > 0
       AND membercost < monthlymaintenance / 50; 
```

###### Questions 2: https://pgexercises.com/questions/basic/where3.html
```sql
SELECT *
FROM   cd.facilities
WHERE  NAME LIKE '%Tennis%';
```

###### Questions 3: https://pgexercises.com/questions/basic/where4.html
```sql
SELECT *
FROM   cd.facilities
WHERE  facid IN( 1, 5 ); 
```

###### Questions 4: https://pgexercises.com/questions/basic/date.html
```sql
SELECT memid,
       surname,
       firstname,
       joindate
FROM   cd.members
WHERE  joindate > '2012-09-01'; 
```

###### Questions 5: https://pgexercises.com/questions/basic/union.html
```sql
SELECT surname
FROM   cd.members
UNION
SELECT NAME
FROM   cd. facilities; 
```

#### Join
###### Questions 1: https://pgexercises.com/questions/joins/simplejoin.html
```sql
SELECT bks.starttime
FROM   cd.bookings bks
       JOIN cd.members mems
         ON bks.memid = mems.memid
WHERE  firstname = 'David'
       AND surname = 'Farrell'; 
```

###### Questions 2: https://pgexercises.com/questions/joins/simplejoin2.html
```sql
SELECT starttime AS start,
       NAME
FROM   cd.bookings bks
       JOIN cd.facilities facs
         ON bks.facid = facs.facid
WHERE  NAME LIKE 'Tennis Court%'
       AND starttime >= '2012-09-21'
       AND starttime < '2012-09-22'
ORDER  BY starttime ASC; 
```

###### Questions 3: https://pgexercises.com/questions/joins/self2.html
```sql
SELECT mems.firstname AS memfname,
       mems.surname   AS memsname,
       recs.firstname AS recfname,
       recs.surname   AS recsname
FROM   cd.members mems
       LEFT OUTER JOIN cd.members recs
                    ON recs.memid = mems.recommendedby
ORDER  BY mems.surname,
          mems.firstname ASC; 
```

###### Questions 4: https://pgexercises.com/questions/joins/self.html
```sql
SELECT DISTINCT recs.firstname,
                recs.surname
FROM   cd.members mems
       INNER JOIN cd.members recs
               ON mems.recommendedby = recs.memid
ORDER  BY surname,
          firstname ASC; 
```

###### Questions 5: https://pgexercises.com/questions/joins/sub.html
```sql
SELECT DISTINCT Concat(mems.firstname, ' ', mems.surname) AS member,
                (SELECT Concat(recs.firstname, ' ', recs.surname) AS recommender
                 FROM   cd.members recs
                 WHERE  recs.memid = mems.recommendedby)
FROM   cd.members mems
ORDER  BY member ASC; 
```

#### Aggregation
###### Questions 1: https://pgexercises.com/questions/aggregates/count3.html
```sql
SELECT recommendedby,
       Count(*) AS count
FROM   cd.members
WHERE  recommendedby IS NOT NULL
GROUP  BY recommendedby
ORDER  BY recommendedby; 
```

###### Questions 2: https://pgexercises.com/questions/aggregates/fachours.html
```sql
SELECT facid,
       Sum(slots) AS "Total Slots"
FROM   cd.bookings
GROUP  BY facid
ORDER  BY facid; 
```

###### Questions 3: https://pgexercises.com/questions/aggregates/fachoursbymonth.html
```sql
SELECT facid,
       Sum(slots) AS "Total Slots"
FROM   cd.bookings
WHERE  starttime >= '2012-09-01'
       AND starttime < '2012-10-01'
GROUP  BY facid
ORDER  BY "total slots" ASC; 
```

###### Questions 4: https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
```sql
SELECT facid,
       Extract(month FROM starttime) AS month,
       Sum(slots)                    AS "Total Slots"
FROM   cd.bookings
WHERE  starttime >= '2012-01-01'
       AND starttime < '2013-01-01'
GROUP  BY facid,
          month
ORDER  BY facid,
          month; 
```

###### Questions 5: https://pgexercises.com/questions/aggregates/members1.html
```sql
SELECT Count(DISTINCT memid) AS count
FROM   cd.bookings; 
```

###### Questions 6: https://pgexercises.com/questions/aggregates/nbooking.html
```sql
SELECT mems.surname,
       mems.firstname,
       bks.memid,
       Min(bks.starttime)
FROM   cd.members mems
       INNER JOIN cd.bookings bks
               ON mems.memid = bks.memid
WHERE  bks.starttime >= '2012-09-01'
GROUP  BY mems.surname,
          mems.firstname,
          bks.memid
ORDER  BY bks.memid; 
```

###### Questions 7: https://pgexercises.com/questions/aggregates/countmembers.html
```sql
SELECT Count(*)
         OVER () AS count,
       firstname,
       surname
FROM   cd.members; 
```

###### Questions 8: https://pgexercises.com/questions/aggregates/nummembers.html
```sql
SELECT Row_number()
         OVER (
           ORDER BY joindate),
       firstname,
       surname
FROM   cd.members; 
```

###### Questions 9: https://pgexercises.com/questions/aggregates/fachours4.html
```sql
SELECT facid, SUM(slots) as total
FROM cd.bookings
GROUP BY facid
order by SUM(slots) DESC
LIMIT 1;
```

#### String

###### Questions 1: https://pgexercises.com/questions/string/concat.html
```sql
SELECT Concat(surname, ', ', firstname) AS NAME
FROM   cd.members; 
```

###### Questions 2: https://pgexercises.com/questions/string/reg.html
```sql
SELECT memid,
       telephone
FROM   cd.members
WHERE  telephone LIKE '(___)%'; 
```

###### Questions 3: https://pgexercises.com/questions/string/substr.html
```sql
SELECT Substr(surname, 1, 1) AS letter,
       Count(*)              AS count
FROM   cd.members
GROUP  BY letter
ORDER  BY letter ASC; 
```
