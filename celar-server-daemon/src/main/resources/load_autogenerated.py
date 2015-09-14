#!/usr/bin/python
__author__ = 'cmantas'

from math import pow
from sys import argv
import psycopg2
from os.path import dirname, realpath

flavors = []
images = []
ram_sizes = []
for ram_exp in range(9,14):ram_sizes.append(int(pow(2, ram_exp)))
dik_sizes = [5, 10 , 20, 40, 80, 100]
cpu_sizes = [1, 2, 4, 8]
mypath = dirname(realpath(__file__))


def generate_okeanos():
    # genarate flavors
    for c in cpu_sizes:
        for r in ram_sizes:
            for d in dik_sizes:
                id = "C%dR%dD%dext_vlmc" % (c, r, d)
                flavors.append({'id': id, 'vcpus': c, 'ram': r, 'disk': d })
    for line in open(mypath+"/okeanos_images.txt", "r"):
        i = line.find(" ")
        images.append([line[:i], line[i:].strip()])

def generate_flexiant():
    # genarate flavors
    for c in cpu_sizes:
        for r in ram_sizes:
            for d in dik_sizes:
                id = "vcpus:{0} ram:{1} disk:{2}".format(c, r, d)
                flavors.append({'id': id, 'vcpus': c, 'ram': r, 'disk': d })
    for line in open(mypath+"/flexiant_images.txt", "r"):
        i = line.find(" ")
        images.append([line[:i], line[i:].strip()])



if __name__ == "__main__":

    if len(argv)<3:
        print "Please define a host"
        exit()
    host=argv[2]
    iaas= argv[1]
    print "Using host: "+host+", iaas:"+iaas

    # chose the right IaaS
    if iaas=="okeanos":generate_okeanos()
    elif iaas=="flexiant": generate_flexiant()
    else: print "Unknown IaaS"; exit(-1)


    # connect to db

    db = psycopg2.connect(host=host, user="celaruser", password="celar-user", database="celardb")
    cursor = db.cursor()


    #clear all old values from the DB
    cursor.execute("DELETE FROM RESOURCE_TYPE WHERE TRUE")
    cursor.execute("DELETE FROM SPECS WHERE TRUE")
    cursor.execute("DELETE FROM PROVIDED_RESOURCE WHERE TRUE")


    # add 'VM_FLAVOR' entry on the RESOURCE_TYPE table
    cursor.execute("INSERT INTO RESOURCE_TYPE VALUES (DEFAULT, 'VM_FLAVOR') returning id")
    type_table_id = cursor.fetchone()[0]

    # itreate through all available flavors and insert data in the DB
    for flav in flavors:
        flav_id = flav['id']
        ram= flav['ram'];cores= flav['vcpus']; disk=flav['disk']

        # #insert flavors into  PROVIDED_RESOURCE table
        cursor.execute("INSERT INTO PROVIDED_RESOURCE VALUES (DEFAULT , %d, '%s') returning id" % (type_table_id, flav_id))
        resources_table_id = cursor.fetchone()[0]

        #insert into SPEC Description table
        cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id,  "cores", cores ))
        cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id, "ram", ram ))
        cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id, "disk", disk ))


    # add 'VM_IMAGE' entry on the RESOURCE_TYPE table
    cursor.execute("INSERT INTO RESOURCE_TYPE VALUES (DEFAULT, 'VM_IMAGE')  returning id"  )
    type_table_id = cursor.fetchone()[0]
    # itreate through all available images and insert data in the DB
    for img in images:
        # #insert into  PROVIDED_RESOURCE table
        cursor.execute("INSERT INTO PROVIDED_RESOURCE VALUES (DEFAULT , %d, '%s') returning id" % ( type_table_id, img[0]))
        resources_table_id = cursor.fetchone()[0]
        #insert into SPEC Description table
        cursor.execute("""INSERT INTO SPECS VALUES (DEFAULT, %d, '%s','%s' ); """ %  ( resources_table_id,  "name", img[1] ))


    #commit insertions
    db.commit()
    #

    # Lookup everything


    cursor.execute("SELECT * FROM RESOURCE_TYPE")
    # get the number of rows in the resultset
    numrows = int(cursor.rowcount)
    print "Added "+str(numrows) + " Resource Types"

    cursor.execute("SELECT * FROM PROVIDED_RESOURCE")
    numrows = int(cursor.rowcount)
    print "Added "+str(numrows) + " Provided Resources"

    #select all provided resources
    cursor.execute("SELECT * FROM SPECS")
    numrows = int(cursor.rowcount)
    print "Added "+str(numrows) + " Specs"

    #close the cursor
    cursor.close()