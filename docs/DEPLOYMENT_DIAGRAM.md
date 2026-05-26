 # Deployment Diagram (EC2 + Docker)

 ```plantuml
 @startuml
 !define AWSPUML https://raw.githubusercontent.com/awslabs/aws-icons-for-plantuml/v14.0/LATEST/AWSPUML
 !includeurl AWSPUML

 skinparam componentStyle rectangle

 node "AWS Account" {
   frame "VPC" {
     cloud "Internet" as Internet

     node "EC2 Instance (t3.medium)" as EC2 {
       component "Docker Engine" as Docker
       container "nginx (frontend)" as NGINX
       container "backend (Spring Boot)" as BACKEND
       container "postgres:15" as POSTGRES
       container "pgAdmin (optional)" as PGADMIN
     }

     Internet --> EC2 : HTTP/HTTPS (Elastic IP)
   }
 }

 BACKEND --> POSTGRES : JDBC
 NGINX --> BACKEND : /api proxied
 NGINX --> Internet : serve SPA

 note right of EC2
 - Docker Compose orchestrates containers
 - Elastic IP attached to EC2 for static IP
 - Optional: Certbot + nginx for HTTPS
 - Use systemd unit to start docker-compose on boot
 end note

 @enduml
 ```

 Short instructions:
 - Provision EC2, allocate Elastic IP, open ports 80/443/22 (and 5050 if using pgAdmin).
 - Install Docker & docker-compose, clone repo, create `.env`, run `docker-compose up -d`.
 - Attach Elastic IP to the EC2 instance to keep a stable public IP.
