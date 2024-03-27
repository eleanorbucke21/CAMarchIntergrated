import mysql.connector
from faker import Faker
import random
from datetime import datetime

try:
    # Connect to MySQL
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="QaisaR123!",
        database="CMS"
    )
    cursor = conn.cursor()

    # Initialize Faker
    fake = Faker()

    # Generate fake module data
    modules = ["Module A", "Module B", "Module C", "Module D", "Module E"]

    # Courses table
    programmes = ["Computer Science", "Engineering", "Business", "Psychology"]
    lecturers = ["Dr. Smith", "Prof. Johnson", "Dr. Brown", "Prof. Williams"]
    rooms = ["A101", "B204", "Online"]

    print("Programmes:", programmes)
    print("Lecturers:", lecturers)
    print("Rooms:", rooms)

    # Populate Courses table
    for _ in range(20):
        course_name = fake.word()
        programme = random.choice(programmes)
        lecturer = random.choice(lecturers)  # Changed variable name
        room = random.choice(rooms)
        credits = random.randint(1, 5)
        description = fake.text()

        print("Inserting course:", course_name, programme, lecturer, room, credits, description)

        cursor.execute("INSERT INTO Courses (CourseName, Programme, Lecturer, Room, Credits, Description) VALUES (%s, %s, %s, %s, %s, %s)",  # Changed column name
                       (course_name, programme, lecturer, room, credits, description))
        conn.commit()

        # Get the last inserted course ID
        cursor.execute("SELECT LAST_INSERT_ID()")
        course_id = cursor.fetchone()[0]

        # Populate Modules table for each course
        for module_name in modules:
            cursor.execute("INSERT INTO Modules (ModuleName, CourseID, Lecturer) VALUES (%s, %s, %s)", (module_name, course_id, lecturer))  # Changed column name
            conn.commit()

    # Populate Students table
    for _ in range(50):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = fake.email()
        address = fake.address()
        phone = fake.phone_number()[:20] 

        cursor.execute("INSERT INTO Students (FirstName, LastName, Email, Address, Phone) VALUES (%s, %s, %s, %s, %s)",
                       (first_name, last_name, email, address, phone))
        conn.commit()

    # Enrollments table
    cursor.execute("SELECT StudentID FROM Students")
    student_ids = [row[0] for row in cursor.fetchall()]
    cursor.execute("SELECT CourseID FROM Courses")
    course_ids = [row[0] for row in cursor.fetchall()]

    print("Student IDs:", student_ids)
    print("Course IDs:", course_ids)

    for _ in range(100):  
        student_id = random.choice(student_ids)
        course_id = random.choice(course_ids)
        print("Enrolling student", student_id, "in course", course_id)
        cursor.execute("INSERT INTO Enrollments (StudentID, CourseID, EnrollmentDate) VALUES (%s, %s, %s)",
                       (student_id, course_id, datetime.now()))
        conn.commit()

    # Populate Feedback table
    for _ in range(50):
        # Query valid EnrollmentID values from Enrollments table
        cursor.execute("SELECT EnrollmentID FROM Enrollments")
        valid_enrollment_ids = [row[0] for row in cursor.fetchall()]

        if not valid_enrollment_ids:
            print("No valid enrollment IDs found. Aborting population of Feedback table.")
            break

        # Choose a random valid EnrollmentID
        enrollment_id = random.choice(valid_enrollment_ids)
        feedback_text = fake.paragraph()
        feedback_date = fake.date_time_between(start_date='-1y', end_date='now')

        cursor.execute("INSERT INTO Feedback (EnrollmentID, FeedbackText, FeedbackDate) VALUES (%s, %s, %s)",
                       (enrollment_id, feedback_text, feedback_date))
        conn.commit()

    # Populate Grades table
    for _ in range(100):
        # Query valid EnrollmentID values from Enrollments table
        cursor.execute("SELECT EnrollmentID FROM Enrollments")
        valid_enrollment_ids = [row[0] for row in cursor.fetchall()]

        if not valid_enrollment_ids:
            print("No valid enrollment IDs found. Aborting population of Grades table.")
            break

        # Choose a random valid EnrollmentID
        enrollment_id = random.choice(valid_enrollment_ids)
        grade = random.choice(['A', 'B', 'C', 'D', 'F'])
        grade_date = fake.date_time_between(start_date='-1y', end_date='now')

        cursor.execute("INSERT INTO Grades (EnrollmentID, Grade, GradeDate) VALUES (%s, %s, %s)",
                       (enrollment_id, grade, grade_date))
        conn.commit()

    # Populate programme table
    for _ in range(4):  # Assuming 4 programmes
        programme_name = fake.word()
        programme_description = fake.text()
        department = fake.word()
        start_date = fake.date_this_decade()
        end_date = fake.date_this_decade()

        cursor.execute("INSERT INTO programme (ProgrammeName, ProgrammeDescription, Department, StartDate, EndDate) VALUES (%s, %s, %s, %s, %s)",
                       (programme_name, programme_description, department, start_date, end_date))
        conn.commit()

    print("Data population successful.")

    # Close database connection
    cursor.close()
    conn.close()

except mysql.connector.Error as error:
    print("Error while connecting to MySQL:", error)
except Exception as e:
    print("An error occurred:", e)
