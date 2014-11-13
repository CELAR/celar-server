#!/usr/bin/python	
__author__ = 'cmantas'

print "Loading resources"

import psycopg2
from kamaki.clients.astakos import AstakosClient
from kamaki.clients.cyclades import CycladesClient
from logging import getLogger, ERROR
from sys import argv

if len(argv)<2:
	print "Please define a host"
	exit()
HOST=argv[1]
print "Using host: "+HOST


# init synneffo  stuff

AUTHENTICATION_URL="https://accounts.okeanos.grnet.gr/identity/v2.0"
TOKEN="C3-Y2yBdIE--O3vhBkRqRseP8aY5zKKbHifuYaZImkM"
synnefo_user = AstakosClient(AUTHENTICATION_URL, TOKEN)
synnefo_user.logger.setLevel(ERROR)
getLogger().setLevel(ERROR)
cyclades_endpoints = synnefo_user.get_service_endpoints('compute')
CYCLADES_URL = cyclades_endpoints['publicURL']
cyclades_client = CycladesClient(CYCLADES_URL, TOKEN)

# connect to db

db = psycopg2.connect(host=HOST, user="celaruser", password="celar-user", database="celardb")
cursor = db.cursor()


#clear all old values from the DB
cursor.execute("DELETE FROM RESOURCE_TYPE WHERE TRUE")
cursor.execute("DELETE FROM SPECS WHERE TRUE")
cursor.execute("DELETE FROM PROVIDED_RESOURCE WHERE TRUE")


# add 'VM_FLAVOR' entry on the RESOURCE_TYPE table
cursor.execute("INSERT INTO RESOURCE_TYPE VALUES (1, 'VM_FLAVOR')")


# itreate through all available flavors and insert data in the DB
for flav in cyclades_client.list_flavors()[:10]:
    flav_id = flav['id']
    details = cyclades_client.get_flavor_details(flav_id)
    ram=details['ram']; cores=details['vcpus']; disk=details['disk']; name = details['name'];

    # #insert into  PROVIDED_RESOURCE table
    cursor.execute("INSERT INTO PROVIDED_RESOURCE VALUES (DEFAULT , 1, '%s') returning id" % (name))
    resources_table_id = cursor.fetchone()[0]

    #insert into SPEC Description table
    cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id,  "cores", cores ))
    cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id, "ram", ram ))
    cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id, "disk", disk ))


# add 'VM_IMAGE' entry on the RESOURCE_TYPE table
cursor.execute("INSERT INTO RESOURCE_TYPE VALUES (2, 'VM_IMAGE')"  )
# itreate through all available images and insert data in the DB
for img in cyclades_client.list_images()[:10]:
    # #insert into  PROVIDED_RESOURCE table
    cursor.execute("INSERT INTO PROVIDED_RESOURCE VALUES (DEFAULT , 2, '%s') returning id" % ( img['id']))
    resources_table_id = cursor.fetchone()[0]
    #insert into SPEC Description table
    cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id,  "name", img['name'] ))


#commit insertions
db.commit()
#

# Lookup everything


print "-------------------- RESOURCE TYPES ------------------------"
cursor.execute("SELECT * FROM RESOURCE_TYPE")
# get the number of rows in the resultset
numrows = int(cursor.rowcount)
# get and display one row at a time.
for x in range(0,numrows):
    row = cursor.fetchone()
    print row

print "-------------------- PROVIDED RESOURCES ------------------------"
cursor.execute("SELECT * FROM PROVIDED_RESOURCE")
# get the number of rows in the resultset
numrows = int(cursor.rowcount)
# get and display one row at a time.
for x in range(0,numrows):
    row = cursor.fetchone()
    print row

print "-------------------- SPECS ------------------------"
#select all provided resources
cursor.execute("SELECT * FROM SPECS")
# get the number of rows in the resultset
numrows = int(cursor.rowcount)
# get and display one row at a time.
for x in range(0,numrows):
    row = cursor.fetchone()
    print row

#close the cursor
cursor.close()

