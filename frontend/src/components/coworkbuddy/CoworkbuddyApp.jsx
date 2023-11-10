import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../security/AuthContext';

import HeaderComponent from './HeaderComponent';
import LoginComponent from './LoginComponent';
import ErrorComponent  from './ErrorComponent';
import WelcomeComponent  from './WelcomeComponent';
import LogoutComponent from './LogoutComponent';
import UsersComponent from './UsersComponent';
import UserComponent from './UserComponent';
import RoomsComponent from './RoomsComponent';

import './Coworkbuddy.css';
import RoomComponent from './RoomComponent';

export default function CoworkbuddyApp() {

    const authContext = useAuth();

    const AdminRoute = ({ children }) => {
        if (authContext.isAuthenticated && authContext.isAdmin) {
            return children;
        }
        return <Navigate to="/" />;
    };
    
    const AuthenticatedRoute = ({ children }) => {
        if (authContext.isAuthenticated) {
            return children;
        }
        return <Navigate to="/" />;
    };

    return (
    <div className='CoworkbuddyApp'>
        <BrowserRouter>
            <HeaderComponent />
            {
                authContext && 
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

                    <Route path='/users/:id' element= {
                        <AdminRoute>
                            <UserComponent />
                        </AdminRoute>
                    }/>

                    <Route path='/rooms' element= {
                        <AuthenticatedRoute>
                            <RoomsComponent />
                        </AuthenticatedRoute>
                    } />

                    <Route path='rooms/:id/users/:userId' element= {
                        <AuthenticatedRoute>
                            <RoomComponent />
                        </AuthenticatedRoute>
                    } />

                    <Route path='' element= {
                        <AuthenticatedRoute>
                            {/* todo: add pairs/:id component */}
                        </AuthenticatedRoute>
                    } />

                    <Route path='/logout' element={
                        <AuthenticatedRoute>
                            <LogoutComponent />
                        </AuthenticatedRoute>
                    }/>

                    <Route path='*' element={<ErrorComponent />}/>
                </Routes>
            }
        </BrowserRouter>
    </div>
    );
}