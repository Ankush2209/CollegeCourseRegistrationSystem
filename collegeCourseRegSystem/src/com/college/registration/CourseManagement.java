package com.college.registration ;

import java.sql.* ;


public class CourseManagement {
    private Connection conn;

    public CourseManagement(Connection conn) {
        this.conn = conn;
    }

    public void addCourse(String courseName, int seats, int slot) {
        String sql = "INSERT INTO courses (course_name, available_seats, slot) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseName);
            pstmt.setInt(2, seats);
            pstmt.setInt(3, slot);
            pstmt.executeUpdate();
            System.out.println("Course added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void viewAvailableCourses() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM courses")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Course ID: " + rs.getInt("course_id") + 
                                   ", Name: " + rs.getString("course_name") + 
                                   ",Available Seats" + rs.getInt("available_seats") + 
                                   ", Slot: " + rs.getInt("slot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

