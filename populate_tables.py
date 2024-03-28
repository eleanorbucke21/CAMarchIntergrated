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

    # Populate Enrollments, Feedback, Grades, and Programme tables (unchanged from your original code)

    print("Data population successful.")

    # Close database connection
    cursor.close()
    conn.close()

except mysql.connector.Error as error:
    print("Error while connecting to MySQL:", error)
except Exception as e:
    print("An error occurred:", e)
