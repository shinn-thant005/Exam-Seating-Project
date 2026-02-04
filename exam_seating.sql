--- Create student table ---
CREATE TABLE student(
	roll_no VARCHAR(15) PRIMARY KEY,
    student_name VARCHAR(50)
);

--- Create major table ---
CREATE TABLE major (
	major_id VARCHAR(10) PRIMARY KEY,
    major_name VARCHAR(45)
);

--- Create room table ---
CREATE TABLE room (
	room_id INT PRIMARY KEY,
    room_name VARCHAR(15),
    total_capacity INT,
    row_capacity INT,
    column_capacity INT,
    floor INT
);

--- Create exam table ---
CREATE TABLE exam(
	exam_id INT PRIMARY KEY,
    subject VARCHAR(50),
    academic_year YEAR,
    date DATE,
    time_slot TIME
);

--- Create invigilator table ---
CREATE TABLE invigilator(
	invigilator_id INT PRIMARY KEY,
    invigilator_name VARCHAR(50),
    department VARCHAR(30)
);
