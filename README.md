# URLQ

A simple library to filter resources using set of expressions on URL queries,
thats conveted into *javax.persistence.criteria.Predicate*.

## Usage

Example using Spring Data JPA Specifications

```java
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    List<Employee> listEmployees() {

        String filter = "inactive[:]false[and]createdAt[::]2024-01-01T00:00:00,2024-08-31T00:00:00"

        Specification<Employee> specification = new Specification<Employee>() {
            @Override
                public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> cQuery, CriteriaBuilder builder) {
                    return URLQ.predicate(root, builder, filter);
                }
        };

        return repository.findAll(specification);
    }

}
```

## Operators

Name                      | Syntax
--------------------------|-----------
Equal                     | *field[:]value*
Not equal                 | *field[~:]value*
Containing                | *field[#]value*
Not containing            | *field[~#]value*
Less than                 | *field[lt]value*
Less than or equal        | *field[lte]value*
Greater than              | *field[gt]value*
Greater than or equal     | *field[gte]value*
In                        | *field[in]value1,value2,...*
Not in                    | *field[~in]value1,value2,...*
Between                   | *field[::]start,end*

## Logical Operators

Name                      | Syntax
--------------------------|-----------
AND                       | *field1[gt]value1[and]field2[lt]value2*
OR                        | *field1[~:]value1[or]field2[:]value2*

## Composite Path

Use underscore '_' to create composite paths.

## Groupping
Use parentheses '()' to group expresssion.
