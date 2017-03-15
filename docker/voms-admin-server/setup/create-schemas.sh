#!/bin/bash
set -x

VO_NAME_PREFIX=${VOMS_VO_NAME_PREFIX:-"test"}
VO_COUNT=${VOMS_VO_COUNT:-1}

create_vo_schema(){
  
  tmp_file=$(mktemp /tmp/create-schema-$1-XXX.sql)

  cat << EOF > ${tmp_file}
  CREATE DATABASE IF NOT EXISTS test_$1;
  GRANT ALL ON test_$1.* TO '${VOMS_DB_USERNAME}'@'%' IDENTIFIED BY '${VOMS_DB_PASSWORD}';
EOF

  cat ${tmp_file}
  cat ${tmp_file} | mysql -hdb -uroot -p${MYSQL_ROOT_PASSWORD}
}


for i in $(seq 0 ${VO_COUNT}); do
  create_vo_schema $i;
done
