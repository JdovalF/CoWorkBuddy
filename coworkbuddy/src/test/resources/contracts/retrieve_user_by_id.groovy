package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("should retrieve user by id = 8e69ffe0-1864-44b0-8881-ce95f85be14c in database")
    request {
        url "/api/v1/users/8e69ffe0-1864-44b0-8881-ce95f85be14c"
        method GET()
    }
    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            "id": "8e69ffe0-1864-44b0-8881-ce95f85be14c",
            "username": "admin",
            "email": "admin@coworkbuddy.com",
            "roles": [
                    [
                            "id": "be7c0db8-69f2-4e4e-9c6b-4ede9b38ca50",
                            "name": "ADMIN"
                    ]
            ]
        )
    }
}