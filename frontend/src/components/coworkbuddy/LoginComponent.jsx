import { useState } from "react";
import { useAuth } from '../security/AuthContext';
import { useNavigate } from "react-router-dom";

function LoginComponent() {
    
    const authContext = useAuth()
    const [username, setUsername] = useState('admin')
    const [password, setPassword] = useState('password')
    const [showErrorMessage, setShowErrorMessage] = useState(false)
    const navigate = useNavigate()

    function handleUsernameChange(event) {
        setUsername(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    async function handleSubmit() {
        if(await authContext.login(username, password)) {
            navigate(`/welcome/${username}`)
        } else {
            setShowErrorMessage(true)
        }
    }

    return (
        <div className="container p-5">
            <h1 className="fs-2 fw-bold text-dark">Login</h1>
            <div className="d-flex flex-column ">
                {showErrorMessage && <div className="errorMessage">Authentication Failed. Please check your credentials</div>}
                <fieldset className="justify-content-center row align-items-baseline mt-1">
                    <label className="col-sm-1">Username:</label>
                    <input type="text" className="border border-primary col-sm-3" name="username" value={username} onChange={handleUsernameChange} />
                </fieldset>
                <fieldset className="justify-content-center row align-items-baseline">
                    <label className="col-sm-1">Password:</label>
                    <input className="border border-primary col-sm-3" type="password" name="password" value={password} onChange={handlePasswordChange} />
                </fieldset>
                <div className="mt-2">
                    <button className="btn btn-outline-primary btn" type="button" name="login" onClick={handleSubmit}>Login</button>
                </div>
            </div>
        </div>
    );
}

export default LoginComponent