This is an Android project sample to demonstrate saving data in a sqlite database, then extracting that databasefrom the command line, storing it in another project, and using the stored database in that project. This is useful for example if you have a bunch of data in a database (like a list of phone numbers) you want to ship with an application.  Instead of having a large XML file or something and doing individual INSERTs into the database per row, you can instead do those INSERTs in a separate project, then extract the database as a binary file from the emulator, store that file in your real project, and copy it from the project's resources into the application's data directory, and start using the database.

Copyright 2012 Brian Johnson

Brian Johnson
codebybrian.com
brian@codebybrian.com
