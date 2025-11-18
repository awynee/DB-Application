
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
    cost DECIMAL(10,2),
    cert_id INT
);

CREATE TABLE IF NOT EXISTS Certification (
    cert_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    issuing_body VARCHAR(100),
    validity_period INT
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

INSERT INTO Departments (department_name, department_manager) VALUES
    ('Human Resources', 'Roberto Robertson'),
    ('Finance', 'Paolo Patawaran'),
    ('IT Services', 'Alwayne Dacanay'),
    ('Operations', 'Carlos Austria'),
    ('Marketing', 'John Perez'),
    ('Legal', 'Atty. Kim Possible'),
    ('Quality Assurance', 'Sasa Lele'),
    ('Security', 'Lt. Mark Reyes'),
    ('Training & Development', 'Sarah Lim'),
    ('Logistics', 'Carlos Dizon');
    
INSERT INTO Personnel (name, department_id, role) VALUES
    ('Roberto Robertson', 1, 'Department Manager'),
    ('Paolo Patawaran', 2, 'Department Manager'),
    ('Alwayne Dacanay', 3, 'Department Manager'),
    ('Carlos Austria', 4, 'Department Manager'),
    ('John Perez', 5, 'Department Manager'),
    ('Atty. Kim Possible', 6, 'Department Manager'),
    ('Sasa Lele', 7, 'Department Manager'),
    ('Lt. Mark Reyes', 8, 'Department Manager'),
    ('Sarah Lim', 9, 'Department Manager'),
    ('Carlos Dizon', 10, 'Department Manager'),
    ('Peter Mendoza', 1, 'HR Associate'),
    ('Maria Klara', 2, 'Junior Accountant'),
    ('Emilio Sinclair', 3, 'IT Specialist'),
    ('Sosou Frieren', 4, 'Operations Analyst'),
    ('Freddy Fazbear', 5, 'Marketing Coordinator'),
    ('Saul Goodman', 6, 'Legal Assistant'),
    ('Matthew Mercer', 7, 'QA Technician'),
    ('Johnny SilverHand', 8, 'Security Officer'),
    ('Allmind', 9, 'Training Coordinator'),
    ('John Helldiver', 10, 'Logistics Supervisor');



INSERT INTO Certification (name, issuing_body, validity_period) VALUES
    ('Basic First Aid Certificate', 'Red Cross', 2),
    ('Fire Safety Certification', 'BFP', 3),
    ('Data Privacy Compliance', 'NPC', 1),
    ('IT Security Foundations', 'CompTIA', 3),
    ('Leadership Training Cert', 'UST-Center for Leadership', 5),
    ('Equipment Handling Cert', 'TESDA', 2),
    ('Project Management Basics', 'PMI', 3),
    ('Customer Service Excellence', 'CS Academy', 1),
    ('Advanced Excel Certificate', 'Microsoft', 2),
    ('Company Orientation Cert', 'HR Dept', 1);

INSERT INTO TrainingProgram (title, provider, training_date, duration, cost, cert_id) VALUES
    ('Basic First Aid Training', 'Red Cross', '2024-11-16', 8, 2000.00, 1),
    ('Fire Safety and Evacuation', 'Bureau of Fire Protection', '2024-10-20', 6, 1500.00, 2),
    ('Data Privacy Compliance Workshop', 'National Privacy Commission', '2024-12-05', 4, 1000.00, 3),
    ('Cybersecurity Awareness Foundations', 'CompTIA', '2025-01-12', 5, 2800.00, 4),
    ('Effective Leadership Essentials', 'UST Leadership Center', '2024-09-10', 12, 3200.00, 5),
    ('Equipment Operation Level 1', 'TESDA', '2024-08-18', 10, 1800.00, 6),
    ('Project Management Basics', 'PMI Philippines', '2024-11-03', 6, 2500.00, 7),
    ('Customer Service Workshop', 'Customer Service Academy', '2024-12-01', 3, 900.00, 8),
    ('Advanced Excel Data Analysis', 'Microsoft Philippines', '2024-10-15', 6, 1400.00, 9),
    ('Company Orientation Program', 'HR Department', '2024-07-20', 2, 500.00, 10);
