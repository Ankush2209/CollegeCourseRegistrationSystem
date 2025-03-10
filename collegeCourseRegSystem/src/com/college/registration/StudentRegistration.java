package com.college.registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRegistration {
    private Connection conn;

    public StudentRegistration(Connection conn) {
        this.conn = conn;
    }
    
    public StudentRegistration() {
    	
      }

    public void registerStudent(String name, String email, String password) {
        String sql = "INSERT INTO students (name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            System.out.println("Student registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyStudentLogin(int studentId, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM students WHERE student_id = ?")) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getStudentIdByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT student_id FROM students WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("student_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if student not found
    }
    
    public boolean verifyAdminPassword(String password) {
        // Hardcoded admin password
        String adminPassword = "Admin@123"; // Set your desired admin password here
        return adminPassword.equals(password);
    }


}
