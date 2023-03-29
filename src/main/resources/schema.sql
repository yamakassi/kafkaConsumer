
CREATE TABLE if NOT EXISTS  Machine
(
    id bigint PRIMARY KEY,
    machine_name VARCHAR(60) not null,
    machine_status VARCHAR(20) not null,
    machine_service_date Date not null

);
CREATE TABLE if NOT EXISTS  Sensor
(
    id serial PRIMARY KEY,
    machine_id bigint not null  ,
    sensor_type VARCHAR(20) not null,
    value float8 not null,
    date_value TIMESTAMP  not null

);


alter table Sensor
    add foreign key (machine_id) references Machine (id);
