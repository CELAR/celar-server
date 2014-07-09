
#trust local users
sed -i "s/local   all         all                               ident/local   all         all                               trust/g" -i /var/lib/pgsql/data/pg_hba.conf

#restart to apply changes
service postgresql restart
