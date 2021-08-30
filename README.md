# CS425_project


### We built a web applicant. It has three different parts - user management, course management, and discussion management. Users can register and log in. The administrator needs to approve the registration. It was a team project of three. My main task is to build the frontend framework. I have been talking about the progress of the entire project with my team members, and constantly improving the design and code according to the needs of the project.

> - Graph in E-R diagram to build relationship;
> - Using SQL to store all the information in the Oracle database;
> - Data access by JDBI/SQL/JDBC;
> - BackEnd: Java and Dropwizard as framework;
> - FrontEnd: HTML, CSS and Bootstrap create view, JavaScrip/JQuery as Model/Controller.

## Clone the repository into cs425_project in your computer.

<ol>
<li>In cs425_project, double click mvnec.bat and download automatically;</li>
   * If you are not using windows, enter the command mvn eclipse:eclipse -DdownloadSources –DdownloadJavadocs in the terminal.
  
<li>In Eclipse, click "import" under "file" and select an import source "cs425_project" with clicking "Existing Projects into Workspace".</li>
<li>In Eclipse, click "Install New Software" under "help”, choose "Mars - http://download.eclipse.org/releases/mars" at "work with”, and type filter text with "web". Chose and download "Eclipse Java Web Developer Tools"</li>
<li>There is new database you can use to test all data for our project. Similar to build a new connect as Homework1, You can open oracle and then build a new connect by using the setting below. Or you can use the database of cs425 in the school.</li>
  * Username: CS425       Password: cAGk6EB9hCtXY6tx
  <li>Enter the command mvn package in the terminal to build the project, and then run oracle.bat to start the server..</li>
  * If you are not using windows, enter the following command to start the server:
 java -ea -cp .;cs425-1.0.0-fat.jar;ojdbc6.jar cs425.MyApp server oracle.json
If CLASSPATH is already set up, just run:
java -ea cs425.MyApp server oracle.json
For the red part, you need to check how to specify java classpath in your terminal.
  
</ol>


### For login as Administration:
- Username: admin     Password: 1234
- The following link you can use:
http://127.0.0.1:8080/index.html (website home page)
- You can find all links on the home page, which include user/admin login, student/faculty registration, and link part all queries for projects. 
- If logging as admin, you can find almost all tables on it.


## Skills:
 - Graph in E-R diagram to build relationship;
 - Using SQL to store all the information in the Oracle database;
 - Data access by JDBI/SQL/JDBC;
 - BackEnd: Java and Dropwizard as framework;
- FrontEnd: HTML, CSS and Bootstrap create view, JavaScrip/JQuery as Model/Controller.
