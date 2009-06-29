create table emp (
    id integer primary key,
    name varchar(20),
    salary decimal(15,2),
    version integer,
    insertTimestamp timestamp,
    updateTimestamp timestamp
)