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

--- add foreign keys in student table ---
ALTER TABLE student 
ADD COLUMN major_id VARCHAR(10);

ALTER TABLE student
ADD FOREIGN KEY(major_id) REFERENCES major(major_id) ON DELETE SET NULL;

--- add assigned_room foreign key in student table ---
ALTER TABLE student
ADD COLUMN assigned_room INT;

ALTER TABLE student
ADD FOREIGN KEY(assigned_room) REFERENCES room(room_id) ON DELETE SET NULL;

-- Create seating table ---
CREATE TABLE seating(
	roll_no VARCHAR(15),
    exam_id INT,
    assigned_row INT,
    assigned_column INT
);

--- set composite keys of seating table ---
ALTER TABLE seating
ADD PRIMARY KEY(roll_no, exam_id);

ALTER TABLE seating
ADD FOREIGN KEY(roll_no) REFERENCES student(roll_no) ON DELETE CASCADE;

ALTER TABLE seating
ADD FOREIGN KEY(exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE;


-- Recreate seating table ---
DROP TABLE seating;

CREATE TABLE seating (
	roll_no VARCHAR(15),
    exam_id INT,
    room_id INT,
    assigned_row INT,
    assigned_column INT,
    PRIMARY KEY(roll_no, exam_id),
    FOREIGN KEY(roll_no) REFERENCES student(roll_no) ON DELETE CASCADE,
    FOREIGN KEY(exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE,
    FOREIGN KEY(room_id) REFERENCES room(room_id) ON DELETE CASCADE
);

ALTER TABLE seating 
ADD CONSTRAINT unique_seat_per_exam 
UNIQUE (exam_id, room_id, assigned_row, assigned_column);

CREATE TABLE room_supervision (
	invigilator_id INT,
    room_id INT,
    exam_id INT,
    PRIMARY KEY(invigilator_id, room_id),
    FOREIGN KEY (invigilator_id) REFERENCES invigilator(invigilator_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE
);

--- Create available_invigilator table ---
--- This tracks the "pool" of teachers available for specific exams ---
CREATE TABLE available_invigilator (
    invigilator_id INT,
    exam_id INT,
    PRIMARY KEY (invigilator_id, exam_id),
    FOREIGN KEY (invigilator_id) REFERENCES invigilator(invigilator_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE
);


