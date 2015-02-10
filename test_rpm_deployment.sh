export BUILD_NUMBER=1000
rm celar-server-rpm/target/rpm/celar-server-rpm/RPMS/noarch/celar-server-rpm-0.0.1-1000.el6.noarch.rpm
echo Packaging
mvn clean package >/dev/null
echo Sending
scp celar-server-rpm/target/rpm/celar-server-rpm/RPMS/noarch/celar-server-rpm-0.0.1-1000.el6.noarch.rpm celar-server: &>/dev/null
echo Uninstalling
ssh celar-server "yum remove celar-server-rpm.noarch -y" >/dev/null
echo Installing
ssh celar-server "rm -f /tmp/celar-server.log*; rpm -ivh celar-server-rpm-0.0.1-1000.el6.noarch.rpm" 
echo END
