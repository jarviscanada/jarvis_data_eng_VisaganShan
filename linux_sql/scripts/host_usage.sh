#!/bin/sh

# Setup and validate arguments
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

# save current machine hostname to variables
hostname=$(hostname -f)

# Retrieveces hardware specification variables
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}' | xargs)
cpu_idle=$(vmstat | awk 'NR==3 {print $15}' | xargs)
cpu_kernel=$(top -bn1 | awk '/Cpu/ {print $6}' | xargs)
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}' | xargs)
disk_available=$(df -BM / | awk '/\// {print $4}' | sed 's/M//' | xargs)

# Current time formatted
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Subquery to find matching id in host info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

# Inserts server usage data into host_usage table
insert_stmt="INSERT INTO host_usage (\"timestamp\", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) 
VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available)";

# Set up env var for psql cmd
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
