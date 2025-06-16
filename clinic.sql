-- SHOW PROCESSLIST;
-- KILL {Id};

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
    Date_of_birth DATE,
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


CREATE TABLE APPOINTMENT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Time DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,  -- Finish, Coming, Cancel
    Cancel_reason TEXT,
    Doctor_id CHAR(36) NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    Urgency_level INT DEFAULT 1,
    Prescription_Status VARCHAR(50) DEFAULT "None", -- None, Created, Paid
    Is_followup BOOLEAN DEFAULT FALSE,  -- true nếu là tái khám
    Priority_score INT,

    -- Khóa ngoại
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Doctor_id),
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(Patient_id),

    -- Thời gian tạo và cập nhật
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE APPOINTMENT_SERVICE (
    Appointment_id CHAR(36) NOT NULL,
    Service_id CHAR(36) NOT NULL,
    PRIMARY KEY (Appointment_id, Service_id),
    FOREIGN KEY (Appointment_id) REFERENCES APPOINTMENT(Id) ON DELETE CASCADE,
    FOREIGN KEY (Service_id) REFERENCES SERVICE(Id) ON DELETE CASCADE
);

CREATE TABLE AVAILABLE_SLOT (
    Id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    Doctor_id CHAR(36) NOT NULL,
    Slot_time TIME NOT NULL,
    Slot_date DATE NOT NULL,
    Duration_minutes INT DEFAULT 15,
    Is_booked BOOLEAN DEFAULT FALSE,
    Appointment_id CHAR(36), 
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Doctor_id),
    FOREIGN KEY (Appointment_id) REFERENCES APPOINTMENT(Id) ON DELETE SET NULL
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
    status VARCHAR(50) NOT NULL DEFAULT 'Created', -- 'khi in tính tiền thì sẽ paid
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

DELIMITER $$

CREATE TRIGGER trg_update_prescription_diagnose_after_patient_update
AFTER UPDATE ON PATIENT
FOR EACH ROW
BEGIN
    -- Nếu Diagnosis của bệnh nhân thay đổi và không NULL
    IF NEW.Diagnosis IS NOT NULL AND NEW.Diagnosis <> OLD.Diagnosis THEN
        -- Cập nhật các đơn thuốc chưa có chuẩn đoán
        UPDATE PRESCRIPTION
        SET diagnose = NEW.Diagnosis
        WHERE Patient_id = NEW.Patient_id
          AND (diagnose IS NULL OR diagnose = '');
    END IF;
END$$

DELIMITER ;



-- Tạo từng Service riêng, lấy Id ra biến
SET @Service1_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service1_id, 'General Psychological Checkup', 300000.00, 'Examination');

SET @Service2_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service2_id, 'Personal Psychological Counseling', 350000.00, 'Examination');

SET @Service3_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service3_id, 'School Psychological Counseling', 250000.00, 'Examination');

SET @Service4_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service4_id, 'Cognitive Behavioral Therapy (CBT)', 500000.00, 'Examination');

SET @Service5_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service5_id, 'Electrocardiogram (ECG)', 200000.00, 'Test');

SET @Service6_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service6_id, 'Basic Blood Test', 150000.00, 'Test');

SET @Service7_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service7_id, 'General Health Checkup', 400000.00, 'Examination');

SET @Service8_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service8_id, 'MRI Scan', 1500000.00, 'Test');

SET @Service9_id := UUID();
INSERT INTO SERVICE (Id, Name, Price, Type) VALUES 
(@Service9_id, 'Psychological Assessment', 600000.00, 'Test');

-- Tạo doctor và user cho từng service

-- Doctor 1
SET @doctor1_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor1_id,
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
    @doctor1_id,
    '0912345678',
    @Service1_id,
    '12 Medical Lane, District 1'
);

-- Doctor 2
SET @doctor2_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor2_id,
    'dr.jane',
    'doctor123',
    'dr.jane@example.com',
    'Dr. Jane Doe',
    NULL,
    'Female',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor2_id,
    '0987654321',
    @Service2_id,
    '34 Wellness Street, District 2'
);

-- Doctor 3
SET @doctor3_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor3_id,
    'dr.michael',
    'doctor123',
    'dr.michael@example.com',
    'Dr. Michael Nguyen',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor3_id,
    '0911122233',
    @Service3_id,
    '56 Psychology Avenue, District 3'
);

-- Doctor 4
SET @doctor4_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor4_id,
    'dr.emily',
    'doctor123',
    'dr.emily@example.com',
    'Dr. Emily Tran',
    NULL,
    'Female',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor4_id,
    '0933344455',
    @Service4_id,
    '78 CBT Road, District 4'
);

-- Doctor 5
SET @doctor5_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor5_id,
    'dr.henry',
    'doctor123',
    'dr.henry@example.com',
    'Dr. Henry Le',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor5_id,
    '0966677788',
    @Service5_id,
    '90 ECG Street, District 5'
);

-- Doctor 6
SET @doctor6_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor6_id,
    'dr.lisa',
    'doctor123',
    'dr.lisa@example.com',
    'Dr. Lisa Pham',
    NULL,
    'Female',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor6_id,
    '0978899001',
    @Service6_id,
    '101 Blood Test Blvd, District 6'
);

-- Doctor 7
SET @doctor7_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor7_id,
    'dr.david',
    'doctor123',
    'dr.david@example.com',
    'Dr. David Hoang',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor7_id,
    '0944556677',
    @Service7_id,
    '123 Health Plaza, District 7'
);

-- Doctor 8
SET @doctor8_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor8_id,
    'dr.sophia',
    'doctor123',
    'dr.sophia@example.com',
    'Dr. Sophia Vu',
    NULL,
    'Female',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor8_id,
    '0922334455',
    @Service8_id,
    '150 MRI Center, District 8'
);

-- Doctor 9
SET @doctor9_id := UUID();
INSERT INTO USER_ACCOUNT (
    Id, Username, Password, Email, Name, Avatar, Gender, Role, Is_active
) VALUES (
    @doctor9_id,
    'dr.alex',
    'doctor123',
    'dr.alex@example.com',
    'Dr. Alex Dang',
    NULL,
    'Male',
    'DOCTOR',
    TRUE
);
INSERT INTO DOCTOR (
    doctor_id, Phone, Service_id, Address
) VALUES (
    @doctor9_id,
    '0933445566',
    @Service9_id,
    '180 Assessment Ave, District 9'
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

INSERT INTO PATIENT 
(Patient_id, Date_of_birth, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date) 
VALUES
(@patient_id, '1980-04-15', 'John Smith', 'john.smith@example.com', 'Male', '5551234567', '123 Main St, New York, NY', 'Common cold', 180.25, 75.50, '2024-05-01 08:00:00', '2024-05-01 08:00:00'),
(UUID(), '1990-07-22', 'Emily Johnson', 'emily.johnson@example.com', 'Female', '5555678901', '456 Park Ave, Los Angeles, CA', 'Stomach ulcer', 165.00, 60.20, '2024-05-02 09:30:00', '2024-05-02 09:30:00'),
(UUID(), '1975-12-05', 'Michael Brown', 'michael.brown@example.com', 'Male', '5559101123', '789 Broadway, Chicago, IL', 'Hypertension', 172.75, 82.40, '2024-05-03 10:45:00', '2024-05-03 10:45:00'),
(UUID(), '1988-03-18', 'Sarah Davis', 'sarah.davis@example.com', 'Female', '5551213141', '321 Ocean Dr, Miami, FL', 'Diabetes', 160.50, 68.75, '2024-05-04 14:00:00', '2024-05-04 14:00:00'),
(UUID(), '1995-09-10', 'David Wilson', 'david.wilson@example.com', 'Male', '5551415161', '654 River Rd, Seattle, WA', 'Sore throat', 177.60, 70.10, '2024-05-05 16:15:00', '2024-05-05 16:15:00');


-- Cuộc hẹn 1: hoàn thành
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id,
    Urgency_level, Is_followup, Priority_score,
    Create_date, Update_date
) VALUES (
    UUID(), '2025-05-10 09:00:00', 'Finish', NULL, @doctor1_id , @patient_id,
    1, FALSE, 6, NOW(), NOW()
);

-- Cuộc hẹn 2: hủy với lý do
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id,
    Urgency_level, Is_followup, Priority_score,
    Create_date, Update_date
) VALUES (
    UUID(), '2025-05-11 10:30:00', 'Cancel', 'Patient had a family emergency', @doctor2_id, @patient_id,
    3, FALSE, 3, NOW(), NOW()
);

-- Cuộc hẹn 3: sắp tới
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id,
    Urgency_level, Is_followup, Priority_score,
    Create_date, Update_date
) VALUES (
    UUID(), '2025-05-14 15:00:00', 'Coming', NULL, @doctor3_id, @patient_id,
    1, TRUE, 9, NOW(), NOW()
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

    
INSERT INTO APPOINTMENT (
    Id, Time, Status, Cancel_reason, Doctor_id, Patient_id,
    Urgency_level, Is_followup, Priority_score,
    Create_date, Update_date
)
VALUES
(UUID(), '2025-05-10 09:00:00', 'Coming', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a',
 (SELECT Patient_id FROM PATIENT WHERE Name = 'John Smith' LIMIT 1), 1, FALSE, 10, NOW(), NOW()),

(UUID(), '2025-05-11 14:00:00', 'Finish', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a',
 (SELECT Patient_id FROM PATIENT WHERE Name = 'Emily Johnson' LIMIT 1), 2, FALSE, 8, NOW(), NOW()),

(UUID(), '2025-05-12 11:00:00', 'Cancel', 'Patient unavailable', '88669f0e-300b-11f0-bbf2-581122815a3a',
 (SELECT Patient_id FROM PATIENT WHERE Name = 'Michael Brown' LIMIT 1), 3, FALSE, 5, NOW(), NOW()),

(UUID(), '2025-05-13 16:30:00', 'Coming', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a',
 (SELECT Patient_id FROM PATIENT WHERE Name = 'Sarah Davis' LIMIT 1), 1, TRUE, 9, NOW(), NOW()),

(UUID(), '2025-05-14 10:45:00', 'Finish', NULL, '88669f0e-300b-11f0-bbf2-581122815a3a',
 (SELECT Patient_id FROM PATIENT WHERE Name = 'David Wilson' LIMIT 1), 2, FALSE, 7, NOW(), NOW());




-- Thêm các slot mỗi ngày

SET GLOBAL event_scheduler = ON;


-- Tạo Stored Procedure
DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateDailySlots $$
CREATE PROCEDURE GenerateDailySlots()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE doc_id CHAR(36);
    DECLARE start_time TIME;
    DECLARE slot_date DATE;

    DECLARE cur CURSOR FOR SELECT Doctor_id FROM DOCTOR;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    SET slot_date = CURDATE();

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO doc_id;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SET start_time = '08:00:00';

        WHILE start_time < '16:00:00' DO
            INSERT INTO AVAILABLE_SLOT (Id, Doctor_id, Slot_time, Slot_date, Duration_minutes, Is_booked)
            SELECT UUID(), doc_id, start_time, slot_date, 15, FALSE
            FROM DUAL
            WHERE NOT EXISTS (
                SELECT 1 FROM AVAILABLE_SLOT 
                WHERE Doctor_id = doc_id 
                  AND Slot_time = start_time 
                  AND Slot_date = slot_date
            );
            SET start_time = ADDTIME(start_time, '00:15:00');
        END WHILE;

    END LOOP;

    CLOSE cur;
END $$

DELIMITER ;

-- GỌI LẦN ĐẦU TIÊN NGAY
CALL GenerateDailySlots();

-- Tạo EVENT chạy tự động lúc 00:00 mỗi ngày
DELIMITER $$
DROP EVENT IF EXISTS add_daily_slots $$
CREATE EVENT add_daily_slots
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
ON COMPLETION PRESERVE
DO
BEGIN
    CALL GenerateDailySlots();
END $$

DELIMITER ;

ALTER EVENT add_daily_slots
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP;


-- DELIMITER $$

-- DROP PROCEDURE IF EXISTS GenerateDailySlots $$

-- CREATE PROCEDURE GenerateDailySlots()
-- BEGIN
--     DECLARE done INT DEFAULT FALSE;
--     DECLARE doc_id CHAR(36);
--     DECLARE start_time TIME;
--     DECLARE slot_date DATE;

--     DECLARE cur CURSOR FOR SELECT Doctor_id FROM DOCTOR;
--     DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

--     SET slot_date = CURDATE();

--     INSERT INTO DEBUG_LOG (message) VALUES (CONCAT('Bắt đầu tại ', slot_date));

--     OPEN cur;

--     read_loop: LOOP
--         FETCH cur INTO doc_id;
--         IF done THEN
--             LEAVE read_loop;
--         END IF;

--         INSERT INTO DEBUG_LOG (message) VALUES (CONCAT('Xử lý Doctor ', doc_id));

--         SET start_time = '08:00:00';

--         WHILE start_time < '16:00:00' DO

--             -- ghi debug kiểm thời gian
--             INSERT INTO DEBUG_LOG (message) VALUES (CONCAT('Kiểm khung ', start_time, ' tại ', slot_date, ' cho Doctor ', doc_id));

--             IF NOT EXISTS (
--                 SELECT 1 FROM AVAILABLE_SLOT 
--                 WHERE Doctor_id = doc_id 
--                    AND Slot_time = start_time 
--                    AND Slot_date = slot_date
--             )
--             THEN
--                 INSERT INTO AVAILABLE_SLOT (Id, Doctor_id, Slot_time, Slot_date, Duration_minutes, Is_booked)
--                 VALUES (UUID(), doc_id, start_time, slot_date, 15, 0);
                
--                 INSERT INTO DEBUG_LOG (message) VALUES (CONCAT('Đã thêm khung ', start_time, ' tại ', slot_date, ' cho Doctor ', doc_id)); 
--             ELSE
--                 INSERT INTO DEBUG_LOG (message) VALUES (CONCAT('Đã tồn tại khung ', start_time, ' tại ', slot_date, ' cho Doctor ', doc_id)); 
--             END IF;

--             SET start_time = ADDTIME(start_time, '00:15:00');    

--         END WHILE;

--     END LOOP;

--     CLOSE cur;

--     INSERT INTO DEBUG_LOG (message) VALUES ('Hoàn tất thủ tục.');

-- END $$

-- DELIMITER ;
