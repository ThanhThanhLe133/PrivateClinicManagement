-- DROP VÀ TẠO LẠI DATABASE
DROP DATABASE IF EXISTS clinic;
CREATE DATABASE clinic;
USE clinic;

-- Bảng USER
CREATE TABLE USER_ACCOUNT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Email VARCHAR(50),
    Name VARCHAR(50),
    Avatar LONGBLOB,
    Gender VARCHAR(10),
    Role VARCHAR(20) NOT NULL, -- ADMIN, PATIENT, RECEPTIONIST
    Is_active BOOLEAN DEFAULT FALSE,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- BẢNG admin
CREATE TABLE ADMIN (
    Admin_id CHAR(36) PRIMARY KEY NOT NULL,
    FOREIGN KEY (Admin_id) REFERENCES USER_ACCOUNT(Id) ON DELETE CASCADE
);

-- BẢNG doctor
CREATE TABLE DOCTOR (
    Doctor_id CHAR(36) PRIMARY KEY NOT NULL,
    Phone VARCHAR(50),
    Specialized TEXT,
    Address TEXT,
    Is_confirmed BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Doctor_id) REFERENCES USER_ACCOUNT(Id) ON DELETE CASCADE
);

-- BẢNG RECEPTIONIST
CREATE TABLE RECEPTIONIST (
    Receptionist_id CHAR(36) PRIMARY KEY NOT NULL,
    Phone VARCHAR(50),
    Address TEXT,
    Is_confirmed BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Receptionist_id) REFERENCES USER_ACCOUNT(Id) ON DELETE CASCADE
);

-- Bảng Patient
CREATE TABLE PATIENT (
    Patient_id CHAR(36) PRIMARY KEY NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    Phone VARCHAR(50) NOT NULL,
    Address TEXT,
    Diagnosis TEXT,
    Height DECIMAL,
    Weight DECIMAL,
    Create_date DATETIME,
    Update_date DATETIME
);

-- BẢNG appointment
CREATE TABLE APPOINTMENT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Time DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,  -- Finish, Cancel, Unfinish
    Cancel_reason TEXT,
    Doctor_id CHAR(36) NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    Create_date DATETIME,
    Update_date DATETIME,
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Doctor_id),
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(Patient_id)
);

CREATE TABLE DRUG (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Manufacturer VARCHAR(100) NOT NULL,
    Expiry_date DATE NOT NULL,
    Unit VARCHAR(50) NOT NULL, 
    Price DECIMAL(10,2) NOT NULL,  
    Stock INT NOT NULL, 
    Create_date DATETIME,
    Update_date DATETIME
);

CREATE TABLE PRESCRIPTION (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    Doctor_id CHAR(36) NOT NULL,
    DateIssued DATE NOT NULL, 
    TotalAmount DECIMAL(10,2) NOT NULL, 
    Create_date DATETIME,
    Update_date DATETIME,
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(patient_Id),
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(doctor_Id)
);

CREATE TABLE PRESCRIPTION_DETAILS (
    Prescription_id CHAR(36) NOT NULL,
    Drug_id CHAR(36) NOT NULL,
    Quantity INT NOT NULL,  -- Số lượng
    Instructions TEXT NOT NULL,  -- Cách dùng: uống sáng/tối…
    Create_date DATETIME,
    Update_date DATETIME,
    PRIMARY KEY (Prescription_id, Drug_id),
    FOREIGN KEY (Prescription_id) REFERENCES PRESCRIPTION(Id),
    FOREIGN KEY (Drug_id) REFERENCES DRUG(Id)
);

-- INSERT mẫu ADMIN
SET @Admin_id := UUID();

INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @Admin_id,
    'admin02',
    'adminpass',
    'admin02@example.com',
    'Admin 02',
    NULL,
    'Male',
    'ADMIN',
    TRUE
);

INSERT INTO ADMIN (Admin_id) VALUES (@Admin_id);

-- INSERT mẫu DOCTOR
SET @doctor_id := UUID();

INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor_id,
    'dr.smith',
    'doctor123',
    'dr.smith@example.com',
    'Dr. John Smith',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);

INSERT INTO DOCTOR (
    doctor_id, Phone, Specialized, Address
) VALUES (
    @doctor_id,
    '0912345678',
    'Neurology',
    '12 Medical Lane, District 1'
);

-- INSERT mẫu RECEPTIONIST
SET @recept_id := UUID();

INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @recept_id,
    'recept.anna',
    'recept123',
    'anna.reception@example.com',
    'Receptionist Anna',
    NULL,
    'Female',
    'RECEPTIONIST',
    TRUE
),
(
    '10',
    'asdf',
    '123456',
    'anna.reception@example.com',
    'Receptionist Anna',
    NULL,
    'Male',
    'RECEPTIONIST',
    TRUE
);


INSERT INTO RECEPTIONIST (
    Receptionist_id, Phone, Address
) VALUES (
    @recept_id,
    '0988123456',
    '23 Front Office Blvd, District 3'
),
(
    '10',
    '0988123456',
    '23 Front Office Blvd, District 3'
);
