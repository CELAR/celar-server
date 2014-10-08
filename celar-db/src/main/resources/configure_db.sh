#trust local users
sed -i "s/local   all         all                               ident/local   all         all                               trust/g" -i /var/lib/pgsql/data/pg_hba.conf

#restart to apply changes
service postgresql restart


# drop and re-create the DB
echo "DROP DATABASE celardb;
    CREATE DATABASE celardb;
" | psql -U celaruser postgres >/dev/null

echo "use password celar-user if asked
"
psql -U celaruser -d celardb -a -f celardb.psql >/dev/null
