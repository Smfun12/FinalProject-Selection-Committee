package com.example.FinalProject.pestistence.repository.facultyRepo;

import com.example.FinalProject.api.dto.HeaderStorage;
import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.mapper.FacultyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class FacultyRepo implements DefaultFacultyRepository{

    private final FacultyJDBCRepo facultyJDBCRepo;
    private final FacultyRepository facultyRepository;
    HeaderStorage headerStorage;

    public FacultyRepo(FacultyJDBCRepo facultyJDBCRepo, FacultyRepository facultyRepository) {
        this.facultyJDBCRepo = facultyJDBCRepo;
        this.facultyRepository = facultyRepository;
        this.headerStorage = new HeaderStorage();
    }

    @Override
    public List<FacultyModel> getFaculties() {
        if (headerStorage.getHeader().equals("jdbc")){
            return FacultyMapper.INSTANCE.facultyListToFacultyModelList(facultyJDBCRepo.getFaculties());
        }
        return FacultyMapper.INSTANCE.facultyListToFacultyModelList(facultyRepository.findAll());
    }

    @Override
    public FacultyModel findByTitle(String title) {
        Optional<Faculty> byTitle;
        if (headerStorage.getHeader().equals("jdbc")){
            byTitle = facultyJDBCRepo.findByTitle(title);
        }
        else {
            byTitle = facultyRepository.findByTitle(title);
        }
        if (!byTitle.isPresent()){
            throw new NoSuchElementException("Faculty with title:" + title + " not found");
        }
        return FacultyMapper.INSTANCE.facultyToModelFaculty(byTitle.get());
    }

    @Override
    public FacultyModel findById(long id) {
        Optional<Faculty> byId;
        if (headerStorage.getHeader().equals("jdbc")){
            byId = facultyJDBCRepo.findById(id);
        }
        else {
            byId = facultyRepository.findFacultyByFacultyid(id);
        }
        if (!byId.isPresent()){
            throw new NoSuchElementException("Faculty with id:" + id + " not found");
        }
        return FacultyMapper.INSTANCE.facultyToModelFaculty(byId.get());
    }

    @Override
    public void saveFaculty(FacultyModel faculty) {
        if (headerStorage.getHeader().equals("jdbc")){
            facultyJDBCRepo.saveFaculty(FacultyMapper.INSTANCE.facultyModelToFaculty(faculty));
        }
        facultyJDBCRepo.saveFaculty(FacultyMapper.INSTANCE.facultyModelToFaculty(faculty));
    }

    @Override
    public void deleteById(long id) {
        if (headerStorage.getHeader().equals("jdbc")){
            facultyJDBCRepo.deleteById(id);
        }
        facultyJDBCRepo.deleteById(id);
    }
}
