FROM italiangrid/base-dev

RUN sed -i 's/socket_timeout=3/socket_timeout=1/' /etc/yum/pluginconf.d/fastestmirror.conf
RUN echo "include_only=.garr.it,.cern.ch" >> /etc/yum/pluginconf.d/fastestmirror.conf

ADD setup /

RUN yum -y -d2 install sudo && sh setup-voms-user.sh

RUN /bin/bash setup.sh 

USER voms
WORKDIR /home/voms
CMD /bin/bash /run.sh 
EXPOSE 15000 
