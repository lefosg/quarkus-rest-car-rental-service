package test_k8sallowedrepos

import data.k8sallowedrepos.violation

# we want to check the AdmissionReview, not the Pod.yaml 
#  Flow:
#     ____________        _______________________       ________________________       _______
#     | Pod.yaml |  --->  | AdmissionController | --->  | AdmissionReview json | --->  | OPA |
#     |__________|        |_____________________|       |______________________|       |_____|

allowed_repos := ["ghcr.io/lefosg", "gcr.io/google-samples"]

valid_pod := {
    "review": {
        "kind": {
            "kind": "Pod"
        },
        "object": {
            "spec": {
                "containers": [
                    {
                        "image": "ghcr.io/lefosg/quarkus-rest-car-rental-service/fleet:latest"
                    }
                ]
            }
        }
    }

}
invalid_pod := {
    "review": {
        "kind": {
            "kind": "Pod"
        },
        "object": {
            "spec": {
                "containers": [
                    {
                        "image": "hooli.com/nginx"
                    },
                    {
                        "image": "busybox"
                    }
                ]
            }
        }
    }
}
# test names must start with test_

test_pod_trusted {
    input := {
        "review": {
            "object": {
                "spec": {
                    "containers": [
                        {
                            "name": "asd",
                            "image": "hoodle.com/fleet:latest"
                        }
                    ]
                }
            }
        },
        "parameters": {
            "repos": ["ghcr.io/lefosg"]
        }
    }
    violation[{"msg": msg}]
    msg == "container <app-container> has an invalid image repo <disallowed-repo/app-image:latest>, allowed repos are [\"allowed-repo\"]"
}
