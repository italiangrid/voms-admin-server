name=voms-admin-server
spec=spec/$(name).spec
oracle_location=/usr/lib/oracle/10.2.0.4
version=$(shell grep "Version:" $(spec) | sed -e "s/Version://g" -e "s/[ \t]*//g")
release=1
rpmbuild_dir=$(shell pwd)/rpmbuild

.PHONY: etics clean rpm

all: 	rpm

clean:	
		rm -rf target $(rpmbuild_dir) tgz RPMS 

rpm:	
		mvn -B -s src/config/emi-build-settings.xml package
		mkdir -p 	$(rpmbuild_dir)/BUILD $(rpmbuild_dir)/RPMS \
					$(rpmbuild_dir)/SOURCES $(rpmbuild_dir)/SPECS \
					$(rpmbuild_dir)/SRPMS

		cp target/$(name)-$(version).src.tar.gz $(rpmbuild_dir)/SOURCES/$(name)-$(version).tar.gz
		rpmbuild --nodeps -v -ba $(spec) --define "_topdir $(rpmbuild_dir)" \
			--define "oracle_location $(oracle_location)"

etics: 	clean rpm
		mkdir -p tgz RPMS
		cp target/*.tar.gz tgz
		cp -r $(rpmbuild_dir)/RPMS/* $(rpmbuild_dir)/SRPMS/* RPMS
