# Interactive Truth Tables


## Members:
 - Josh Gandolfo
 - Trevor Smith
 - Kyle Worley
 - Berto Cisneros
 - Zach Sherwood

## Abstract

Instructors and students working with truth tables are often subjected to creating tables that all have a similar
structure and contain the same or similar contents. This process can be repetitive, tedious, and unnecessary, so
some automation could be used to generate a base portion of the table dependent on the statements/clauses
provided.

The idea is to have an application that allows instructors to create truth table problems such as testing an
argument's validity, or finding out whether two propositional well-formed formulas (WFF) are equivalent, or
finding out whether a WFF is a tautology, etc..

We plan to build a Spring Boot application (Java based web framework), storing information in a MySQL
Database and using Boise State services for back-end/hosting. This will allow us to create truth table problems,
allowing the professor to organize different questions in chapters, and potentially evaluate student problems for
class grades.

## Project Description

We built our project using Spring Boot + Thymleaf with a MySQL database. The project supports multiple web pages with information to assist the student on each page. A webform is used for a student to pass a question to our backend through one of our controllers. The controller then sends that question to the parser which will process and build a result to be sent back to the controller. Once parsed, the controller will pass the results back to the frontend, displaying a truth table. Truth Tables give students that ability to test and practice various problems. Data is persisted in the MySQL database using a mounted volume.

To use our application navigate to sdp.boisestate.edu/truth-tables. From here you can enter a question and see the resulting truth table. You can also click on a chapter and view the problems for that chapter which the professor has the ability to modify. 

This demo shows the process of entering a logical status type problem and interacting with the resulting table. The interactive table includes functionality for checking the user's input, showing the correct true and false values, and also partially enforces correct flow of work requiring supporting answers when selecting specific final answers.
<img src="demo.gif">
