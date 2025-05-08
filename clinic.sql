-- DROP VÀ TẠO LẠI DATABASE
DROP DATABASE IF EXISTS clinic;
CREATE DATABASE clinic;
USE clinic;

-- Bảng USER
CREATE TABLE USER_ACCOUNT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    email VARCHAR(50),
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
    Patient_id CHAR(36) PRIMARY KEY NOT NULL DEFAULT (UUID()),
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    Phone VARCHAR(50) NOT NULL,
    Address TEXT,
    Diagnosis TEXT,
    Height DECIMAL(10,2),
    Weight DECIMAL(10,2),
    Create_date DATETIME DEFAULT (Now()),
    Update_date DATETIME DEFAULT (Now())
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
    Id CHAR(36) PRIMARY KEY NOT NULL DEFAULT (UUID()),
    Name VARCHAR(50) NOT NULL,
    Manufacturer VARCHAR(100) NOT NULL,
    Expiry_date DATE NOT NULL,
    Unit VARCHAR(50) NOT NULL, 
    Price DECIMAL(10,2) NOT NULL,  
    Stock INT NOT NULL, 
    Create_date DATETIME DEFAULT (Now()),
    Update_date DATETIME DEFAULT (Now())
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

-- INSERT mẫu DRUG
INSERT INTO DRUG (Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date)
VALUES 
(
  UUID(),
  'Paracetamol 500mg',
  'ABC Pharma Co., Ltd',
  '2026-12-31',
  'Tablet',
  150000.00,
  100,
  NOW(),
  NOW()
),
(
  UUID(),
  'Amoxicillin 250mg',
  'XYZ Healthcare Inc.',
  '2025-10-15',
  'Capsule',
  250000.00,
  50,
  NOW(),
  NOW()
);

INSERT INTO DRUG (Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date)
VALUES
(UUID(), 'Ibuprofen', 'ABC Health', '2027-05-20', 'Bottle', 75.00, 200, '2025-05-07 13:00:00', '2025-05-07 13:00:00'),
(UUID(), 'Aspirin', 'GHI Pharma', '2028-01-15', 'Bottle', 60.00, 120, '2025-05-07 15:00:00', '2025-05-07 15:00:00'),
(UUID(), 'Vitamin C', 'JKL Health', '2025-12-31', 'Box', 25.00, 250, '2025-05-07 16:00:00', '2025-05-07 16:00:00');

INSERT INTO PATIENT (Patient_id, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date) VALUES
(UUID(), 'John Smith', 'john.smith@example.com', 'Male', '555-1234', '123 Main St, New York, NY', 'Common cold', 180.25, 75.50, '2024-05-01 08:00:00', '2024-05-01 08:00:00'),
(UUID(), 'Emily Johnson', 'emily.johnson@example.com', 'Female', '555-5678', '456 Park Ave, Los Angeles, CA', 'Stomach ulcer', 165.00, 60.20, '2024-05-02 09:30:00', '2024-05-02 09:30:00'),
(UUID(), 'Michael Brown', 'michael.brown@example.com', 'Male', '555-9101', '789 Broadway, Chicago, IL', 'Hypertension', 172.75, 82.40, '2024-05-03 10:45:00', '2024-05-03 10:45:00'),
(UUID(), 'Sarah Davis', 'sarah.davis@example.com', 'Female', '555-1213', '321 Ocean Dr, Miami, FL', 'Diabetes', 160.50, 68.75, '2024-05-04 14:00:00', '2024-05-04 14:00:00'),
(UUID(), 'David Wilson', 'david.wilson@example.com', 'Male', '555-1415', '654 River Rd, Seattle, WA', 'Sore throat', 177.60, 70.10, '2024-05-05 16:15:00', '2024-05-05 16:15:00');