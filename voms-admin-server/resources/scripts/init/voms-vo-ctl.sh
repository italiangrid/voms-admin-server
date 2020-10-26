#!/bin/bash
if [ -n "${VERBOSE}" ]; then
  set -x
fi
## return value
RETVAL=0

conf_dir="/etc/voms-admin/"
deploy_dir="/var/lib/voms-admin/vo.d"
configured_vos=$(find ${conf_dir} -maxdepth 1 -mindepth 1 -type d -exec basename {} \;)

deploy_vos() {
  vos=$1

  if [ -z "$vos" ]; then
    vos=$configured_vos
  fi

  for vo in $vos; do
    if [ $# -gt 1 ]; then
      echo -n "Deploying vo($vo): "
    fi
    check_vo_name "$vo"
    if [ -f $deploy_dir/$vo ]; then
      if [ $# -gt 1 ]; then
        success "already deployed"
      fi
    else
      touch $deploy_dir/$vo
      if [ $# -gt 1 ]; then
        success
      fi
    fi
  done
}

undeploy_vos() {
  vos=$1

  if [ -z "$vos" ]; then
    vos=$configured_vos
  fi

  for vo in $vos; do
    if [ $# -gt 1 ]; then
      echo -n "Undeploying vo($vo): "
    fi
    if [ -f $deploy_dir/$vo ]; then
      rm -f $deploy_dir/$vo
      if [ $# -gt 1 ]; then
        success
      fi
    else
      if [ $# -gt 1 ]; then
        success "was not deployed"
      fi
    fi
  done
}

check_vo_name() {

  for v in $configured_vos; do
    [ "$v" = "$1" ] && return 0
  done

  failure "not configured"
  exit 1

}

deploy_dir_is_empty(){
  deployed_vos=`find $deploy_dir -maxdepth 1 -mindepth 1 -type f -exec basename {} \;`
  if [ -z "$deployed_vos" ]; then
    return 0;
  else
    return 1;
  fi
}

list(){

  if [ -z "$configured_vos" ]; then
    echo "no configured VO found"
    return 0;
  fi

  for v in ${configured_vos}; do
    echo -n "$v"
    if [ -f "${deploy_dir}/$v" ]; then
      echo " (deployed)"
    else
      echo " (not deployed)"
    fi
  done
}

init(){
  deploy_dir_is_empty
  if [ $? -eq 0 ]; then
    deploy_vos "$configured_vos" "verbose"
  else
    deployed_vos=`find $deploy_dir -maxdepth 1 -mindepth 1 -type f -exec basename {} \; | sort`
    echo "Deployed VOs:"
    for v in $deployed_vos; do
      echo "	$v" 
    done
  fi
  success
}

success()
{

  if [ ! -z "$1" ]; then
    echo -n "$1"
  fi

  RES_COL=60
  echo -en "\\033[${RES_COL}G"
  echo -n "[  "
  echo -en "\\033[1;32m"
  echo -n OK
  echo -en "\\033[0;39m"
  echo -n "  ]"
  echo -ne "\r"
  echo

  return 0
}


failure()
{
  rc=$?

  if [ ! -z "$1" ]; then
    echo -n "$1"
  fi

  RES_COL=60
  echo -en "\\033[${RES_COL}G"
  echo -n "["
  echo -en "\\033[1;31m"
  echo -n FAILED
  echo -en "\\033[0;39m"
  echo -n "]"
  echo -ne "\r"
  echo

  return $rc
}

case "$1" in
  list)
    list 
    ;;

  deploy)
    deploy_vos "$2" "verbose"
    ;;

  undeploy)
    undeploy_vos "$2" "verbose"
    ;;

  *)
    echo "Usage: $0 {list|deploy|undeploy}"
    RETVAL=1
    ;;
esac

exit $RETVAL
