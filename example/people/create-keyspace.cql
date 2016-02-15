CREATE KEYSPACE people
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};