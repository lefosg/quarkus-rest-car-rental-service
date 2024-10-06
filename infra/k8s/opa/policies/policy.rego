package policy

default allow = false

allow = true {  # = true is implied, can skip
    input.user.roles[_] == "admin"
}

# rules are OR'ed -> if one of the rules is true, the decision evaluates to true

allow {
    startswith(input.request.path, "/api/") 
    startswith(input.request.path, "/api") 
    not input.request.method == "DELETE" 
}