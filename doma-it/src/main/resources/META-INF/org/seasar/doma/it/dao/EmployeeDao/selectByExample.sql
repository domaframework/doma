select * from EMPLOYEE where 
/*%if e.employee_no > 7800*/
  /*%if e.manager_id != null*/
    salary >= /*e.salary*/9999
  /*%end*/
/*%end*/
