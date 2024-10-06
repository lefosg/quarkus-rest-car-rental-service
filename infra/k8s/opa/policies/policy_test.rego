package policy_test

import data.policy.allow

test_allow_is_false_by_default {
    not allow
}

test_allow_if_admin {
    allow with input as {
            "user" : {
            "username": "enastypos",
            "roles": ["dev", "admin"]
        }
    }
}

test_deny_if_not_admin {
    not allow with input as {
            "user" : {
            "username": "enastypos",
            "roles": ["dev"]
        }
    }
}

test_allow_if_not_delete_on_api {
    allow with input as {
        "request": {
            "path": "/api",
            "method": "GET"
        }
    }
    allow with input as {
        "request": {
            "path": "/api",
            "method": "PUT"
        }
    }
    allow with input as {
        "request": {
            "path": "/api",
            "method": "POST"
        }
    }
    allow with input as {
        "request": {
            "path": "/api/vehicle",
            "method": "GET"
        }
    }
}

test_deny_if_delete_on_api {
    not allow with input as {
        "request": {
            "path": "/api",
            "method": "DELETE"
        }
    }
}

test_deny_if_delete_on_private_api {
    not allow with input as {
        "request": {
            "path": "/admin",
            "method": "GET"
        }
    }
}