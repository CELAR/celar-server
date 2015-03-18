################ test resources #######################
img_count=$(curl http://localhost:8080/resources/images 2>/dev/null |  grep \<id\> | wc -l)
echo Got $img_count images
flav_count=$(curl http://localhost:8080/resources/flavors 2>/dev/null |  grep \<id\> | wc -l)
echo Got $flav_count flavors

################  test application ####################
echo Putting application
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

################ test deployment   ###########################
# Create a dummy deployment (offline)
dep_id=$RANDOM
dep_id=$(echo -e "$dep_id" | tr -d '[[:space:]]') #trim whitespaces (idk why this is needed)
echo "insert into deployment(id, application_id, orchestrator_ip) values ('$dep_id', '$app_id' , '1.2.3.4');" | psql celardb celaruser >/dev/null

#retreive a module and a component id (offline)
module_id=$(echo "select id from module where application_id='$app_id' limit 1;" | psql celardb celaruser -tq)
module_id=$(echo -e "$module_id" | tr -d '[[:space:]]') #trim whitespaces (idk why this is needed)
component_id=$(echo "select component.id from component join module on component.module_id=module.id where module.id=$module_id limit 1;" | psql celardb celaruser -tq)
component_id=$(echo -e "$component_id" | tr -d '[[:space:]]') #trim whitespaces (idk why this is needed)

#create a resizing action for those (offline)
ra_id=$(echo "insert into resizing_action values (default, $module_id, $component_id, 'ADD') returning id;" | psql celardb celaruser -tq)

#create a decision for that resizing action
echo Putting a Decision
des_id=$(curl http://localhost:8080/deployment/$dep_id/component/$component_id/decision/add  2>/dev/null| grep "<id>")
des_id=${des_id%</*>}
des_id=${des_id#*id>}
echo Got decision with id: $des_id

#retreive all decisions for the deployment
echo Getting decisions for deployment\($dep_id\)
decision_count=$(curl http://localhost:8080/deployment/$dep_id/decisions?action_name=ADD 2>/dev/null| grep "<id>" | wc -l)
echo Got $decision_count decisions

# Creating a dummy resource (offline)
res_id=$(echo "insert into resources (deployment_id, provided_resource_id, start_time) values ('$dep_id', 1, now())  returning id;" | psql celardb celaruser -tq)
echo Getting resources for deployment
res_count=$(curl http://localhost:8080/deployment/$dep_id/resources 2>/dev/null | grep \<id\> | wc -l)
echo Got $res_count resources



################ test dependencies ######################
echo Creating and getting dummy dependencies
echo "insert into component_dependency values ($component_id, $component_id, 'dummy_myself') ;" | psql celardb celaruser -qt
comp_dep_count=$(curl http://localhost:8080/application/component/$component_id/dependencies 2>/dev/null | grep \<componentDependency\> | wc -l)
echo Got $comp_dep_count Component dependencies
echo "insert into module_dependency values ($module_id, $module_id, 'dummy_myself') ;" | psql celardb celaruser -qt
echo "insert into module_dependency values ($module_id, $module_id, 'dummy_myself_2') ;" | psql celardb celaruser -qt
mod_dep_count=$(curl http://localhost:8080/application/module/$module_id/dependencies 2>/dev/null | grep \<moduleDependency\> | wc -l)
echo Got $mod_dep_count Module dependencies


################ test metric value ###########################
echo Inserting Some dummy metric values

# create a couple of metric values (offline)
echo "insert into metric_values values (default, $metric_id, $res_id, now(), 12345);" | psql celardb celaruser -tq
echo "insert into metric_values values (default, $metric_id, $res_id, now(), 6789);" | psql celardb celaruser -tq

echo Getting metric values for metric\($metric_id\)
curl http://localhost:8080/metrics/$metric_id/values 2>/dev/null  >test_metric_values.xml
metric_values_count=$(cat test_metric_values.xml | grep id | wc -l)
echo Got $metric_values_count metric values





