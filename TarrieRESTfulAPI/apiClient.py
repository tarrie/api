#https://2.python-requests.org/en/v3.0.0/api/
import requests
import urllib.parse
import json
url = 'http://localhost:8080/api/groups/members'


addUserToGroup = {
	'adminUserId': '1234',
	'newUserIds': ['5','5'],
}
x=requests.get(url+'groups/GRP%23boogoParty33333/events', json={'userId':'USR#54321'})
x.json