CREATE TABLE Advisor (
	AdvisorID int NOT NULL,
	Name varchar(100) NOT NULL,
	Position varchar(100) NOT NULL,
	Department varchar(50) NOT NULL,
	Phone varchar(15),
	Email varchar(300),
	PRIMARY KEY (AdvisorID)
);
CREATE TABLE Apartment (
	ApartmentID int NOT NULL,
	Accomodation int NOT NULL,
	Address varchar(100),
	Phone varchar(15),
	PRIMARY KEY (ApartmentID)
);
CREATE TABLE Staff (
	StaffID int NOT NULL,
	Name varchar(100) NOT NULL,
	Email varchar(300),
	Address varchar(100) NOT NULL,
	DOB Date NOT NULL,
	Gender varchar(1) NOT NULL CHECK (Gender IN ('M', 'F')),
	Title varchar(50) NOT NULL,
	Location varchar(50) NOT NULL,
	PRIMARY KEY (StaffID)
);
CREATE TABLE Residence_Hall (
	HallID int NOT NULL,
	Name varchar(100) NOT NULL,
	Address varchar(100) NOT NULL,
	Phone varchar(15) NOT NULL,
	StaffID int,
	PRIMARY KEY (HallID),
	FOREIGN KEY (StaffID) REFERENCES Staff (StaffID)
);
CREATE TABLE Room (
	RoomID int NOT NULL,
	RoomNum int NOT NULL,
	Rent float NOT NULL,
	HallID int,
	ApartmentID int,
	PRIMARY KEY (RoomID),
	FOREIGN KEY (HallID) REFERENCES Residence_Hall (HallID),
	FOREIGN KEY (ApartmentID) REFERENCES Apartment (ApartmentID)
);
CREATE TABLE Inspection (
	InspectionID int NOT NULL,
	DateInspected Date,
	SatisfactoryCondition varchar(1) CHECK (SatisfactoryCondition IN ('Y', 'N')),
	Action varchar(300),
	StaffID int,
	RoomID int NOT NULL,
	PRIMARY KEY (InspectionID),
	FOREIGN KEY (StaffID) REFERENCES Staff (StaffID),
	FOREIGN KEY (RoomID) REFERENCES Room (RoomID)
);
CREATE TABLE Student (
	StudentID int NOT NULL,
	Name varchar(100) NOT NULL,
	Address varchar(100) NOT NULL,
	Phone varchar(15) NOT NULL,
	Email varchar(300),
	Gender varchar(1) NOT NULL CHECK (Gender IN ('M', 'F')),
	DOB Date NOT NULL,
	Category varchar(100) NOT NULL,
	Major varchar(50),
	Minor varchar(50),
	AdvisorID int NOT NULL,
	PRIMARY KEY (StudentID),
	FOREIGN KEY (AdvisorID) REFERENCES Advisor (AdvisorID)
);
CREATE TABLE Lease (
	LeaseID int NOT NULL,
	Duration int NOT NULL,
	StudentName varchar(100) NOT NULL,
	Cost float NOT NULL,
	StartDate Date NOT NULL,
	StudentID int NOT NULL,
	RoomID int NOT NULL,
	PRIMARY KEY (LeaseID),
	FOREIGN KEY (StudentID) REFERENCES Student (StudentID),
	FOREIGN KEY (RoomID) REFERENCES Room (RoomID)
);
CREATE TABLE Invoice (
	InvoiceID int NOT NULL,
	Semester int NOT NULL,
	PaymentDue float NOT NULL,
	DatePaid Date,
	LeaseID int NOT NULL,
	PRIMARY KEY (InvoiceID),
	FOREIGN KEY (LeaseID) REFERENCES Lease (LeaseID)
);
