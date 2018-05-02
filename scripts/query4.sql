select roomnum, hallname, rent
from (	
	select roomid
	from room
		minus
	select roomid 
	from lease 
	where startdate <= '02-MAY-2018' and enddate >= '02-MAY-2018' 
	group by roomid
) join room using (roomid) join residence_hall using (hallid);
select roomnum, apartmentid, rent
from (
    select roomid
    from room
        minus
    select roomid
    from lease
    where startdate <= '02-MAY-2018' and enddate >= '02-MAY-2018'
    group by roomid
) join room using (roomid) join apartment using (apartmentid);

select count(roomid)
from (
select roomid
from room
	minus
select roomid
from lease
where startdate <= '02-MAY-2018' and enddate >= '02-MAY-2018'
);
