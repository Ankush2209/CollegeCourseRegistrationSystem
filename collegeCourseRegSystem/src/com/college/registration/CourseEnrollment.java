package com.college.registration ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseEnrollment {
    private Connection conn;

    public CourseEnrollment(Connection conn) {
        this.conn = conn;
    }

    public boolean enrollInCourse(int studentId, int courseId) {
        try {
            if (isAlreadyEnrolled(studentId, courseId)) {
                System.out.println("Student is already enrolled in this course.");
                return false;
            }

            if (getCourseCountForStudent(studentId) >= 5) {
                System.out.println("Cannot enroll, student already registered for the maximum of 5 courses.");
                return false;
            }

            if (isCourseSlotConflict(studentId, courseId)) {
                System.out.println("Cannot enroll, student already registered for a course in this slot.");
                return false;
            }

            String enrollSQL = "INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(enrollSQL);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();

            System.out.println("Student enrolled in the course successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dropCourse(int studentId, int courseId) {
        try {
            if (!isAlreadyEnrolled(studentId, courseId)) {
                System.out.println("Student is not enrolled in this course.");
                return false;
            }

            String dropSQL = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(dropSQL);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();

            System.out.println("Course dropped successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isAlreadyEnrolled(int studentId, int courseId) throws SQLException {
        String checkSQL = "SELECT * FROM student_courses WHERE student_id = ? AND course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkSQL);
        pstmt.setInt(1, studentId);
        pstmt.setInt(2, courseId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    private int getCourseCountForStudent(int studentId) throws SQLException {
        String countSQL = "SELECT COUNT(*) AS course_count FROM student_courses WHERE student_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(countSQL);
        pstmt.setInt(1, studentId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("course_count");
        }
        return 0;
    }

    private boolean isCourseSlotConflict(int studentId, int courseId) throws SQLException {
        String courseSlotSQL = "SELECT slot FROM courses WHERE course_id = ?";
        PreparedStatement courseStmt = conn.prepareStatement(courseSlotSQL);
        courseStmt.setInt(1, courseId);
        ResultSet courseRs = courseStmt.executeQuery();

        if (courseRs.next()) {
            int newCourseSlot = courseRs.getInt("slot");

            String slotCheckSQL = "SELECT c.slot FROM courses c INNER JOIN student_courses sc ON c.course_id = sc.course_id WHERE sc.student_id = ?";
            PreparedStatement slotStmt = conn.prepareStatement(slotCheckSQL);
            slotStmt.setInt(1, studentId);
            ResultSet slotRs = slotStmt.executeQuery();

            while (slotRs.next()) {
                if (slotRs.getInt("slot") == newCourseSlot) {
                    return true;
                }
            }
        }
        return false;
    }
}

