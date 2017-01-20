/*drop table if exists customer;

create table customer (
  id identity,
  username varchar (25) not null,
  password varchar (25) not null,
  firstname varchar (25) not null,
  lastname varchar(25) not null
);

*/
drop table if exists customer;

create table customer (
  id mediumint not null auto_increment,
  username varchar (25) not null,
  password varchar (25) not null,
  firstname varchar (25) not null,
  lastname varchar (25) not null,  
  primary key (id)
);
