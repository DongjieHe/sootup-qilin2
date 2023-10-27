art of illusion v2.8.1

INTRODUCTION
------------
This is a modified version of the art of illusion project. 
The executable original is available in the download section of http://java.labsoft.dcc.ufmg.br/qualitas.class/

For information concerning the use of the original project see http://www.artofillusion.org/


MODIFICATIONS
-------------
For purposes of the xcorpus project, several changes have been applied to the original project architecture:

DELETED bin folder       - own binaries folder will be created during build process
ADDED exercise.xml       - ant build file
ADDED yvi.xml            - yvi file for project dependencies
ADDED README-xcorpus.txt - this file
REPLACED libraries       - with ivy dependencies
     EXCEPTIONS          - Buoy.jar
                         - Buoyx.jar
                         - classes.jar
                         - helpgui-1.1b.jar
                         - jhelpaction.jar
     The libraries that could not be replaced have been moved to the ./default-lib folder.

	 
ANALYSIS
--------
The project can be compiled, executed and analysed via the ant build file exercise.xml.
Important ant targets:
        clean                   - Deletes all data that can be regenerated via compiling or execution.
	compile                 - Compiles the project.
        generate-tests          - This target replaces the currently existing generated test suite with a newly generated one. Warning: Can take a long time.
	compile-builtin-tests   - Depends on the compile target and additionally compiles the built-in tests.
        compile-generated-tests - Depends on the compile target and compiles the generated test suite.
        
	builtin-test            - Depends on compile-builtin-tests. This target executes all builtin tests and creates coverage metrics. Test results will be stored in the "testreport" directory, the coverage metrics in the "coveragemetrics" directory.
	generated-tests         - Depends on compile-generated-tests and execute the generated test suite.


TEST SUITE GENERATION
---------------------
The generation time of the current test suite was 2300 minutes.
