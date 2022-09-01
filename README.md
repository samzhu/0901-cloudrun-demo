# Cloud Run 調整建議

1. Java 升級

2. SpringBoot 升級 & 套件增加
    - sleuth

3. 增加 DB 版控 liquibase

4. 增加 [Google Cloud's operations suite](https://cloud.google.com/products/operations)
    - trace
    - secretmanager
    - metrics
    - logging
      - 增加 src/main/resources/logback-spring.xml
      - 使用 profile gcp

5. 使用 buildpack

## Cloud Native Buildpacks

``` bash
./mvnw spring-boot:build-image
```

Ref:  

- [https://buildpacks.io/](https://buildpacks.io/)  
- [Maven packaging OCI Images](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)  

## Configure environment variables

``` bash
export REGION=asia-east1
export PROJECT_ID=das-ct-lab
export REPOSITORY_NAME=demo-0831
export REGISTRY_URI=$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY_NAME
```

## Create secrets

For config

``` bash
export SECRET_CONFIG_NAME=config-gcp

gcloud secrets create $SECRET_CONFIG_NAME --quiet --data-file=./config/application-gcp.yml || gcloud secrets versions add $SECRET_CONFIG_NAME --data-file=./config/application-gcp.yml
```

For DB password

``` bash
export SECRET_NAME=db_password
export DB_PASSWORD=pw123456

echo -n $DB_PASSWORD | \
  gcloud secrets create $SECRET_NAME --data-file=-

# OR
echo -n $DB_PASSWORD | \
  gcloud secrets versions add $SECRET_NAME --data-file=-
```

(Example output)

``` bash
Created version [1] of the secret [db_password].
```

這 SECRET_NAME 必須對應上 config/application-gcp.yml 的 ${sm://db_password}

## Deploy

``` bash
export REGION=asia-east1
export PROJECT_ID=das-ct-lab
export REPOSITORY_NAME=demo-sam
export REGISTRY_URI=$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY_NAME

export SERVICE_NAME=sam-demo-0831

export IMAGE_NAME=book
export IMAGE_TAG=0.0.3

export CPU=1
export MEMORY=1024M

export MIN_INSTANCES=0
export MAX_INSTANCES=1
export SERVICE_ACCOUNT_ID=serviceaccount-cloudrun
export VPC_ACCESS_NAME=vpc_access

export INSTANCE_CONNECTION_NAME=test-cloud-run-0324-1:asia-east1:quickstart-instance
export DB_DATABASE_NAME=testdb
export DB_USERNAME=dbuser
export SECRET_CONFIG_NAME=config-gcp
export SECRET_CONFIG_PATH=/workspace/config/application-gcp.yml

gcloud run deploy $SERVICE_NAME \
  --quiet \
  --project=$PROJECT_ID \
  --region=$REGION \
  --image=$REGISTRY_URI/$IMAGE_NAME:$IMAGE_TAG \
  --cpu=$CPU \
  --memory=$MEMORY \
  --min-instances=$MIN_INSTANCES \
  --max-instances=$MAX_INSTANCES \
  --service-account=$SERVICE_ACCOUNT_ID \
  --vpc-connector=$VPC_ACCESS_NAME \
  --set-env-vars="^@^spring.profiles.active=gcp" \
  --set-env-vars="spring.cloud.gcp.secretmanager.enabled=true" \
  --set-env-vars="sql_instance_connection_name=${INSTANCE_CONNECTION_NAME}" \
  --set-env-vars="sql_database_name=${DB_DATABASE_NAME}" \
  --set-env-vars="db_username=${DB_USERNAME}" \
  --set-secrets=$SECRET_CONFIG_PATH=$SECRET_CONFIG_NAME:latest \
  --allow-unauthenticated
```

























=======================================================================================

  --set-env-vars="^@^spring.config.location=classpath:,file:/config/" \


curl -o cloud_sql_proxy https://dl.google.com/cloudsql/cloud_sql_proxy.darwin.amd64
chmod +x cloud_sql_proxy
./cloud_sql_proxy -instances=das-ct-lab:asia-east1:sam-tset-0824=tcp:5432


./cloud_sql_proxy -instances=INSTANCE_CONNECTION_NAME=tcp:5432





CREATE TABLE public.delivery_company (
	id bigserial NOT NULL,
	name varchar NOT NULL,
	"label" varchar NULL,
	"level" integer NULL DEFAULT -1,
	CONSTRAINT delivery_company_pk PRIMARY KEY (id),
	CONSTRAINT delivery_company_un UNIQUE (name)
);

db_username=user1

sm
db_password=pw123456


