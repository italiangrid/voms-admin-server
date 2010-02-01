Summary: org.glite.security.voms-admin-server v. ${server-version}-${age}
Name: glite-security-voms-admin-server
Version: ${server-version}
Release: ${age}
License: egee
Vendor: EGEE
Group: System/Middleware
Packager: ETICS
Prefix: /opt/glite
BuildArch: noarch
BuildRoot: %{_builddir}/%{name}-%{version}
AutoReqProv: yes
Source0: glite-security-voms-admin-server-${server-version}-${age}.tar.gz

%define debug_package %{nil}

%description
This package contains the VOMS-Admin server application and configuration tools.

%prep

%setup -c

%build
  
%install

%clean

%files
%defattr(-,root,root)
%dir %{prefix}/etc
%attr(0755,root,root) %{prefix}/etc/init.d/*
%attr(0755,root,root) %{prefix}/sbin/*
%dir %{prefix}/share/webapps
%dir %{prefix}/share/java
%dir %{prefix}/share/voms-admin/tools
%dir %{prefix}/share/doc/glite-security-voms-admin-server
%dir %{prefix}/share/interface
%dir %{prefix}/share/voms-admin/endorsed

%pre

if [ -d "%{prefix}/share/voms-admin/tools/lib" ]; then
	rm -rf %{prefix}/share/voms-admin/tools/lib/*.jar
fi

exit 0

%preun

echo "Stopping potentially running voms-admin instances..."
%{prefix}/sbin/init-voms-admin.py stop
exit 0

