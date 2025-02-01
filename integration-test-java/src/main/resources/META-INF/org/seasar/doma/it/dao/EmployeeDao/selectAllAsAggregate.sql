select
    /*%expand */*
from
    EMPLOYEE e
left outer join DEPARTMENT d
                on e.department_id = d.department_id
left outer join ADDRESS a
                on e.address_id = a.address_id
