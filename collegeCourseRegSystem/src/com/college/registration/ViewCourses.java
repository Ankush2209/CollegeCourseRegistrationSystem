package com.college.registration ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewCourses {
    private Connection conn;

    public ViewCourses(Connection conn) {
        this.conn = conn;
    }

    public void viewEnrolledCourses(int studentId) {
        String sql = "SELECT c.course_name FROM courses c INNER JOIN student_courses sc ON c.course_id = sc.course_id WHERE sc.student_id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Courses enrolled by student ID " + studentId + ":");
            while (rs.next()) {
                System.out.println(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
