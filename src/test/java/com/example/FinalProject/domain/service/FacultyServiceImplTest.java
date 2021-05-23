package com.example.FinalProject.domain.service;

import com.example.FinalProject.api.mapper.FacultyMapper;
import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.repository.FacultyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FacultyServiceImplTest {

    @MockBean
    FacultyRepository facultyRepository;

    @Test
    public void getFaculties() {

        Faculty faculty = Faculty.builder().title("Math").build();
        List<Faculty> list = new ArrayList<>();
        list.add(faculty);
        list.add(Faculty.builder().title("Physics").build());
        when(facultyRepository.findAll()).thenReturn(list);

        FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        List<Faculty> faculties = facultyService.getFaculties();
        assertEquals(list,faculties);

    }
    @Test
    public void findByTitle() {
        Optional<Faculty> faculty = Optional.of(Faculty.builder().title("Math").build());
        when(facultyRepository.findByTitle(faculty.get().getTitle())).thenReturn(faculty);
        FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        Optional<FacultyModel> faculties = FacultyMapper.INSTANCE.facultyToModelFaculty(facultyService.findByTitle("Math").get());
        assertEquals(faculty.get().getTitle(),faculties.get().getTitle());
    }


    @Test
    public void findFacultyById() {
        Optional<Faculty> faculty = Optional.of(Faculty.builder().title("Math").build());
        faculty.get().setFacultyid(1);

        when(facultyRepository.findFacultyByFacultyid(faculty.get().getFacultyid())).thenReturn(faculty);
        FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        Optional<Faculty> faculties = facultyService.findByFacultyById(1);
        assertEquals(faculty,faculties);
    }

    @Test
    public void deleteFacultyById() {
        Faculty faculty = Faculty.builder().title("Math").build();
        facultyRepository.deleteById(faculty.getFacultyid());
        verify(facultyRepository, times(1)).deleteById(faculty.getFacultyid());
    }

}
