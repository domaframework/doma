select
    /*%expand "d, e" */*,
    a.address_id as a_address_id,
    a.street as a_street,
    a.version as a_version
from
    DEPARTMENT d
left outer join EMPLOYEE e
    on d.department_id = e.department_id
left outer join ADDRESS a
    on e.address_id = a.address_id
where
    d.department_id = /*departmentId*/0