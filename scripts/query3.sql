select lname, paymentdue, roomnum, hallname
from (
	select leaseid, paymentdue, datepaid 
	from invoice 
		minus 
	select leaseid, paymentdue, datepaid 
	from invoice 
	where datepaid < to_date('01-01-5000', 'DD-MM-YYYY')
) join lease using (leaseid) join room using (roomid) join residence_hall using (hallid)
union
select lname, paymentdue, roomnum, NULL
from (
    select leaseid, paymentdue, datepaid
    from invoice
        minus
    select leaseid, paymentdue, datepaid
    from invoice
    where datepaid < to_date('01-01-5000', 'DD-MM-YYYY')
) join lease using (leaseid) join room using (roomid) join apartment using (apartmentid);

select sum(paymentdue)
from (
    select leaseid, paymentdue, datepaid
    from invoice
        minus
    select leaseid, paymentdue, datepaid
    from invoice
    where datepaid < to_date('01-01-5000', 'DD-MM-YYYY')
) join lease using (leaseid) join room using (roomid);

