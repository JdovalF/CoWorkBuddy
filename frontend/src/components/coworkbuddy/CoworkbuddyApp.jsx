import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import AuthProvider, { useAuth } from '../security/AuthContext';
import HeaderComponent from './HeaderComponent';
import LoginComponent from './LoginComponent';
import ErrorComponent  from './ErrorComponent';
import WelcomeComponent  from './WelcomeComponent';
import LogoutComponent from './LogoutComponent';
import UsersComponent from './UsersComponent';
import UserComponent from './UserComponent';

import './Coworkbuddy.css';

function AdminRoute({ children }) {
    const authContext = useAuth()
    if(authContext.isAuthenticated && authContext.isAdmin) {
        return children
    }
    return <Navigate to='/' />
}

function AuthenticatedRoute({ children }) {
    const authContext = useAuth()
    if(authContext.isAuthenticated) {
        return children
    }
    return <Navigate to='/' />
}

export default function CoworkbuddyApp() {

    return (
    <div className='CoworkbuddyApp'>
        <AuthProvider>
            <BrowserRouter>
                <HeaderComponent />
                <Routes>
                    <Route path='/' element={<LoginComponent/>} />
                    <Route path='/login' element={<LoginComponent/>} />

                    <Route path='/welcome/:username' element={
                        <AuthenticatedRoute>
                            <WelcomeComponent />
                        </AuthenticatedRoute>
                    }/>

                    <Route path='/users' element={
                        <AdminRoute>
                            <UsersComponent />
                        </AdminRoute>
                    }/>

                    <Route path='/user/:id' element= {
                        <AdminRoute>
                            <UserComponent />
                        </AdminRoute>
                    }/>

                    <Route path='/logout' element={
                        <AuthenticatedRoute>
                            <LogoutComponent />
                        </AuthenticatedRoute>
                    }/>

                    <Route path='*' element={<ErrorComponent />}/>
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    </div>
    );
}