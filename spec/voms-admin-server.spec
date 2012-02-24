## Turn off meaningless jar repackaging 
%define __jar_repack 0

Name: voms-admin-server
Version: 2.7.0
Release: 1%{?dist}
Summary: The VOMS Administration service

Group:		Applications/Internet
License:    ASL 2.0
URL: https://twiki.cnaf.infn.it/twiki/bin/view/VOMS
Source:  %{name}-%{version}.tar.gz
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-root-%(%{__id_u} -n)

BuildArch:      noarch

BuildRequires:  maven
BuildRequires:  jpackage-utils
BuildRequires:  java-devel
BuildRequires:  oracle-instantclient-basic

Requires: emi-trustmanager
Requires: bouncycastle >= 1.39
Requires: tomcat%{?tomcat_version}
Requires: java

%description
The Virtual Organization Membership Service (VOMS) is an attribute authority
which serves as central repository for VO user authorization information,
providing support for sorting users into group hierarchies, keeping track of
their roles and other attributes in order to issue trusted attribute
certificates and SAML assertions used in the Grid environment for
authorization purposes.


The VOMS Admin service is a web application providing tools for administering
the VOMS VO structure. It provides an intuitive web user interface for daily
administration tasks.

%prep
%setup -q -n voms-admin

%build
mvn -B -s src/config/cnaf-build-settings.xml package

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT
tar -C $RPM_BUILD_ROOT -xvzf target/%{name}-%{version}.tar.gz

mkdir -p $RPM_BUILD_ROOT%{_sysconfdir}/voms-admin

# Stage oracle jar
cp `find /usr/lib/oracle/ -name ojdbc14.jar` $RPM_BUILD_ROOT%{_datadir}/voms-admin/tools/lib

%clean
rm -rf $RPM_BUILD_ROOT

%pre
if [ -d "%{_datadir}/voms-admin/tools/lib" ]; then
	rm -rf %{_datadir}/voms-admin/tools/lib/*.jar
fi
exit 0

%preun
echo "Stopping potentially running voms-admin instances..."
%{_initrddir}/voms-admin stop
exit 0


%files

%defattr(-,root,root,-)

%{_initrddir}/voms-admin

%dir %{_sysconfdir}/voms-admin
%config(noreplace) %{_sysconfdir}/sysconfig/voms-admin

%{_sbindir}/*

%{_javadir}/glite-security-voms-admin.jar
%{_datadir}/webapps/glite-security-voms-admin.war
%{_datadir}/webapps/glite-security-voms-siblings.war

%doc resources/doc/*

%{_datadir}/voms-admin/*

%changelog

* Fri Dec 16 2011 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 2.7.0-1
- Self-managed packaging
