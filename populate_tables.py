import mysql.connector
from faker import Faker
import random

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
    rooms = ["A101", "B204", "Online"]

    print("Programmes:", programmes)
    print("Rooms:", rooms)

    # Populate Courses table
    course_names_set = set()  # Create a set to store unique course names
    for _ in range(20):
        course_name = fake.word()

        # Ensure course name is unique
        while course_name in course_names_set:
            course_name = fake.word()

        description = fake.text()
        lecturer = random.choice(["Dr. Smith", "Prof. Johnson", "Dr. Brown", "Prof. Williams"])
        room = random.choice(rooms)  # Randomly select a room

        print("Inserting course:", course_name, description, lecturer, room)

        cursor.execute("INSERT INTO Courses (CourseName, CourseDescription, Lecturer, Room) VALUES (%s, %s, %s, %s)",
                       (course_name, description, lecturer, room))
        conn.commit()

        course_names_set.add(course_name)  # Add course name to the set to ensure uniqueness

    # Populate Students table
    for _ in range(50):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = fake.email()
        phone = fake.phone_number()[:20]

        # Check if email already exists
        cursor.execute("SELECT COUNT(*) FROM Students WHERE StudentEmail = %s", (email,))
        result = cursor.fetchone()[0]

        if result == 0:  # Email does not exist, insert new student
            cursor.execute("INSERT INTO Students (StudentName, StudentEmail, Phone) VALUES (%s, %s, %s)",
                           (first_name + " " + last_name, email, phone))
            conn.commit()
        else:
            print(f"Email {email} already exists. Skipping insertion.")

    # Populate Enrollments table
    cursor.execute("SELECT StudentID FROM Students")
    student_ids = [row[0] for row in cursor.fetchall()]
    cursor.execute("SELECT CourseID FROM Courses")
    course_ids = [row[0] for row in cursor.fetchall()]

    print("Student IDs:", student_ids)
    print("Course IDs:", course_ids)

    for _ in range(100):
        student_id = random.choice(student_ids)
        course_id = random.choice(course_ids)

        # Check if the enrollment already exists
        cursor.execute("SELECT COUNT(*) FROM Enrollments WHERE StudentID = %s AND CourseID = %s",
                       (student_id, course_id))
        if cursor.fetchone()[0] == 0:
            # Enrollment doesn't exist, insert new enrollment
            cursor.execute("INSERT INTO Enrollments (StudentID, CourseID) VALUES (%s, %s)",
                           (student_id, course_id))
            conn.commit()
        else:
            print(f"Enrollment for student {student_id} in course {course_id} already exists. Skipping insertion.")

        # Populate Feedback table for each enrollment
        cursor.execute("SELECT EnrollmentID FROM Enrollments WHERE StudentID = %s AND CourseID = %s",
                       (student_id, course_id))
        enrollment_id = cursor.fetchone()[0]
        feedback_text = fake.paragraph()

        cursor.execute("INSERT INTO Feedback (EnrollmentID, FeedbackText) VALUES (%s, %s)",
                       (enrollment_id, feedback_text))
        conn.commit()

        # Populate Grades table for each enrollment
        grade = random.choice(['A', 'B', 'C', 'D', 'F'])

        cursor.execute("INSERT INTO Grades (EnrollmentID, Grade) VALUES (%s, %s)",
                       (enrollment_id, grade))
        conn.commit()

    # Populate Programme table
    for programme_name in programmes:
        cursor.execute("INSERT INTO Programme (ProgrammeName) VALUES (%s)",
                       (programme_name,))
        conn.commit()

    # Populate Modules table
    for course_id in course_ids:
        # Fetch lecturer for the current course
        cursor.execute("SELECT Lecturer FROM Courses WHERE CourseID = %s", (course_id,))
        lecturer = cursor.fetchone()[0]  # Fetch the lecturer for the current course

        for module_name in modules:
            programme_id = random.choice(range(1, 5))  # Assuming there are 4 programmes
            room = random.choice(rooms)

            cursor.execute("INSERT INTO Modules (ModuleName, CourseID, ProgrammeID, Lecturer, Room) VALUES (%s, %s, %s, %s, %s)",
                        (module_name, course_id, programme_id, lecturer, room))
            conn.commit()


    print("Data population successful.")

    # Close database connection
    cursor.close()
    conn.close()

except mysql.connector.Error as error:
    print("Error while connecting to MySQL:", error)
except Exception as e:
    print("An error occurred:", e)
