# Linux Cluster Monitoring Agent

## Introduction
The Linux Cluster Monitoring Agent is a project created to retrieve a computer/node's hardware specifications and monitor its resource usage. This monitoring agent caters to users who have a Linux cluster of nodes and would like to monitor a node's resource usage for future planning. Statistics given by the monitoring agent can benefit users through proactive response and data gathering which ultimately leads to optimized efficiency in performance and lower energy consumption in cluster systems.

In implementing this project, I have used technologies such as Bash, Docker, PostgreSQL, and Git and tested it using virtual machine instances running Rocky Linux on Google Cloud Platform(GCP).

## Quick Start
```bash
# To Create and run a PSQL instance
bash ./scripts/psql_docker.sh create <username> <password>

# To create tables using ddl.sql
export PGPASSWORD=<password>
psql -h localhost -U <username> -c "CREATE DATABASE host_agent;"
psql -h localhost -U <username> -d host_agent -f sql/ddl.sql

# To insert hardware specs data into the DB using host_info.sh
bash ./scripts/host_info.sh localhost 5432 host_agent <username> <password>

# Insert hardware usage data into the DB using host_usage.sh
bash ./scripts/host_usage.sh localhost 5432 host_agent <username> <password>

# Crontab Setup - execute crontab cmd and add the next command inside the crontab file
crontab -e
* * * * * bash /full/path/to/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

## Implementation
1. The process to implement the Linux Cluster Monitoring System began with using docker to create a PostgreSQL container. The container would later be used for operations relating to the management of hardware specifications and resource usage information.
2. Once I created a successful psql(PostgreSQL) container and volume to persist data, the next step was to create two tables using Postgres to store and manage hardware specification and usage information and automate the initialization process using a script (ddl.sql).
3. After completing the ddl.sql script, the project is now able to store information but it needs to be able to retrieve it. Using scripts, I automated this process to retrieve hardware specifications and resource usage information. Since the hardware specifications of a node are static, this process needs to be done only once. Resource usage, however, needs to be constantly recorded as it is constantly changing. To accomplish this task, two separate bash scripts were created to retrieve and store the values: host_info.sh stores the hardware specifications while host_usage.sh stores the hardware usage info.
4. The final step of implementation involved executing host_usage.sh on an interval of one minute to store the most recent hardware usage information. To accomplish this task, I used crontab to set up predefined intervals for the execution of this script, which ultimately allows users to receive constant updates on their hardware resource usage.

### Architecture
This diagram describes how a monitoring system operates in a cluster. Every node executes the bash script stored on their system and the data from the bash script is sent to the database. One node, in addition to executing the bash script, houses the psql instance to store the records. In order for data to be sent and stored in the psql instance, script execution outputs from nodes that don't house the psql instance must travel through a switch.

### Scripts
psql_docker.sh, This script used to create, start and stop a psql container.
```
./psql_docker.sh <create|start|stop> <username> <password>
```

host_info - This script is used to retrieve the hardware specifications of the node and store it in a database.
```
./host_info.sh <host> <port> <database> <username> <password>
```

host_usage - This script is used to retrieve the hardware resource usage of the node and store it in a database.
```
./host_info.sh <host> <port> <database> <username> <password>
```
crontab - This is a file that's used to execute commands at a predefined interval.
```
crontab -e
# Set interval at which to execute command, add the command you want to execute, and save and exit file.
```

### Database Modeling
The host agent database contains two tables: host_info and host_usage. Below is a schema that provides additional details on the table structures:

`host_info`:

| Properties | Description |
| --- | --- |
| id | An integer assigned uniquely to each host |
| hostname | The machine's distinct hostname |
| cpu_number | The count of CPUs present in the machine, represented as a positive integer |
| cpu_architecture | The architecture type of the machine's CPU |
| cpu_model | The model designation of the machine's CPU |
| cpu_mhz | The clock speed of the machine's CPU, expressed in MHz and expected to be a positive value |
| L2_cache | The size of the L2 cache in KB associated with the machine's CPU, denoted as a non-negative integer |
| total_mem | The total amount of memory available on the machine, measured in KB and assumed to be a positive integer |
| timestamp | The recorded time of data collection, presented in the UTC time zone |

`host_usage`:

| Properties | Description |
| --- | --- |
| timestamp | The recorded time of data collection, presented in the UTC time zone |
| host_id | 	The identification number corresponding to the host machine, which must be present in the id column of the host_info table. |
| memory_free | The quantity of available memory on the machine, measured in MB, and expected to be a positive integer. |
| cpu_idle | The percentage of CPU capacity that remains unused, constrained to values between 0 and 100 |
| cpu_kernel | The percentage of CPU capacity utilized by the kernel, restricted to values between 0 and 100 |
| disk_io | The count of disk input/output operations executed by the machine, specified as a non-negative integer |
| disk_available | The available disk space on the machine, presented in MB, and anticipated to be a non-negative integer |

## Test
The bash scripts were tested on a single virtual machine on Google Cloud Platform (GCP). Bash scripts were tested using the bash -x command to ensure all passed values were correct and all lines were properly executed. SQL queries and SQL files were tested manually through execution and then checking the database to ensure the commands executed correctly. Sample data as well as real-time data was used to test database functions.

## Deployments

## Improvements
Here are a few things I would like to improve on in the future:
1. Setting up checks and alerts for failed hardwares.
2. The ablility to check hardware changes once every reboot. At the moment, this is set to run just once, however it's possible that hardware and equipment can be ugraded over time to keep up to date.
3. Additional data points for temperatures of a node's major hardware components to assess component health. Example: Constant diagnostics on CPU and GPU temperatures.
4. The ability to determine if a hardware needs technician assessment to see if it needs it be replaced. This can be determined by noticing a regular pattern of decline in the performance of a piece of hardware. Example: Ex. Constant high CPU usage, temperatures, etc.
5. Easier to read format for data stored. Perhaps converting stored data into a .csv file and converting the .csv file into a graph. That way we have data records as well as graphs that can assess the trends of a node's resource usage.
