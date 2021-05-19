package com.example.FinalProject.services;

import com.example.FinalProject.entities.models.Faculty;
import com.example.FinalProject.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Faculty service class
 * Transfer db operations to repository
 */
@Service
public class FacultyServiceImpl implements FacultyService{

    FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<Faculty> getFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Optional<Faculty> findByTitle(String title) {
        return facultyRepository.findByTitle(title);
    }

    @Override
    public Optional<Faculty> findByFacultyById(long id) {
        return facultyRepository.findFacultyByFacultyid(id);
    }

    @Override
    public void saveFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    @Override
    public void deleteFacultyById(long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Page<Faculty> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
        return facultyRepository.findAll(pageable);
    }
}
