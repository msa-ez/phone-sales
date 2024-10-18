# 

## Model
www.msaez.io/#/487999/storming/commterminalsales

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- spec
- order
- marketing
- insurance
- payment
- salesdashboard
- customer


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- spec
```
 http :8088/specComparations id="id" productId="productId" options="options" customerId="customerId" 
 http :8088/specs id="id" spec="spec" phoneColor="phoneColor" phoneType="phoneType" image="image" manufacturer="manufacturer" 
```
- order
```
 http :8088/orders productId="1" customerId="jyjang@uengine.org" address="address" insuranceOption="insuranceOption" 
```
- marketing
```
 http :8088/retargettings id="id" customerId="customerId" productId="productId" returnCount="returnCount" 
```
- insurance
```
 http :8088/insuranceSubscriptions id="id" customerId="customerId" productId="productId" terminalProductId="terminalProductId" 
```
- payment
```
 http :8088/payments id="id" amount="amount" product="product" customerId="customerId" paymentMethod="paymentMethod" 
```
- salesdashboard
```
```
- customer
```
 http :8088/customers id="id" region="region" gender="gender" 
 http :8088/messages id="id" message="message" customerId="customerId" 
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

