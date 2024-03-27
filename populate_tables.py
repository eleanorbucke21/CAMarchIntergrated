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
        instructor = random.choice(lecturers)
        room = random.choice(rooms)
        credits = random.randint(1, 5)
        description = fake.text()

        print("Inserting course:", course_name, programme, instructor, room, credits, description)

        cursor.execute("INSERT INTO Courses (CourseName, Programme, Instructor, Room, Credits, Description) VALUES (%s, %s, %s, %s, %s, %s)",
                       (course_name, programme, instructor, room, credits, description))
        conn.commit()

        # Get the last inserted course ID
        cursor.execute("SELECT LAST_INSERT_ID()")
        course_id = cursor.fetchone()[0]

        # Populate Modules table for each course
        for module_name in modules:
            cursor.execute("INSERT INTO Modules (ModuleName, CourseID) VALUES (%s, %s)", (module_name, course_id))
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

    print("Data population successful.")

    # Close database connection
    cursor.close()
    conn.close()

except mysql.connector.Error as error:
    print("Error while connecting to MySQL:", error)
except Exception as e:
    print("An error occurred:", e)