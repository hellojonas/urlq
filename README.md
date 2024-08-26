# Custom Query Language

## Filters


Name                      | Syntax
--------------------------|-----------
Equal                     | *field[:]*
Not equal                 | *field[~:]*
Containing                | *field[#]*
Not containing            | *field[~#]*
Less than                 | *field[lt]*
Less than or equal        | *field[lte]*
Greater than              | *field[gt]*
Greater than or equal     | *field[gte]*
In                        | *field[in]*
Not in                    | *field[~in]*
Between                   | *field[::]*

## Arrays

Use comma (,) to create arrays, for example the following filters a resource
which names not contained in the list specified.

When used with between filter **field[:]** commas are ignored.

```
/resource?name[~in]Higor,Lugo
```

## Composite path

Use underscore (_) to create composite paths, for example the following filters
a resource which sub-resource creation date is between 2000-01-10 and 
2005-04-22.

```
/resource?query=subResource_createdAt[::]2000-01-10,2005-04-22
```

## Logical operators

```
/resource?field1[::]2000-01-10,2005-04-22[or](field2[lt]1[and]field3[gt]field4)
```

Name                      | Syntax
--------------------------|-----------
AND                       | *field[gt]2[and]field[lt]5*
OR                        | *field[~:]field[or]field[:]1*


## Groupping
Use underscore () to group expresssion

```
/resource?field1[::]2000-01-10,2005-04-22[or](field2[lt]1[and]field3[gt]field4)
```
