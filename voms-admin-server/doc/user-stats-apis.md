# VOMS Admin User stats API

## General notes

SSL access with client authentication is *mandatory*.

## Mandatory HTTP request headers

All HTTP requests must include the `X-VOMS-CSRF-GUARD` header.

## Resources

### /user-stats.action

Returns information about number of active, suspended and expired users in a VO

- URL: `https://HOST:8443/voms/VO/apiv2/user-stats.action` 
- Method: GET
- Parameters: None
- Sample response:

```json
{"expiredUsersCount":0,"suspendedUsersCount":0,"usersCount":15}
```

### /suspended-users.action

Returns information about the suspended users in a VO.

- URL: `https://HOST:8443/voms/VO/apiv2/suspended-users.action`
- Method: GET
- Parameters: None
- Sample response:

```json
{"suspendedUsers":
	
	[
		{
			"address":null,
			"certificates":
				[
					{
						"issuerString":"\/C=IT\/O=IGI\/CN=Test CA",
						"subjectString":"\/C=IT\/O=IGI\/CN=(Parenthesis)",
						"suspended":true,
						"suspensionReason":"test"
					}
				],
			"creationTime":"2012-12-12T16:41:50",
			"emailAddress":"andrea.ceccanti@cnaf.infn.it",
			"endTime":"2013-12-12T16:41:50",
			"id":4,
			"institution":null,
			"name":null,
			"phoneNumber":null,
			"surname":null,
			"suspended":true,
			"suspensionReason":"test",
			"suspensionReasonCode":"OTHER"
		}
	]
}

```

### /expired-users.action

Returns information about the expired users in a VO.

- URL: `https://HOST:8443/voms/VO/apiv2/expired-users.action`
- Method: GET
- Parameters: None
- Sample response:

```json
{"suspendedUsers":
	
	[
		{
			"address":null,
			"certificates":
				[
					{
						"issuerString":"\/C=IT\/O=IGI\/CN=Test CA",
						"subjectString":"\/C=IT\/O=IGI\/CN=(Parenthesis)",
						"suspended":true,
						"suspensionReason":"User has expired."
					}
				],
			"creationTime":"2012-12-12T16:41:50",
			"emailAddress":"andrea.ceccanti@cnaf.infn.it",
			"endTime":"2013-1-12T16:41:50",
			"id":4,
			"institution":null,
			"name":null,
			"phoneNumber":null,
			"surname":null,
			"suspended":true,
			"suspensionReason":"User has expired.",
			"suspensionReasonCode":"OTHER"
		}
	]
}

```
