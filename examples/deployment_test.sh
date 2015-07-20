if [ -z "$1" ]
  then
    echo "No file given to test"
    exit
fi

echo Putting application
curl http://localhost:8080/application/describe/ --data-binary @$1 -X POST -H "Content-Type: application/octet-stream" -ik > test_app.xml 2>/dev/null
app_id=$(cat test_app.xml | grep -m1 application_Id)
# remove postfix/suffix
app_id=${app_id%</*>}
app_id=${app_id#*Id>}
echo Got application id: $app_id


################ test deployment  ###########################
# Deploy application
echo Deploying
curl http://localhost:8080/application/$app_id/deploy/ \
	--data-binary @$1 -X POST -H "Content-Type: application/octet-stream" -ik \
	> test_deployment.xml 2>/dev/null
#this keeps the las occurence of <id>. -Maybe wrong
dep_id=$(tac test_deployment.xml | grep "<id>" -m1)
# remove postfix/suffix
dep_id=${dep_id%</*>}
dep_id=${dep_id#*>}
echo Got deployment ID: $dep_id


echo Waiting for state to change
state="mix"
while [ "$state" = "mix" ]; do
	echo -n "."
	state=$(curl http://localhost:8080/deployment/$dep_id 2>/dev/null | grep "<state")
	state=${state%</*>}
	state=${state#*>}
	sleep 5
done

echo \nState is now $state

curl http://localhost:8080/deployment/$dep_id/terminate
