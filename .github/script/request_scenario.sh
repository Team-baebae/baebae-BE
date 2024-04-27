#!/bin/bash

# Function to sign the request
function Sign_Request() {
    local api_server=$1
    local api_url=$2
    local apicall_method=$3
    local ncloud_accesskey=$4
    local ncloud_secretkey=$5

    local unixtimestamp=$(echo $(($(date +%s%N)/1000000)))
    local message="${apicall_method} ${api_url}\n${unixtimestamp}\n${ncloud_accesskey}"

    local signature=$(echo -n -e "$message"|iconv -t utf8 |openssl dgst -sha256 -hmac "$ncloud_secretkey" -binary|openssl enc -base64)

    echo -e "x-ncp-apigw-timestamp:$unixtimestamp\nx-ncp-iam-access-key:$ncloud_accesskey\nx-ncp-apigw-signature-v2:$signature"
}

# Sample API server and URL
api_server="https://vpcsourcedeploy.apigw.ntruss.com"
api_url="/api/v1/project/<project_id>/stage/<stage_id>/scenario"
apicall_method="GET"
ncloud_accesskey="<access_key>"
ncloud_secretkey="<secret_key>"
api_full_url="${api_server}${api_url}"

# Get the headers from the Sign_Request function
headers=$(Sign_Request "$api_server" "$api_url" "$apicall_method" "$ncloud_accesskey" "$ncloud_secretkey")

# Prepare header arguments for curl
header_args=()
while IFS= read -r line; do
    header_args+=("-H" "$line")
done <<< "$headers"

# Make the API request
curl -X "$apicall_method" "$api_full_url" "${header_args[@]}"

