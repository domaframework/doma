select * from EMPLOYEE where 
/*%if e.employeeNo > 7800*/
  /*%if e.managerId != null*/
    salary >= /*e.salary*/9999
  /*%end*/
/*%end*/
