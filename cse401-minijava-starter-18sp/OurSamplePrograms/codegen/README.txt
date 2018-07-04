To use boot.c to create an executable:

FILENAME.s is the output from running

java -cp build/classes:lib/java-cup-11b.jar MiniJava FILENAME.java

gcc -std=c11 src/runtime/boot.c src/runtime/number_converter.c FILENAME.s -lm -o EXECUTABLENAME
