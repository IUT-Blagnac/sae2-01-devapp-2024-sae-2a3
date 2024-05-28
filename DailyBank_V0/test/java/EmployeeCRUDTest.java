import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeCRUDTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateEmployee() {
        // Données de test
        Employee employee = new Employee("John Doe", "john.doe@example.com");

        // Comportement simulé du repository lors de l'ajout de l'employé
        // Vous pouvez ajuster cela en fonction de votre implémentation réelle
        when(employeeRepository.addEmployee(employee)).thenReturn(true);

        // Appel de la méthode à tester
        boolean result = employeeService.addEmployee(employee);

        // Vérification du résultat
        assertEquals(true, result);

        // Vérification que la méthode du repository a été appelée avec les bons arguments
        verify(employeeRepository, times(1)).addEmployee(employee);
    }

    @Test
    public void testListEmployees() {
        // Données de test
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", "john.doe@example.com"));
        employees.add(new Employee("Jane Smith", "jane.smith@example.com"));

        // Comportement simulé du repository lors de la récupération de la liste des employés
        // Vous pouvez ajuster cela en fonction de votre implémentation réelle
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        // Appel de la méthode à tester
        List<Employee> result = employeeService.getAllEmployees();

        // Vérification du résultat
        assertEquals(employees.size(), result.size());

        // Vérification que la méthode du repository a été appelée
        verify(employeeRepository, times(1)).getAllEmployees();
    }

    @Test
    public void testUpdateEmployee() {
        // Données de test
        Employee employee = new Employee("John Doe", "john.doe@example.com");

        // Comportement simulé du repository lors de la mise à jour de l'employé
        // Vous pouvez ajuster cela en fonction de votre implémentation réelle
        when(employeeRepository.updateEmployee(employee)).thenReturn(true);

        // Appel de la méthode à tester
        boolean result = employeeService.updateEmployee(employee);

        // Vérification du résultat
        assertEquals(true, result);

        // Vérification que la méthode du repository a été appelée avec les bons arguments
        verify(employeeRepository, times(1)).updateEmployee(employee);
    }

    @Test
    public void testDeleteEmployee() {
        // Données de test
        int employeeId = 1;
        
        // Comportement simulé du repository lors de la suppression de l'employé
        // Vous pouvez ajuster cela en fonction de votre implémentation réelle
        doNothing().when(employeeRepository).deleteEmployee(employeeId);

        // Appel de la méthode à tester
        boolean result = employeeService.deleteEmployee(employeeId);

        // Vérification du résultat
        assertEquals(true, result);

        // Vérification que la méthode du repository a été appelée avec les bons arguments
        verify(employeeRepository, times(1)).deleteEmployee(employeeId);
    }
}
