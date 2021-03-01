package com.example.FinalProject.services;

import com.example.FinalProject.entities.Faculty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FacultyServiceImplTest {

    @MockBean
    FacultyServiceImpl facultyService;

    @Test
    public void getFaculties() {

        Faculty faculty = Faculty.builder().title("Math").build();
        List<Faculty> list = new ArrayList<>();
        list.add(faculty);
        list.add(Faculty.builder().title("Physics").build());
        when(facultyService.getFaculties()).thenReturn(list);
    }
    @Test
    public void findByTitle() {
        Optional<Faculty> faculty = Optional.of(Faculty.builder().title("Math").build());

        when(facultyService.findByTitle(faculty.get().getTitle())).thenReturn(faculty);
    }


    @Test
    public void findFacultyById() {
        Optional<Faculty> faculty = Optional.of(Faculty.builder().title("Math").build());
        faculty.get().setFacultyid(1);

        when(facultyService.findByFacultyById(faculty.get().getFacultyid())).thenReturn(faculty);
    }

    @Test
    public void saveFaculty() {
        Faculty faculty = Faculty.builder().title("Math").build();
        facultyService.saveFaculty(faculty);
        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void deleteFacultyById() {
        Faculty faculty = Faculty.builder().title("Math").build();
        facultyService.deleteFacultyById(faculty.getFacultyid());
        verify(facultyService, times(1)).deleteFacultyById(faculty.getFacultyid());
    }

}
