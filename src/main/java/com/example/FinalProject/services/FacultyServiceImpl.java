package com.example.FinalProject.services;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class FacultyServiceImpl implements FacultyService{

    @Autowired
    FacultyRepository facultyRepository;

    @Override
    public List<Faculty> getFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Optional<Faculty> findByTitle(String title) {
        return facultyRepository.findByTitle(title);
    }

    @Override
    public void saveFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    @Override
    public void deleteFacultyById(long id) {

    }

    @Override
    public Page<Faculty> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
        return facultyRepository.findAll(pageable);
    }
}
