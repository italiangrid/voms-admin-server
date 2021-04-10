FROM centos:7

ENV VOMS_DB_USERNAME voms
ENV VOMS_DB_PASSWORD pwd
ENV VOMS_DB_NAME voms_test
ENV VOMS_MAIL_FROM voms-admin@dev.local.io
ENV VOMS_HOSTNAME dev.local.io

ADD setup /scripts
ADD setup/repo/*.repo /etc/yum.repos.d/

RUN chmod +x /scripts/run.sh 
RUN yum -y install sudo && sh /scripts/setup-voms-user.sh && sh /scripts/setup.sh

VOLUME /var/log/voms-admin
CMD /scripts/run.sh

EXPOSE 8443 1044
