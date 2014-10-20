#!/bin/bash
set -ex
name=voms-admin-server

base_dir=../../
source_dir=sources
rpmbuild_dir=$(pwd)/rpmbuild

# spec file and its source
spec_src=${name}.spec.in
spec=${name}.spec

# determine the pom version and the rpm version
pom_version=$(grep "<version>" ${base_dir}/pom.xml | head -1 | sed -e 's/<version>//g' -e 's/<\/version>//g' -e "s/[ \t]*//g")
rpm_version=$(grep '%global base_version' ${spec_src} | awk '{ print $3 }')

# Python runtime dep
python=python26

# Oracle
oracle_path=/usr/lib64/oracle

# Oracle jar name
oracle_jar=ojdbc6.jar

# Cleanup
./clean.sh

## Prepare spec file
sed -e "s#@@POM_VERSION@@#${pom_version}#g"  \
  -e "s#@@PYTHON@@#${python}#g" \
  -e "s#@@ORACLE_PATH@@#${oracle_path}#g" \
  -e "s#@@ORACLE_JAR@@#${oracle_jar}#g" \
  ${spec_src} > ${spec}

## setup RPM build dir structure
mkdir -p ${rpmbuild_dir}/BUILD \
  ${rpmbuild_dir}/RPMS \
  ${rpmbuild_dir}/SOURCES \
  ${rpmbuild_dir}/SPECS \
  ${rpmbuild_dir}/SRPMS

## Prepare sources
## We cannot use tar --transform as the tar version in SL5 does not support it
source_tmp_dir=$(mktemp -d /tmp/${name}-rpm.XXXXX)

mkdir -p ${source_tmp_dir}/${name}
cp -r ${base_dir} ${source_tmp_dir}/${name}
pushd ${source_tmp_dir}
tar cvzf ${rpmbuild_dir}/SOURCES/${name}.tar.gz --exclude=".git" --exclude="${name}/package/*" --exclude="${name}/package" --exclude="${name}/target" .
popd

rm -rf ${source_tmp_dir}

rpm_build_command="rpmbuild --nodeps -v -ba ${spec} --define \"_topdir ${rpmbuild_dir}\""

[ -n "${dist}" ] && ${rpm_build_command}="${rpm_build_command} --define \"dist ${dist}\""
[ -n "${build_number}" ] && ${rpm_build_command}="${rpm_build_command} --define \"build_number ${build_number}\""

echo "RPM build command: ${rpm_build_command}"
eval ${rpm_build_command}
