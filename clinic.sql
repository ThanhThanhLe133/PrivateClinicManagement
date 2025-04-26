CREATE TABLE USER_ACCOUNT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Role VARCHAR(20) NOT NULL, -- ADMIN, PATIENT, RECEPTIONIST
    Is_active BOOLEAN DEFAULT FALSE,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE ADMIN (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Name VARCHAR(50),
    Avartar TEXT,
    Email VARCHAR(50),
    Gender VARCHAR(10),
    Is_active BOOLEAN,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE DOCTOR (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Name VARCHAR(50),
    Avartar TEXT,
    Email VARCHAR(50),
    Gender VARCHAR(10),
    Is_active BOOLEAN,
    Phone VARCHAR(50),
    Specialized TEXT,
    Address TEXT,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE RECEPTIONIST (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Name VARCHAR(50),
    Avartar TEXT,
    Email VARCHAR(50),
    Gender VARCHAR(10),
    Is_active BOOLEAN,
    Phone VARCHAR(50),
    Address TEXT,
    Create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PATIENT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    Appointment_id CHAR(36),
    Phone VARCHAR(50) NOT NULL,
    Address TEXT,
    Diagnosis TEXT,
    Height DECIMAL,
    Weight DECIMAL,
    Create_date DATETIME,
    Update_date DATETIME,
    FOREIGN KEY (Appointment_id) REFERENCES APPOINTMENT(Id)
);
CREATE TABLE APPOINTMENT (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Time DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,  -- Finish, Cancel, Unfinish
    Cancel_reason TEXT,
    Doctor_id CHAR(36) NOT NULL,
    Patient_id CHAR(36) NOT NULL,
    Create_date DATETIME,
    Update_date DATETIME,
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Id),
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(Id)
);

CREATE TABLE DRUG (
    Id CHAR(36) PRIMARY KEY NOT NULL,
    Name VARCHAR(50) NOT NULL,
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
    FOREIGN KEY (Patient_id) REFERENCES PATIENT(Id),
    FOREIGN KEY (Doctor_id) REFERENCES DOCTOR(Id)
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


INSERT INTO DOCTOR (Id, Username, Password, Name, Avartar, Email, Gender, Is_active, Phone, Specialized, Address, Create_date, Update_date)
VALUES 
(UUID(), 'doctor123', 'doctorpass', 'Dr. Nguyễn Văn A', 'avatar.jpg', 'doctorA@example.com', 'Male', TRUE, '0123456789', 'Cardiology', '123 Main St, Hanoi', NOW(), NOW()),
(UUID(), 'doctor456', 'doctorpass', 'Dr. Trần Thị B', 'avatar.jpg', 'doctorB@example.com', 'Female', TRUE, '0987654321', 'Neurology', '456 Park Ave, Hanoi', NOW(), NOW());

INSERT INTO RECEPTIONIST (Id, Username, Password, Name, Avartar, Email, Gender, Is_active, Phone, Address, Create_date, Update_date)
VALUES 
(UUID(), 'receptionist123', 'receptionistpass', 'Nguyễn Thị C', 'avatar.jpg', 'receptionistC@example.com', 'Female', TRUE, '0123456789', '789 Oak St, Hanoi', NOW(), NOW()),
(UUID(), 'receptionist456', 'receptionistpass', 'Trần Văn D', 'avatar.jpg', 'receptionistD@example.com', 'Male', TRUE, '0987654321', '101 Pine St, Hanoi', NOW(), NOW());

INSERT INTO ADMIN (Id, Username, Password, Name, Avartar, Email, Gender, Is_active, Create_date, Update_date)
VALUES 
(UUID(), 'admin123', 'adminpass', 'Lê Thị E', 'avatar.jpg', 'adminE@example.com', 'Female', TRUE, NOW(), NOW()),
(UUID(), 'admin456', 'adminpass', 'Nguyễn Văn F', 'avatar.jpg', 'adminF@example.com', 'Male', TRUE, NOW(), NOW());


