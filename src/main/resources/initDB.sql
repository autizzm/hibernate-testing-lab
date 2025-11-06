CREATE DATABASE testdb;
USE testdb;
CREATE TABLE employee (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    empl_name VARCHAR(30) NOT NULL CHECK (CHAR_LENGTH(empl_name) != 0),
    empl_dob DATE NOT NULL CHECK (empl_dob < '2006-01-01'),
    empl_salary DECIMAL(10, 2) CHECK (empl_salary > 0)
);
