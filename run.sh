mvn package
CP=target/lib/jackson-core-2.10.3.jar:target/lib/jackson-databind-2.10.3.jar:target/maxmind-db-1.3.2-SNAPSHOT.jar:target/lib/jopt-simple-4.6.jar:target/lib/jmh-generator-annprocess-1.23.jar:target/lib/jmh-core-1.23.jar:target/lib/commons-math3-3.2.jar:target/test-classes

java -cp $CP org.openjdk.jmh.Main com.maxmind.db.BenchmarksSync > sync.txt
java -cp $CP org.openjdk.jmh.Main com.maxmind.db.BenchmarksNonSync > nonsync.txt

