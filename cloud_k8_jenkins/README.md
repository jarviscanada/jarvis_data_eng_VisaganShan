# Cloud & DevOps Project

## Introduction
This project showcases the migration of a Java-based trading application from on-premises servers to Azure Cloud, ensuring scalability, reliability, and efficient traffic management. To handle varying demand, the system leverages a load balancer and auto-scaling capabilities. Trading data is securely stored in a PostgreSQL database, while development and testing are streamlined using a local Kubernetes cluster via Minikube or Docker Desktop. For production deployment, the application runs on Azure Kubernetes Service (AKS) for high availability. Traffic is efficiently managed by Azure Load Balancer, and Docker images are securely stored in Azure Container Registry (ACR). To ensure a seamless and automated development process, Jenkins handles building, testing, and deployment, enhancing both efficiency and reliability.

## Application Architecture
The application architecture consists of a Java server that handles trading-related requests and connects to a PostgreSQL database for storage. Traffic is routed through an Azure Load Balancer, which ensures even distribution across multiple application pods. These pods are managed within an AKS cluster, which uses auto-scaling to maintain performance during varying traffic loads. The architecture also integrates Kubernetes services and secrets to handle inter-pod communication and secure connections to external systems like the database. The development environment uses a lightweight Kubernetes setup for ease of iteration, while the production environment is optimized for scalability and reliability, leveraging Azure's managed services.

## Jenkins CI/CD pipeline
The Jenkins CI/CD pipeline automates the application delivery process to minimize manual intervention and ensure consistency. The pipeline begins with the git clone stage, where the latest changes are fetched from the Git repository. In the build stage, Azure CLI and Docker are used to create a container image of the application, tag it using Jenkins' BUILD_NUMBER, and push it to Azure Container Registry (ACR). The test stage verifies the application by running unit tests and confirming the Kubernetes cluster's readiness. In the deploy stage, the pipeline connects to the AKS cluster using kubectl and updates the deployment to reference the newly built image. Once deployment is complete, the pipeline validates the application by listing all cluster resources. This CI/CD pipeline ensures seamless transitions from development to production.

## Improvements

Database Optimization: Implement database indexing and query optimization for better performance and faster data retrieval.
Pod Autoscaling Improvements: Fine-tune Kubernetes vertical/horizontal auto-scaling policies to optimize resource utilization and cost efficiency.
Multi-Cloud Support: Expand deployment options to support AWS EKS or Google Kubernetes Engine (GKE) for greater flexibility.
