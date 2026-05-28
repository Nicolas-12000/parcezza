 # Deployment Diagram (S3 frontend + EC2 backend)

 ```plantuml

 skinparam componentStyle rectangle

 node "AWS Account" {
   frame "VPC" {
     cloud "Internet" as Internet

     rectangle "S3 (Static Website)" as S3

     node "EC2 Instance (t3.medium)" as EC2 {
       component "Docker Engine" as Docker
       container "backend (Spring Boot)" as BACKEND
       container "postgres:15" as POSTGRES
     }

     Internet --> S3 : HTTP/HTTPS (CloudFront optional)
     Internet --> EC2 : HTTP/HTTPS (Elastic IP or ALB)
   }
 }

 BACKEND --> POSTGRES : JDBC
 S3 --> Internet : serve SPA
 Browser -> S3 : GET index.html + assets
 Browser -> BACKEND : API calls (CORS or same-origin via domain)

 note right of EC2
 - `docker-compose.yml` runs `backend` and `db` on EC2
 - `deploy.sh` and scripts/ contain helper deployment utilities
 - Frontend is built and deployed to S3 (no container on EC2)
 - Use CloudFront + ACM for HTTPS + CDN (recommended)
 - Consider RDS for managed Postgres in production
 end note

 @enduml
 ```

 Short instructions:
 - Create an S3 bucket for the frontend and enable static website hosting (or use CloudFront + S3 for HTTPS).
 - Build the frontend and `aws s3 sync` the `dist/` directory into the bucket.
 - On EC2: run Docker & `docker compose up -d` to start `backend` and `db` (compose no longer includes frontend).
 - Point your domain to CloudFront (or S3 website endpoint) for the frontend; point API subdomain to the EC2/ALB for the backend.
