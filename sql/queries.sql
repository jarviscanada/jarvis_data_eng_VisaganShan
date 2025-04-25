--
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
             800);
--
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
--
UPDATE cd.facilities
SET    initialoutlay = 10000
WHERE  facid = 1; 
--
UPDATE cd.facilities
SET 
membercost = (SELECT membercost FROM cd.facilities WHERE facid = 0)* 1.1,
guestcost = (SELECT guestcost FROM cd.facilities WHERE facid = 0) * 1.1
WHERE facid = 1;
--
DELETE FROM cd.bookings;
--
DELETE FROM cd.members
WHERE memid = 37;
-- Basics
--
SELECT facid,
       NAME,
       membercost,
       monthlymaintenance
FROM   cd.facilities
WHERE  membercost > 0
       AND membercost < monthlymaintenance / 50;
--
SELECT *
FROM   cd.facilities
WHERE  NAME LIKE '%Tennis%';
--
SELECT *
FROM   cd.facilities
WHERE  facid IN( 1, 5 ); 
--
SELECT memid,
       surname,
       firstname,
       joindate
FROM   cd.members
WHERE  joindate > '2012-09-01'; 
--
SELECT surname
FROM   cd.members
UNION
SELECT NAME
FROM   cd. facilities;
-- Join
--
SELECT bks.starttime
FROM   cd.bookings bks
       JOIN cd.members mems
         ON bks.memid = mems.memid
WHERE  firstname = 'David'
       AND surname = 'Farrell';
--
SELECT starttime AS start,
       NAME
FROM   cd.bookings bks
       JOIN cd.facilities facs
         ON bks.facid = facs.facid
WHERE  NAME LIKE 'Tennis Court%'
       AND starttime >= '2012-09-21'
       AND starttime < '2012-09-22'
ORDER  BY starttime ASC; 
--
SELECT mems.firstname AS memfname,
       mems.surname   AS memsname,
       recs.firstname AS recfname,
       recs.surname   AS recsname
FROM   cd.members mems
       LEFT OUTER JOIN cd.members recs
                    ON recs.memid = mems.recommendedby
ORDER  BY mems.surname,
          mems.firstname ASC; 
--
SELECT DISTINCT recs.firstname,
                recs.surname
FROM   cd.members mems
       INNER JOIN cd.members recs
               ON mems.recommendedby = recs.memid
ORDER  BY surname,
          firstname ASC; 
--
SELECT DISTINCT Concat(mems.firstname, ' ', mems.surname) AS member,
                (SELECT Concat(recs.firstname, ' ', recs.surname) AS recommender
                 FROM   cd.members recs
                 WHERE  recs.memid = mems.recommendedby)
FROM   cd.members mems
ORDER  BY member ASC; 
-- Aggregation
--
SELECT recommendedby,
       Count(*) AS count
FROM   cd.members
WHERE  recommendedby IS NOT NULL
GROUP  BY recommendedby
ORDER  BY recommendedby;
--
SELECT facid,
       Sum(slots) AS "Total Slots"
FROM   cd.bookings
GROUP  BY facid
ORDER  BY facid;
--
SELECT facid,
       Sum(slots) AS "Total Slots"
FROM   cd.bookings
WHERE  starttime >= '2012-09-01'
       AND starttime < '2012-10-01'
GROUP  BY facid
ORDER  BY "total slots" ASC; 
--
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
--
SELECT Count(DISTINCT memid) AS count
FROM   cd.bookings;
--
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
--
SELECT Count(*)
         OVER () AS count,
       firstname,
       surname
FROM   cd.members; 
--
SELECT Row_number()
         OVER (
           ORDER BY joindate),
       firstname,
       surname
FROM   cd.members; 
--
SELECT facid, SUM(slots) as total
FROM cd.bookings
GROUP BY facid
order by SUM(slots) DESC
LIMIT 1;
-- String
--
SELECT Concat(surname, ', ', firstname) AS NAME
FROM   cd.members; 
--
SELECT memid,
       telephone
FROM   cd.members
WHERE  telephone LIKE '(___)%'; 
--
SELECT Substr(surname, 1, 1) AS letter,
       Count(*)              AS count
FROM   cd.members
GROUP  BY letter
ORDER  BY letter ASC; 
