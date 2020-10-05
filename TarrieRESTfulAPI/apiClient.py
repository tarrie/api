# https://2.python-requests.org/en/v3.0.0/api/
import requests
import urllib.parse
import json

# url = 'http://localhost:8080/'
url = 'http://tarrie-api-restful.us-east-2.elasticbeanstalk.com/'

url = 'http://api.tarrie.io/'
# https://realpython.com/python-requests/
addUserToGroup = {
    'adminUserId': '1234',
    'newUserIds': ['5', '5'],
}

x = requests.post(url + 'groups/GRP%23boogoParty33333/events', json={'userId': 'USR#54321'})
x.json

x = requests.get(url + 'api/groups')

# https://franklingu.github.io/programming/2017/10/30/post-multipart-form-data-using-requests/

# test images work.

import requests
import os.path

testGIF_path = "../pictures/dancing80s.gif"
with open(testGIF_path, 'rb') as f:
    url = 'http://localhost:8080/groups/GRP%23boogoParty33333/images/profile'
    files = {'file': (testGIF_path, f, 'image/gif'), 'userId': 'USR#becky395'}
    response = requests.post(url, files=files)
    print(response.status_code, response.content)

# test images work


import requests
import os.path

testGIF_path = "../pictures/dancing80s.gif"
with open(testGIF_path, 'rb') as f:
    url = 'http://localhost:8080/pictures/profile/GRP%23boogoParty33333'
    files = {'file': (testGIF_path, f, 'image/gif'), 'userId': 'USR#becky395'}
    response = requests.put(url, files=files)
    print(response.status_code, response.content)

# Testing Create Event!
url = 'http://localhost:8080/'
owner = "USR#northwestern_69"
group = "GRP#boogoParty"
events = {
    "userId": owner,
    "creatorId": group,
    "startTime": "2020-09-29T03:34:45Z",
    "endTime": "2020-09-29T04:34:45Z",
    "name": "Get Fucked",
    "text": "Hey meet at the loop on the loc",
    "location": {"main_text": "Cafe Madra", "secondary_text": "Bhaudaji Road, Matunga, Mumbai, Maharashtra, India"}
}

response = requests.post(url + 'events', json=events)

print(response.status_code, response.content)
print(response.json)
