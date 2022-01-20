package com.roman.service;

import com.roman.dto.EmployeeFullInfoDto;
import com.roman.entity.Department;
import com.roman.exceptions.*;
import com.roman.repo.DepartmentRepo;
import com.roman.resource.feign.EmployeesFeignClient;
import com.roman.resource.feign.exception.FallbackException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DepartmentServiceImpl.class )
public class DepartmentServiceImplTest {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @MockBean
    private DepartmentRepo departmentRepo;

    @MockBean
    private EmployeesFeignClient feignClient;

    private Department mainDepartment;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        mainDepartment = new Department();

        Department subDepartment = new Department();
        subDepartment.setId(2L);
        subDepartment.setDepartmentName("SubDepartment");
        subDepartment.setMainDepartment(mainDepartment);

        Department anotherSubDepartment = new Department();
        anotherSubDepartment.setId(3L);
        anotherSubDepartment.setDepartmentName("AnotherSubDepartment");
        anotherSubDepartment.setMainDepartment(subDepartment);

        Set<Department> subDepartments = new HashSet<>();
        Set<Department> anotherSubDepartments = new HashSet<>();
        subDepartments.add(subDepartment);
        anotherSubDepartments.add(anotherSubDepartment);

        subDepartment.setSubDepartments(anotherSubDepartments);

        mainDepartment.setId(1L);
        mainDepartment.setDepartmentName("Production");
        mainDepartment.setSubDepartments(subDepartments);
        mainDepartment.setMainDepartment(null);

        assertEquals(1L, (long) mainDepartment.getId());
        assertEquals("Production", mainDepartment.getDepartmentName());
        assertEquals(1, subDepartments.size());
    }

    @Test
    public void testMockCreation() {
        assertNotNull(departmentService);
        assertNotNull(departmentRepo);
        assertNotNull(feignClient);

    }

    @Test
    public void whenSaveDepartmentShouldReturnDepartment() {
        Mockito.when(departmentRepo.findByDepartmentName("Production")).thenReturn(null);
        Mockito.when(departmentRepo.save(mainDepartment)).thenReturn(mainDepartment);

        Department createdDepartment = departmentService.create(mainDepartment);
        assertEquals(mainDepartment, createdDepartment);

        Mockito.verify(departmentRepo, Mockito.times(1)).findByDepartmentName("Production");
        Mockito.verify(departmentRepo, Mockito.times(1)).save(mainDepartment);

    }

    @Test(expected = DepartmentAlreadyExistsException.class)
    public void whenDepartmentExistsThenReturnDepartmentAlreadyExistsException() {
        Mockito.when(departmentRepo.findByDepartmentName("Production")).thenReturn(mainDepartment);
        departmentService.create(mainDepartment);
    }

    @Test
    public void whenGivenIdShouldReturnDepartmentIfFound() {
        Mockito.when(departmentRepo.findById(1L)).thenReturn(Optional.of(mainDepartment));
        departmentService.findById(1L);
        Mockito.verify(departmentRepo, Mockito.times(1)).findById(1L);

    }

    @Test
    public void whenDepartmentDoesntExistShouldThrowDepartmentNotFoundException() {
        Mockito.when(departmentRepo.findById(1L)).thenThrow(new DepartmentNotFoundException(1L));
        thrown.expect(DepartmentNotFoundException.class);
        thrown.expectMessage("Could not find department with id = 1");
        departmentService.findById(1L);
    }

    @Test
    public void whenGivenNameShouldReturnDepartmentIfFound() {
        Mockito.when(departmentRepo.findByDepartmentName("Production")).thenReturn(mainDepartment);
        Department foundDepartment = departmentService.findByDepartmentName("Production");
        assertEquals(mainDepartment, foundDepartment);
    }

    @Test(expected = DepartmentNotFoundException.class)
    public void whenDepartmentNotFoundByNameThenReturnDepartmentNotFoundException() {
        Mockito.when(departmentRepo.findByDepartmentName("Production")).thenThrow(new DepartmentNotFoundException(mainDepartment.getDepartmentName()));
        departmentService.findByDepartmentName("Production");

    }

    @Test
    public void whenGivenIdShouldDeleteDepartmentAndReturnTrue() {
        Long departmentId = mainDepartment.getId();
        List<EmployeeFullInfoDto> employeeList = new ArrayList<>();
        mainDepartment.setSubDepartments(Collections.emptySet());

        assertEquals(Collections.emptySet(), mainDepartment.getSubDepartments());

        Mockito.when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(mainDepartment));
        Mockito.when(feignClient.findByDepartmentId(departmentId)).thenReturn(employeeList);

        Assert.assertTrue(departmentService.delete(1L));
        Mockito.verify(departmentRepo, Mockito.times(1)).deleteById(departmentId);
    }

    @Test
    public void deleteFailTestWhenDepartmentHasEmployees() {
        Long departmentId = mainDepartment.getId();

        EmployeeFullInfoDto employee = new EmployeeFullInfoDto();
        employee.setFirstName("Alex");

        List<EmployeeFullInfoDto> employeeList = new ArrayList<>();
        employeeList.add(employee);
        assertEquals(1, employeeList.size());

        Mockito.when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(mainDepartment));
        Mockito.when(feignClient.findByDepartmentId(departmentId)).thenReturn(employeeList);

        thrown.expect(DepartmentNotEmptyException.class);
        thrown.expectMessage("Could not dismissal department with id = " + departmentId + " because it is not empty");

        departmentService.delete(1L);
    }

    @Test
    public void deleteFailTestWhenDepartmentHasSubDepartmentsReturnDepartmentHasSubDepartmentException() {
        Long departmentId = mainDepartment.getId();

        EmployeeFullInfoDto employee = new EmployeeFullInfoDto();
        employee.setFirstName("Alex");

        Mockito.when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(mainDepartment));
        Mockito.when(feignClient.findByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        thrown.expect(DepartmentHasSubDepartmentException.class);
        thrown.expectMessage("Can't dismissal Department with id " + departmentId + " because it has subDepartments");

        departmentService.delete(1L);
    }

    @Test(expected = FallbackException.class)
    public void returnFallbackMethodWhenEmployeeServiceIsNotAvailable() {
        Long departmentId = mainDepartment.getId();

        EmployeeFullInfoDto employee = new EmployeeFullInfoDto();
        employee.setFirstName("Alex");

        Mockito.when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(mainDepartment));
        Mockito.when(feignClient.findByDepartmentId(departmentId)).thenThrow(FallbackException.class);

        departmentService.delete(1L);
    }

    @Test
    public void whenCallFindAllSubDepartmentsReturnTwo() {
        assertEquals(2, departmentService.findAllSubDepartments(mainDepartment).size());
    }

    @Test
    public void whenCallFindAllHeadDepartmentsReturnTwo() {
        Set<Department> subDepartments = departmentService.findAllSubDepartments(mainDepartment);

        Department subDepartment = subDepartments.stream()
                .filter(someDepartment -> someDepartment.getDepartmentName().equals("AnotherSubDepartment"))
                .findFirst()
                .orElse(null);

        assertEquals(2, departmentService.findAllHeadDepartments(subDepartment).size());
    }

    @Test
    public void whenCallSalaryFundShouldReturn3000() {
        Long departmentId = mainDepartment.getId();

        EmployeeFullInfoDto employee1 = new EmployeeFullInfoDto();
        employee1.setFirstName("Alex");
        employee1.setDepartmentId(1L);
        employee1.setSalary(1000L);

        EmployeeFullInfoDto employee2 = new EmployeeFullInfoDto();
        employee2.setDepartmentId(1L);
        employee2.setFirstName("Greg");
        employee2.setSalary(2000L);

        List<EmployeeFullInfoDto> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);

        Mockito.when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(mainDepartment));
        Mockito.when(feignClient.findByDepartmentId(departmentId)).thenReturn(employeeList);

        assertEquals(3000L, (long) departmentService.getSalaryFund(departmentId));
    }


    @Test
    public void whenGivenDepartmentShouldUpdateItIfFound() {
        Mockito.when(departmentRepo.save(ArgumentMatchers.any(Department.class))).thenReturn(mainDepartment);
        departmentService.update(mainDepartment);
        Mockito.verify(departmentRepo, Mockito.times(1)).save(mainDepartment);
    }

    @Test
    public void shouldReturnAllDepartment() {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(mainDepartment);

        Mockito.when(departmentRepo.findAll()).thenReturn(departmentList);
        assertEquals(1, departmentService.findAll().size());

        Mockito.verify(departmentRepo, Mockito.times(1)).findAll();
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Tests finished");
    }
}