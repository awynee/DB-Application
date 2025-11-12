USE training_db;

CREATE TABLE IF NOT EXISTS Personnel (
    personnel_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
	department_id INT,
    role VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS TrainingProgram (
    training_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    provider VARCHAR(100),
    training_date DATE,
    duration INT,
    cost DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS Certification (
    cert_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    issuing_body VARCHAR(100),
    date_earned DATE,
    validity_period INT,
    renewal_status VARCHAR(50)
);

-- Support tables (not counted as core records)
CREATE TABLE IF NOT EXISTS TrainingRecord (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    personnel_id INT,
    training_id INT,
    status VARCHAR(50), -- completed / in-progress / not started
    completion_date DATE,
    FOREIGN KEY (personnel_id) REFERENCES Personnel(personnel_id),
    FOREIGN KEY (training_id) REFERENCES TrainingProgram(training_id)
);

CREATE TABLE IF NOT EXISTS PersonnelCertification (
    id INT PRIMARY KEY AUTO_INCREMENT,
    personnel_id INT,
    cert_id INT,
    issue_date DATE,
    expiry_date DATE,
    FOREIGN KEY (personnel_id) REFERENCES Personnel(personnel_id),
    FOREIGN KEY (cert_id) REFERENCES Certification(cert_id)
);

CREATE TABLE IF NOT EXISTS Departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL,
    department_manager VARCHAR(100)
);