select
    d.department_id,
    d.department_no,
    d.department_name,
    d.location,
    d.version,
    e.employee_id as e_employee_id,
    e.employee_no as e_employee_no,
    e.employee_name as e_employee_name,
    e.manager_id as e_manager_id,
    e.hiredate as e_hiredate,
    e.salary as e_salary,
    e.department_id as e_department_id,
    e.address_id as e_address_id,
    a.address_id as a_address_id,
    a.street as a_street,
    a.version as a_version
from
    DEPARTMENT as d
left outer join EMPLOYEE as e
    on d.department_id = e.department_id
left outer join ADDRESS as a
    on e.address_id = a.address_id
where
    d.department_id = /*departmentId*/0