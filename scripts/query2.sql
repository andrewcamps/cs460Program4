select count(*) 
from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd') 
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'postgraduate';

select count(*)
from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'postgraduate';

select count(*)
from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'first-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'first-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'second-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'second-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'third-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'third-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'fourth-year';

select count(*)
from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid)
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'fourth-year';

select count(*)
from lease
where startdate <= TO_DATE('2018-05-02', 'yyyy-mm-dd')
and enddate >= TO_DATE('2018-05-02', 'yyyy-mm-dd');

