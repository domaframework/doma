select
    /*%expand */*
from
    EMPLOYEE e
left outer join
    DEPARTMENT d on (e.DEPARTMENT_ID = d.DEPARTMENT_ID)
where
    e.EMPLOYEE_ID = /* employeeId */0
