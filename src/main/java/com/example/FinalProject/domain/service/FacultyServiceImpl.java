package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.mapper.FacultyMapper;
import com.example.FinalProject.pestistence.repository.facultyRepo.FacultyRepository;
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

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<FacultyModel> getFaculties() {
        return FacultyMapper.INSTANCE.facultyListToFacultyModelList(facultyRepository.findAll());
    }

    @Override
    public Optional<FacultyModel> findByTitle(String title) {
        return FacultyMapper.INSTANCE.facultyToModelFaculty(facultyRepository.findByTitle(title).get());
    }

    @Override
    public Optional<FacultyModel> findByFacultyById(long id) {
        return FacultyMapper.INSTANCE.facultyToModelFaculty(facultyRepository.findFacultyByFacultyid(id).get());
    }

    @Override
    public void saveFaculty(FacultyModel faculty) {
        facultyRepository.save(FacultyMapper.INSTANCE.facultyModelToFaculty(faculty).get());
    }

    @Override
    public void deleteFacultyById(long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Page<FacultyModel> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
//        return facultyRepository.findAll(pageable);
        return null;
    }
}
