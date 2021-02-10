# CA1-SRS

## General info

This project uses an SQLite database to store all of the data, just because I wanted a challenge, so for that, there are a couple of libraries.
These are:
  - [ORMLite](https://ormlite.com)
  - [JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)
  - [JDBC SQLite driver](https://github.com/xerial/sqlite-jdbc)

Of course normally it would be foolish to store the database file in the Git Repo, howver, since this data is not very sensitive, we can get away with it.
Please assert that the file: `db.sqlite` is present in the root directory of the project, I.E: the one above the `src` directory.

## Normal building

To build, from the root directory, run:

`javac -classpath ormlite-core-5.3.jar:sqlite-jdbc-3.32.3.2.jar:ormlite-jdbc-5.3.jar src/models/*.java`

To run, from the root directory, use:

`java -classpath out/prod/StudentRecordSystem:ormlite-core-5.3.jar:sqlite-jdbc-3.32.3.2.jar:ormlite-jdbc-5.3.jar University`

## Building for submission

The spec dictates that the path `ecm1410/your student number/ca1/src/` should contain both the source, .java, and output, .class, files.

For that, again from the root directory, use the following commands:

Build: `javac -classpath ormlite-core-5.3.jar:sqlite-jdbc-3.32.3.2.jar:ormlite-jdbc-5.3.jar src/*`

Run: `java -classpath src:ormlite-core-5.3.jar:sqlite-jdbc-3.32.3.2.jar:ormlite-jdbc-5.3.jar University`
