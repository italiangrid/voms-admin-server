Summary: VOMS Admin server v. ${server-version}-${age}
Name: voms-admin-server
Version: ${server-version}
Release: ${age}
License: ASL 2.0
Vendor: EMI
Packager: ETICS
Prefix: /
BuildArch: noarch
BuildRoot: %{_builddir}/%{name}-%{version}
AutoReqProv: yes
Source0: glite-security-voms-admin-server-${server-version}-${age}.tar.gz

%define debug_package %{nil}

%description
This package contains the VOMS Admin server application and configuration tools.

%prep

%setup -c

%build
  
%install

%clean

%files
%defattr(-,root,root)
%dir %{prefix}/etc
%attr(0755,root,root) /etc/rc.d/init.d/voms-admin
%attr(0755,root,root) /usr/share/sbin/voms-admin-configure
%attr(0755,root,root) /usr/share/sbin/voms-admin-ping
%attr(0755,root,root) /usr/share/sbin/*.py

/usr/share/webapps/*.war
/usr/share/java/*.jar
/usr/share/voms-admin/tools/classes/*.properties
/usr/share/voms-admin/tools/lib/*.jar
/usr/share/doc/voms-admin/*
/usr/share/voms-admin/wsdls/*.wsdl
/usr/share/voms-admin/endorsed/*.jar
/usr/share/voms-admin/templates/*
/usr/share/voms-admin/templates/aup/*

%pre

if [ -d "/usr/share/voms-admin/tools/lib" ]; then
	rm -rf /usr/share/voms-admin/tools/lib/*.jar
fi

exit 0

%preun
echo "Stopping potentially running voms-admin instances..."
/etc/rc.d/init.d/voms-admin stop
exit 0

