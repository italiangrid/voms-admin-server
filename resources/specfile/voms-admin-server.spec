Summary: gLite VOMS Admin service
Name: glite-security-voms-admin
Version: 2.5.1
Release: 1
License: Open Source EGEE License
Vendor: EGEE
Group: Unknown
Prefix: /opt/glite
BuildArch: noarch
AutoReqProv: yes
BuildRoot: %{_builddir}/%{name}-%{version}
Requires: PyXML
Requires: ZSI
Source0: glite-security-voms-admin-@MODULE.VERSION@-@MODULE.BUILD@.tar.gz


%define debug_package %{nil}

%description
This package provides a service for administering the gLite Virtual
Organization Membership Service.

%prep

%setup -c

%build
  
%install

%clean
  
%files 
%defattr(-,root,root)
%{prefix}/etc
%attr(0755,root,root) %{prefix}/etc/init.d/*
%attr(0755,root,root) %{prefix}/sbin/*
%{prefix}/share/webapps
%{prefix}/share/java
%{prefix}/share/voms-admin/tools
%attr(0755,root,root) %{prefix}/bin
%{prefix}/share/voms-admin/client
%{prefix}/share/interface
%{prefix}/share/doc
%{prefix}/share/voms-admin/endorsed

%pre

if [ -d "%{prefix}/share/voms-admin/tools/lib" ]; then
	rm -rf %{prefix}/share/voms-admin/tools/lib/*.jar
exit 0


%preun

echo "Stopping potentially running voms-admin instances..."
%{prefix}/sbin/init-voms-admin.py stop
exit 0