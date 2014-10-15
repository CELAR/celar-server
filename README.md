CELAR Server
============

What is it?
----------
CELAR Server is the endpoint of the CELAR Platform. It is located inside the cloud provider, and it is responsible to receive application descriptions,
launch new deployments, store deployment-related data and provide an API for interaction between the platform and the clients.

Licensing
---------
This project is under the Apache License v2.0. For further details please 
see the LICENSE file.


Installation
------------

### Download
Tested for CentOS 6.4

Create a new repo file under /etc/yum.repos.d/ (e.g. CELAR.repo) with the following content:

    [CELAR-snapshots]
    name=CELAR-snapshots
    baseurl=http://snf-175960.vm.okeanos.grnet.gr/nexus/content/repositories/snapshots
    enabled=1
    protect=0
    gpgcheck=0
    metadata_expire=30s
    autorefresh=1
    type=rpm-md

Then you execute (as root):

    yum update && yum install celar-server-rpm

CELAR Server must be successfully installed! To verify it, execute

    service celar-server status

If everyting OK you must see the following message.

    CELAR Server running (process id)



### Configuration
In order to use CELAR Server, you must first obtain and deploy a working SlipStream Server copy. For more details on how to do this, please visit <https://github.com/slipstream/SlipStreamServer>. You must then register a new SlipStream user and connect the account to an IaaS provider.

After that, visit the CELAR Server and edit the following file:

    vi /opt/celar/celar-server/conf/slipstream.properties

and place the appropriate username, password. The CELAR Server must be deployed in the same VM as the SlipStream Server. Finally, execute

    service celar-server restart

and the Server is ready for use!

Contact
-------
Giannis Giannakopoulos, <ggian@cslab.ece.ntua.gr>,

Nikos Papailiou,	<npapa@cslab.ece.ntua.gr>,

Christos Mantas,	<cmantas@cslab.ece.ntua.gr>
