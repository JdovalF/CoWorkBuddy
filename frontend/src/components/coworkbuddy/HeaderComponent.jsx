import { Link } from "react-router-dom"
import { useAuth } from "../security/AuthContext"

export default function HeaderComponent() {
    
    const authContext = useAuth()
    const isAuthenticated = authContext.isAuthenticated
    const username = authContext.username

    function logout() {
        authContext.logout()
    }

    return (
        <header className="header">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <a className="navbar-brand ms-2 fs-2 fw-bold text-black" href="/">CoworkBuddy</a>
                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                <li className="nav-item">{ isAuthenticated && <Link className="nav-link" to={`/welcome/${username}`} >Home</Link> }</li>
                                <li className="nav-item">{ isAuthenticated && <Link className="nav-link" to="/rooms">Rooms</Link> }</li>
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            <li className="nav-item">{ !isAuthenticated && <Link className="nav-link" to='/login'>Login</Link> }</li>
                            <li className="nav-item">{ isAuthenticated && <Link className="nav-link" to="/logout" onClick={logout} >Logout</Link> }</li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>    
    )
}