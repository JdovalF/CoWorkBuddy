import './App.css';
import CoworkbuddyApp from './components/coworkbuddy/CoworkbuddyApp';
import AuthProvider from './components/security/AuthContext';

function App() {
  return (
    <AuthProvider>
      <div className='App'>
        <CoworkbuddyApp />
      </div>
    </AuthProvider>
  );
}

export default App;
