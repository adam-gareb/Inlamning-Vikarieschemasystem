#!/bin/bash

fail() {
  echo FAIL: $1
  exit 1
}

sanity() {
  msg=""
  which jq &>/dev/null || msg="$msg\nMake sure to install jq for this script to work"
  if [[ ! "${#msg}" -eq 0 ]]
  then
    echo -e "Error: $msg"
    exit 1
  fi
}

sanity

HEAD='curl -I'
BODY='curl'
URL='http://localhost:8080/v1'


echo "Testing collecting HEAD, with expected result 200:"
$HEAD "${URL}?format=xml&day=2018-01-16" | grep "200 OK" &> /dev/null || fail "Status code was not 200"
echo "Test succeeded!"

echo "Testing collecting Body, expecting to get proper json schedule"
$BODY "${URL}?format=json" | jq '.' &> /dev/null || fail "Not proper json output"
echo "Test succeeded!"
