# To use this spec file, copy the jar and the LICENSE.txt file to the SOURCES
# subdirectly of the rpmbuild directory. cd to the rpmbuild directory, and
# then issue
# rpmbuild --define "_topdir `pwd`" -bb SPECS/mustache4j-0.9.spec

# Unfortunately, even for noarch builds, a build on a 64-bit host uses lib64
# for _lib. Technically this is correct, as data should go in _datadir, which
# translates to /usr/share. However, it seems weird to put java libraries in
# there, even though they are technically data interpreted by the jvm.
%define libdir %{_exec_prefix}/lib

Summary: Java implementation of a Mustache template engine
Name: mustache4j
Version: 0.9.0
Release: 1
License: Apache
URL: http://github.com/cwestin/mustache4j
Source0: %{name}-%{version}.jar
Source1: LICENSE.txt
#BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}-root
BuildArch: noarch
Requires: java >= 1.6

%description
Another entry for the set from http://mustache.github.com/ .

For a manual, see http://mustache.github.com/mustache.5.html .


%prep
# there's nothing to unpack
# %setup -q


%build
# the java jar is pre-built, so there's nothing to do here


%install
rm -rf $RPM_BUILD_ROOT

# copy the unzipped distribution files to a reasonable place
%define sdir %{_sourcedir}
%define tdir %{buildroot}%{libdir}/%{name}/%{name}-%{version}
%__install -d %{tdir}
cp %{sdir}/%{name}-%{version}.jar %{tdir}
cp %{sdir}/LICENSE.txt %{tdir}


%clean
rm -rf $RPM_BUILD_ROOT


%files
%defattr(-,root,root,-)
%attr(444,root,root) %{libdir}/%{name}/%{name}-%{version}/LICENSE.txt
%attr(444,root,root) %{libdir}/%{name}/%{name}-%{version}/%{name}-%{version}.jar


%changelog
* Wed Oct 24 2012 Chris Westin <cwestin@fedora-16-jetty> - 0.9-1
- Initial build.
