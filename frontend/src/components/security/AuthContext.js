import { useEffect, useState } from "react";
import { useContext } from "react";
import { createContext } from "react";
import { executeJwtAuthenticateService } from "../api/AuthenticationApiService";
import { apiClient } from "../api/ApiClient";

export const AuthContext = createContext()
export const useAuth = () => useContext(AuthContext)

export default function AuthProvider({ children }) {
    
    const [isAuthenticated, setAuthenticated] = useState(false)
    const [isAdmin, setAdmin] = useState(false)
    const [username, setUsername] = useState(null)
    const [token, setToken] = useState(null)

    useEffect(() => {
        const interceptor = apiClient.interceptors.request.use(
            (config) => {
                console.log('intercepting and adding a token')
                config.headers.Authorization = token
                return config
            }
        )

        return () => {
            apiClient.interceptors.request.eject(interceptor)
        }
    }, [token]);

    async function login(username, password) {
        try {
            const response = await executeJwtAuthenticateService(username, password)
            if(response.status === 200) {
                const b64Token = response.data.token
                const decodedPayload = JSON.parse(atob(b64Token.split('.')[1]))

                if(decodedPayload.scope.includes("ADMIN")) {
                    setAdmin(true);
                    console.log("is Admin: true")
                }

                const jwtToken = 'Bearer ' + b64Token
                setAuthenticated(true)
                setUsername(username)
                setToken(jwtToken)

                return true
            } else {
                logout()
                return false
            }

        } catch (error) {
            logout()
            return false
        }
    }

    function logout() {
        setAuthenticated(false)
        setUsername(null)
        setToken(null)
        setAdmin(false);
    }

    const valueToBeShared = {isAuthenticated, login, logout, isAdmin, username, token}

    return (
        <AuthContext.Provider value={ valueToBeShared }>
            {children}
        </AuthContext.Provider>
    )
}