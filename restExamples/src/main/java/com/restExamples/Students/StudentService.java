package com.restExamples.Students;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student createOrUpdateStudent(Student student) {
        Student existingStudent = studentRepository.findByRollNumber(student.getRollNumber());
        if (existingStudent != null) {
            // If student with the same roll number exists, update the existing student
            existingStudent.setName(student.getName());
            return studentRepository.save(existingStudent);
        } else {
            // If student with the roll number doesn't exist, save the new student
            return studentRepository.save(student);
        }
    }

    public Student getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
}