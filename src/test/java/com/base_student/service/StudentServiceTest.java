package com.base_student.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.base_student.dto.StudentRequestDto;
import com.base_student.dto.StudentResponseDto;
import com.base_student.exception.BusinessException;
import com.base_student.model.StudentModel;
import com.base_student.respository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    @Test
    void testCreate() {
        StudentModel savedStudent = new StudentModel(1, "Ana", "García", "+52-222-33-44-55", "ana.garcia@mail.com");
        StudentRequestDto requestDto = new StudentRequestDto(null, "Ana", "García", "+52-222-33-44-55",
                "ana.garcia@mail.com");

        when(studentRepository.save(org.mockito.ArgumentMatchers.any(StudentModel.class))).thenReturn(savedStudent);

        StudentResponseDto responseDto = studentService.create(requestDto);

        org.mockito.ArgumentCaptor<StudentModel> studentCaptor = org.mockito.ArgumentCaptor.forClass(StudentModel.class);
        org.mockito.Mockito.verify(studentRepository).save(studentCaptor.capture());
        StudentModel submittedStudent = studentCaptor.getValue();

        org.junit.jupiter.api.Assertions.assertNull(submittedStudent.getId());
        assertEquals("Ana", submittedStudent.getName());
        assertEquals("García", submittedStudent.getLastName());
        assertEquals("+52-222-33-44-55", submittedStudent.getPhone());
        assertEquals("ana.garcia@mail.com", submittedStudent.geteMail());

        assertEquals(savedStudent.getId(), responseDto.id());
        assertEquals(savedStudent.getName(), responseDto.name());
        assertEquals(savedStudent.getLastName(), responseDto.lastName());
        assertEquals(savedStudent.getPhone(), responseDto.phone());
        assertEquals(savedStudent.geteMail(), responseDto.eMail());
    }
    
    @Test
    void testDeleteById() {
        Integer id = 1;
        org.mockito.Mockito.doNothing().when(studentRepository).deleteById(id);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> studentService.deleteById(id));
        org.mockito.Mockito.verify(studentRepository, org.mockito.Mockito.times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        StudentModel student1 = new StudentModel(1, "John", "Doe", "+52-111-12-34-56", "john.doe@mail.com");
        StudentModel student2 = new StudentModel(2, "Jane", "Smith", "+52-333-98-76-54", "jane.smith@mail.com");
        when(studentRepository.findAll()).thenReturn(java.util.List.of(student1, student2));
        java.util.List<StudentResponseDto> result = studentService.findAll();
        org.assertj.core.api.Assertions.assertThat(result).hasSize(2);
        org.assertj.core.api.Assertions.assertThat(result.get(0).name()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should return dto when id exists")
    void testFindById() throws BusinessException {
        Integer id = 1;
        StudentModel studentModel = new StudentModel(1, "John", "Doe", "+52-111-12-34-56", "john.doe@mail.com");
        when(studentRepository.findById(id)).thenReturn(Optional.of(studentModel));
        StudentResponseDto studentResponseDto = studentService.findById(id);
        assertEquals(studentModel.getId(), studentResponseDto.id());
    }
}
