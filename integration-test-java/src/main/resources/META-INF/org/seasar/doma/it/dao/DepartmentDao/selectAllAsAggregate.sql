select
    d.department_id as d_department_id,
    d.department_no as d_department_no,
    d.department_name as d_department_name,
    d.location as d_location,
    d.version as d_version,
    e.employee_id as e_employee_id,
    e.employee_no as e_employee_no,
    e.employee_name as e_employee_name,
    e.manager_id as e_manager_id,
    e.hiredate as e_hiredate,
    e.salary as e_salary,
    e.department_id as e_department_id,
    e.address_id as e_address_id,
    e.version as e_version,
    a.address_id as a_address_id,
    a.street as a_street,
    a.version as a_version
from
    DEPARTMENT d
left outer join EMPLOYEE e
    on d.department_id = e.department_id
left outer join ADDRESS a
    on e.address_id = a.address_id
