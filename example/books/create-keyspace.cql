CREATE KEYSPACE IF NOT EXISTS books
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};