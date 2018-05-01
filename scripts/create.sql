CREATE TABLE staff (
	StaffID int NOT NULL,
	StaffName varchar2(100) NOT NULL,
	Email varchar2(100),
	Address varchar2(100) NOT NULL,
	DOB varchar2(20) NOT NULL,
	Gender varchar2(1) NOT NULL CHECK (Gender IN ('M', 'F')),
	Title varchar2(50) NOT NULL CHECK (Title IN ('Hall Manager', 'Cleaner', 'Administrative Assistant')),
	Location varchar2(50) NOT NULL CHECK (Location IN ('Residence Office', 'Residence Hall')),
	PRIMARY KEY (StaffID)
);

CREATE TABLE residence_hall (
	HallID varchar2(10) NOT NULL,
	HallName varchar2(100) NOT NULL,
	Address varchar2(100) NOT NULL,
	Phone varchar2(15) NOT NULL,
	StaffID int,
	PRIMARY KEY (HallID),
	FOREIGN KEY (StaffID) REFERENCES staff (StaffID)
);

CREATE TABLE apartment (
	ApartmentID varchar2(10) NOT NULL,
	Accomodation int NOT NULL,
	PRIMARY KEY (ApartmentID)
);

CREATE TABLE room (
	RoomID int NOT NULL,
	RoomNum int NOT NULL,
	Rent float NOT NULL,
	HallID varchar2(10),
	ApartmentID varchar2(10),
	PRIMARY KEY (RoomID),
	FOREIGN KEY (HallID) REFERENCES residence_hall (HallID),
	FOREIGN KEY (ApartmentID) REFERENCES apartment (ApartmentID)
);

CREATE TABLE inspection (
	InspectionID int NOT NULL,
	DateInspected varchar2(20) NOT NULL,
	SatisfactoryCondition varchar2(1) CHECK (SatisfactoryCondition IN ('Y', 'N')),
	Action varchar2(300),
	StaffID int,
	RoomID int NOT NULL,
	PRIMARY KEY (InspectionID),
	FOREIGN KEY (StaffID) REFERENCES staff (StaffID),
	FOREIGN KEY (RoomID) REFERENCES room (RoomID)
);

CREATE TABLE advisor (
	AdvisorID int NOT NULL,
	AdvName varchar2(100) NOT NULL,
	Position varchar2(100) NOT NULL CHECK (Position IN ('Assistant', 'Head')),
	Department varchar2(50) NOT NULL,
	Phone varchar2(15),
	Email varchar2(300),
	PRIMARY KEY (AdvisorID)
);

CREATE TABLE student (
	StudentID int NOT NULL,
	StuName varchar2(100) NOT NULL,
	Address varchar2(100) NOT NULL,
	Phone varchar2(15) NOT NULL,
	Email varchar2(300),
	Gender varchar2(1) NOT NULL CHECK (Gender IN ('M', 'F')),
	DOB varchar2(20) NOT NULL,
	Category varchar2(14) NOT NULL CHECK (Category IN ('undergraduate', 'postgraduate')),
	ClassYear varchar2(15) NOT NULL CHECK (ClassYear IN ('first-year', 'second-year', 'third-year', 'fourth-year')),
	Major varchar2(50),
	Minor varchar2(50),
	AdvisorID int NOT NULL,
	PRIMARY KEY (StudentID),
	FOREIGN KEY (AdvisorID) REFERENCES advisor (AdvisorID)
);

CREATE TABLE lease (
	LeaseID int NOT NULL,
	Duration int NOT NULL CHECK (Duration=1 OR Duration=2),
	LName varchar2(100) NOT NULL,
	Cost float NOT NULL,
	StartDate varchar2(20) NOT NULL,
	StudentID int NOT NULL,
	RoomID int NOT NULL,
	PRIMARY KEY (LeaseID),
	FOREIGN KEY (StudentID) REFERENCES student (StudentID),
	FOREIGN KEY (RoomID) REFERENCES room (RoomID)
);

CREATE TABLE invoice (
	InvoiceID int NOT NULL,
	Semester varchar2(10) NOT NULL CHECK (Semester IN ('Fall', 'Spring')),
	PaymentDue float NOT NULL,
	DatePaid varchar2(20),
	LeaseID int NOT NULL,
	PRIMARY KEY (InvoiceID),
	FOREIGN KEY (LeaseID) REFERENCES lease (LeaseID)
);
