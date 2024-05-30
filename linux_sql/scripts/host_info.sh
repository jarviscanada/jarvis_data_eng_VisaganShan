#!/bin/sh

# Capture CLI arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check number of args
if [ "$#" -ne 5 ]; then
	echo "Illegal number of parameters"
	exit 1
fi

# Save current machine hostname and CPU info to variables
hostname=$(hostname -f)
lscpu_out=$(lscpu)

# Retrieve hardware specification variables
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{for(i=3;i<=NF;i++) printf "%s ", $i}' | xargs)
cpu_mhz=$(cat /proc/cpuinfo | egrep "^cpu MHz" | awk 'NR==1 {print $4}')
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(cat /proc/meminfo | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Insert Data into PSQL
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem) VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem)";

# Set up environment variable for psql command (Why do we this**)
export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port  -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
