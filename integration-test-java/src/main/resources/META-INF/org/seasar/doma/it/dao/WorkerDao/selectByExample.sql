select * from EMPLOYEE where 
/*%if worker.employeeNo > 7800*/
  /*%if worker.managerId != null*/
    salary >= /*worker.salary*/9999
  /*%end*/
/*%end*/
