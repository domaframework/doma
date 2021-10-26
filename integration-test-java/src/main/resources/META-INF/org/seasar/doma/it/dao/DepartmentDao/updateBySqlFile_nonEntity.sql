update
  DEPARTMENT 
set 
  DEPARTMENT_NO = /*departmentNo*/0, 
  DEPARTMENT_NAME = /*departmentName*/'aaa',
  LOCATION = /*location*/'aaa',
  VERSION = /*version*/0 + 1
where
  DEPARTMENT_ID = /*departmentId*/1
  and
  VERSION = /*version*/0