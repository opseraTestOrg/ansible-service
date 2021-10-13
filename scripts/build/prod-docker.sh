#!/bin/bash
set -xe
docker build -t opsera-ansible-service:kube-prod  ../../
docker run --rm \
        440953937617.dkr.ecr.us-east-2.amazonaws.com/kubectl \
        aws ecr get-login-password \
        --region us-east-2 \
        | docker login --username AWS \
        --password-stdin 440953937617.dkr.ecr.us-east-2.amazonaws.com

date_tag=`date +%-d-%m-%Y-%T | sed 's/:/-/g'`
docker tag opsera-ansible-service:kube-prod 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-ansible-service:kube-prod
docker tag opsera-ansible-service:kube-prod 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-ansible-service:kube-prod-${date_tag}

docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-ansible-service:kube-prod
docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-ansible-service:kube-prod-${date_tag}
