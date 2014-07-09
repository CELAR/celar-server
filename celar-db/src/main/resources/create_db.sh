# drop and re-create the DB
echo "DROP DATABASE celardb;
	CREATE DATABASE celardb;
" > create.tmp
psql -U celaruser postgres <create.tmp
rm create.tmp

echo "use password celar-user
"
psql -U celaruser -d celardb -a -f celardb.psql
