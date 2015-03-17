echo Putting application


################  test application ####################
curl http://localhost:8080/application/describe/ --data-binary @test1.csar -X POST -H "Content-Type: application/octet-stream" -ik > test_app.xml 2>/dev/null
app_id=$(cat test_app.xml | grep application_Id)
# remove postfix/suffix
app_id=${app_id%</*>}
app_id=${app_id#*Id>}

echo Got application id: $app_id


################# test metric ########################
#create a dummy xml file for metric
echo '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<metric>
    <name>my metric</name>
    <component_ID>1</component_ID>
</metric>' > metric_tmp.xml

echo Putting Metric
#put metric and get id
curl http://localhost:8080/metrics/put/ --data-binary @metric_tmp.xml -X POST -H "Content-Type: application/octet-stream" -ik >test_metric.xml 2>/dev/null
rm -f metric_tmp.xml
metric_id=$(cat test_metric.xml | grep id)
metric_id=${metric_id%</*>}
metric_id=${metric_id#*id>}
echo Got metric id: $metric_id


################ test metric value ###########################
echo Inserting Some dummy data
# Creating a dummy deployment (offline)
echo "insert into deployment(id, application_id, orchestrator_ip) values ('1345678', '$app_id' , '1.2.3.4');" | psql celardb celaruser >/dev/null

# Creating a dummy resource (offline)
echo "insert into resources (deployment_id, provided_resource_id, start_time) values ('1345678', 1, now());" | psql celardb celaruser >/dev/null

# create a couple of metric values (offline)
echo "insert into metric_values values (default, $metric_id, 1, now(), 12345);" | psql celardb celaruser >/dev/null
echo "insert into metric_values values (default, $metric_id, 1, now(), 6789);" | psql celardb celaruser >/dev/null

echo Getting metric values for metric: $metric_id
curl http://localhost:8080/metrics/$metric_id/values 2>/dev/null  >test_metric_values.xml
metric_values_count=$(cat test_metric_values.xml | grep id | wc -l)
echo Got $metric_values_count metric values