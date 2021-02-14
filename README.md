# CA1-SRS

This project stores the data in CSV files, and loads them when the program starts.

These CSV files were exported directly from an SQLite DB that I had created for my original implementation, which used external libraries, therefore these files all include primary keys (pk) which act as references for each object.
This means that I can create simple references between objects based upon these pks, which made it much mich easier.

In order to manage the arrays of objects, I created a `Support` class, which can be created based upon an object, so all of the return values would be of the correct type.
