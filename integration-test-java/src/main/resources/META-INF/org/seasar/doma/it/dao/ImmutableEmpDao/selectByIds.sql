select
    /*%expand */*
from
    EMPLOYEE e
left outer join
    DEPARTMENT d on (e.DEPARTMENT_ID = d.DEPARTMENT_ID)
left outer join
    EMPLOYEE m on (e.MANAGER_ID = m.EMPLOYEE_ID)
where
    e.EMPLOYEE_ID in /* employeeIds */(0)
