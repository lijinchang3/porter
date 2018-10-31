#!/usr/bin/env sh

PRG="$0"
PRGDIR=`dirname "$PRG"`
cd "$PRGDIR/../"

locate_str=`pwd`
signal_id=`kill -l USR2`

node_pid=`jps -v |grep "ManagerClusterApplication"|grep "${locate_str}/"|awk '{printf  " "$1}'`

if [ -n "$node_pid" ]
then
    kill -${signal_id} ${node_pid}
	echo "[${node_pid}]manager-statistics-collector shutdown Successfully"
fi
