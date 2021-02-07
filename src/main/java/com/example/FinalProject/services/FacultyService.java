package com.example.FinalProject.services;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<Faculty> getFaculties();
    Optional<Faculty> findByTitle(String title);
    void saveFaculty(Faculty faculty);
    void deleteFacultyById(long id);
    Page<Faculty> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}