# Custom Query Language

## Filters


Name                      | Syntax
--------------------------|-----------
Equal                     | *field:*
Not equal                 | *field[~]:*
Containing                | *field[#]:*
Not containing            | *field[~#]:*
Less than                 | *field[lt]:*
Less than or equal        | *field[lte]:*
Greater than              | *field[gt]:*
Greater than or equal     | *field[gte]:*
In                        | *field[in]:*
Not in                    | *field[~in]:*
Between                   | *field[:]:*

## Field Separator

Use semicolon (;) to separate fields, for example the following filters a 
resource which name is Logo and age is lower than 18.

```
/resource?filters=name[~in]:Lugo;age[lt]:18
```

## Arrays

Use comma (,) to crate arrays, for example the following filters a resource
which names not contained in the list specified.

When used with between filter **field[:]:** commas are ignored.

```
/resource?name[~in]:Higor,Lugo
```

## Composite path

Use underscore (_) to crate composite paths, for example the following filters
a resource which sub-resource creation date is between 2000-01-10 and 
2005-04-22.

```
/resource?subResource_createdAt[:]:2000-01-10,2005-04-22
```
