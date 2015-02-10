#this script load the database to mysql in order to convert it
#use with care or find an alternative

#load sql script to mysql
echo "drop database celardb;" | mysql -u root 
mysql -u root < celar_db.sql

#export the db  to a "compatible" sql script
mysqldump -u root celardb --compatible=postgresql > celardb_compatible.sql

#convert it to a psql script and remove
python db_converter.py celardb_compatible.sql celardb.psql
rm celardb_compatible.sql

#convert a data type
sed -i "s/bytea/text/g" celardb.psql 
