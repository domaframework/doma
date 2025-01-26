select
    e.*,
    d.department_id as d_department_id,
    d.department_no as d_department_no,
    d.department_name as d_department_name,
    d.location as d_location,
    d.version as d_version,
    a.address_id as a_address_id,
    a.street as a_street,
    a.version as a_version
from
    EMPLOYEE e
left outer join DEPARTMENT d
    on e.department_id = d.department_id
left outer join ADDRESS a
    on e.address_id = a.address_id
where
    e.EMPLOYEE_ID = /*employeeId*/0