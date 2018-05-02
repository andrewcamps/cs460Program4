select lname, paymentdue, roomnum, hallname
from ( 
	select leaseid, paymentdue 
	from invoice 
	where datepaid is NULL
) join lease using (leaseid) join room using (roomid) join residence_hall using (hallid)
union
select lname, paymentdue, roomnum, NULL
from (
    select leaseid, paymentdue
    from invoice
    where datepaid is NULL
) join lease using (leaseid) join room using (roomid) join apartment using (apartmentid);

select sum(paymentdue)
from (
    select leaseid, paymentdue
    from invoice
	where datepaid is NULL
) join lease using (leaseid) join room using (roomid);

