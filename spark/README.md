# Spark/Scala Project
## Introduction
After our initial data project helped London Gift Shop (LGS) boost customer engagement with targeted email campaigns, LGS decided to expand this approach across the entire company. However, my original solution using Jupyter Notebooks and Python was limited to smaller datasets on a single machine. To handle larger data volumes, I've re-implemented the solution using Apache Spark for distributed processing. We're also evaluating two platforms: Zeppelin on GCP and Databricks on Azure.

## Zeppelin and DataBricks Implementation
### Dataset and Analysis
 This implementation processes retail transaction data using Spark DataFrames and SQL queries on Databricks, enabling efficient ETL operations and generating analytics results for LGS.
### Architecture
Environment: Databricks on Azure.
Storage: Data resides in Azure Blob Storage, accessible through Databricks File System (DBFS).
Data Flow: Data is read from DBFS, transformed using Spark's Structured API, and saved back to DBFS or Azure storage.
Hive Metastore: Databricks integrates with the Hive Metastore for cataloging.
### Diagram

## Future Improvement
Implement Real-Time Analytics: Integrate streaming data pipelines with Spark Streaming for real-time customer insights.
Optimize Data Storage: Transition to a more optimized data format like Delta Lake for Databricks to improve data management and performance.
Automate Workflows: Set up automated ETL pipelines with tools like Azure Data Factory or Apache Airflow to streamline data processing.
