package ru.skypro.lessons.springboot.security_web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skypro.lessons.springboot.security_web.service.Employee;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class EmployeeDTO {
    // Поля для хранения идентификатора, имени и зарплаты сотрудника
    private Long id;
    private String name;
    private Integer salary;
    private String department;

    public EmployeeDTO(Long id, String name, Integer salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Метод для преобразования сущности Employee в объект EmployeeDTO
    public static EmployeeDTO fromEmployee(Employee employee) {
        return new EmployeeDTO()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSalary(employee.getSalary());
    }

    // Метод для преобразования объекта EmployeeDTO в сущность Employee
    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setName(this.getName());
        employee.setSalary(this.getSalary());
        return employee;
    }
}
