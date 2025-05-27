-- DROP VÀ TẠO LẠI DATABASE
DROP DATABASE IF EXISTS clinic;
CREATE DATABASE clinic;
USE clinic;

-- Bảng SERVICE
CREATE TABLE SERVICE (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Name VARCHAR(100) UNIQUE NOT NULL,
    Type VARCHAR(100) NOT NULL,-- EXAMINATION/TEST
    Price DECIMAL(10,2) NOT NULL,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Bảng USER
CREATE TABLE USER_ACCOUNT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
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
    Service_id CHAR(36),
    Address TEXT,
    Is_confirmed BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Service_id) REFERENCES SERVICE(Id) ON DELETE SET NULL,
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
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- BẢNG appointment
CREATE TABLE APPOINTMENT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Time DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,  -- Finish, Coming, Cancel
    Cancel_reason TEXT,
    Doctor_id CHAR(36) NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Doctor_id),
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(Patient_id),
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);



CREATE TABLE DRUG (
    Id CHAR(36) PRIMARY KEY NOT NULL DEFAULT (UUID()),
    Name VARCHAR(50) NOT NULL,
    Manufacturer VARCHAR(100) NOT NULL,
    Expiry_date DATE NOT NULL,
    Unit VARCHAR(50) NOT NULL, 
    Price DECIMAL(10,2) NOT NULL,  
    Stock INT NOT NULL, 
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PRESCRIPTION (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    Doctor_id CHAR(36) NOT NULL,
    Appointment_id CHAR(36) NOT NULL,
    TotalAmount DECIMAL(20,2) NOT NULL, 
    diagnose TEXT,
    advice TEXT,
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(patient_Id),
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(doctor_Id),
    FOREIGN KEY (Appointment_id) REFERENCES APPOINTMENT(Id),
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PRESCRIPTION_DETAILS (
    Prescription_id CHAR(36) NOT NULL,
    Drug_id CHAR(36) NOT NULL,
    Quantity INT NOT NULL,  -- Số lượng
    Instructions TEXT NOT NULL,  -- Cách dùng: uống sáng/tối…
    PRIMARY KEY (Prescription_id, Drug_id),
    FOREIGN KEY (Prescription_id) REFERENCES PRESCRIPTION(Id),
    FOREIGN KEY (Drug_id) REFERENCES DRUG(Id),
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DELIMITER //

CREATE TRIGGER trg_update_prescription_diagnose_after_patient_update
AFTER UPDATE ON PATIENT
FOR EACH ROW
BEGIN
    -- Nếu trường Diagnosis thay đổi
    IF NEW.Diagnosis <> OLD.Diagnosis THEN
        UPDATE PRESCRIPTION
        SET diagnose = NEW.Diagnosis
        WHERE Patient_id = NEW.Patient_id;
    END IF;
END;
//

DELIMITER ;

-- Insert mẫu service
SET @Service_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type)
VALUES 
    (UUID(), 'General Psychological Checkup', 300000.00, 'Examination'),
    (UUID(), 'Personal Psychological Counseling', 350000.00, 'Examination'),
    (UUID(), 'School Psychological Counseling', 250000.00, 'Examination'),
    (UUID(), 'Cognitive Behavioral Therapy (CBT)', 500000.00, 'Examination'),
    (UUID(), 'Electrocardiogram (ECG)', 200000.00, 'Test'),
    (UUID(), 'Basic Blood Test', 150000.00, 'Test'),
    (UUID(), 'General Health Checkup', 400000.00, 'Examination'),
    (@Service_id, 'Customized Health Checkup', 450000.00, 'Examination'),
    (UUID(), 'MRI Scan', 1500000.00, 'Test'),
    (UUID(), 'Psychological Assessment', 600000.00, 'Test');

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

-- INSERT mẫu DOCTOR (NHỚ LẤY ID BÊN SERVICE ĐỂ GÁN VÀO DOCTOR)
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
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor_id,
    '0912345678',
    @Service_id,
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
  'Tablet',  -- Viên nén
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
  'Capsule',  -- Viên nang
  250000.00,
  50,
  NOW(),
  NOW()
);

INSERT INTO DRUG (Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date)
VALUES
(UUID(), 'Ibuprofen', 'ABC Health', '2027-05-20', 'Bottle', 75.00, 200, '2025-05-07 13:00:00', '2025-05-07 13:00:00'),  -- Dạng siro
(UUID(), 'Aspirin', 'GHI Pharma', '2028-01-15', 'Strip', 60.00, 120, '2025-05-07 15:00:00', '2025-05-07 15:00:00'),    -- Theo vỉ
(UUID(), 'Vitamin C', 'JKL Health', '2025-12-31', 'Box', 25.00, 250, '2025-05-07 16:00:00', '2025-05-07 16:00:00');    -- Hộp chứa viên

SET @patient_id := UUID();

INSERT INTO PATIENT (Patient_id, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date) VALUES
(@patient_id, 'John Smith', 'john.smith@example.com', 'Male', '5551234567', '123 Main St, New York, NY', 'Common cold', 180.25, 75.50, '2024-05-01 08:00:00', '2024-05-01 08:00:00'),
(UUID(), 'Emily Johnson', 'emily.johnson@example.com', 'Female', '5555678901', '456 Park Ave, Los Angeles, CA', 'Stomach ulcer', 165.00, 60.20, '2024-05-02 09:30:00', '2024-05-02 09:30:00'),
(UUID(), 'Michael Brown', 'michael.brown@example.com', 'Male', '5559101123', '789 Broadway, Chicago, IL', 'Hypertension', 172.75, 82.40, '2024-05-03 10:45:00', '2024-05-03 10:45:00'),
(UUID(), 'Sarah Davis', 'sarah.davis@example.com', 'Female', '5551213141', '321 Ocean Dr, Miami, FL', 'Diabetes', 160.50, 68.75, '2024-05-04 14:00:00', '2024-05-04 14:00:00'),
(UUID(), 'David Wilson', 'david.wilson@example.com', 'Male', '5551415161', '654 River Rd, Seattle, WA', 'Sore throat', 177.60, 70.10, '2024-05-05 16:15:00', '2024-05-05 16:15:00');

-- Cuộc hẹn 1: đã hoàn thành
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id, Create_date, Update_date
) VALUES (
    UUID(), '2025-05-10 09:00:00', 'Finish', NULL, @doctor_id, @patient_id, NOW(), NOW()
);

-- Cuộc hẹn 2: đã hủy, có lý do
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id, Create_date, Update_date
) VALUES (
    UUID(), '2025-05-11 10:30:00', 'Cancel', 'Patient had a family emergency', @doctor_id, @patient_id, NOW(), NOW()
);

-- Cuộc hẹn 3: chưa hoàn thành
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id, Create_date, Update_date
) VALUES (
    UUID(), '2025-05-14 15:00:00', 'Coming', NULL, @doctor_id, @patient_id, NOW(), NOW()
);

-- Thêm bản ghi vào USER_ACCOUNT trước
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    '88669f0e-300b-11f0-bbf2-581122815a3a',
    'dr.fake',
    'password123',
    'fake.doctor@example.com',
    'Fake Doctor',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);

-- Sau đó mới chèn vào DOCTOR
INSERT INTO DOCTOR (Doctor_id, Phone, Service_id, Address, Is_confirmed)
VALUES 
(
    '88669f0e-300b-11f0-bbf2-581122815a3a',
    '0912345678',
    (SELECT Id FROM SERVICE WHERE Name = 'General Psychological Checkup' LIMIT 1),
    '12 Medical Lane, District 1',
    TRUE
);

    
INSERT INTO APPOINTMENT (Id, Time, Status, Cancel_reason, Doctor_id, Patient_id, Create_date, Update_date)
VALUES 
(UUID(), '2025-05-10 09:00:00', 'InProgress', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a', 
    (SELECT Patient_id FROM PATIENT WHERE Name = 'John Smith' LIMIT 1), NOW(), NOW()),

(UUID(), '2025-05-11 14:00:00', 'Finished', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a', 
    (SELECT Patient_id FROM PATIENT WHERE Name = 'Emily Johnson' LIMIT 1), NOW(), NOW()),

(UUID(), '2025-05-12 11:00:00', 'Cancelled', 'Patient unavailable', '88669f0e-300b-11f0-bbf2-581122815a3a', 
    (SELECT Patient_id FROM PATIENT WHERE Name = 'Michael Brown' LIMIT 1), NOW(), NOW()),

(UUID(), '2025-05-13 16:30:00', 'InProgress', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a', 
    (SELECT Patient_id FROM PATIENT WHERE Name = 'Sarah Davis' LIMIT 1), NOW(), NOW()),

(UUID(), '2025-05-14 10:45:00', 'Finished', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a', 
    (SELECT Patient_id FROM PATIENT WHERE Name = 'David Wilson' LIMIT 1), NOW(), NOW());