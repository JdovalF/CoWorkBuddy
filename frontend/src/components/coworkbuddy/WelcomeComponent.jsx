import { Link, useParams } from 'react-router-dom';

export default function WelcomeComponent() {

    const { username } = useParams()

    return (
        <div className="WelcomeComponent">
            <h1 className='ms-2 fs-2 fw-bold text-dark'>Welcome {username}!</h1>
            <div>Your rooms are <Link>Here</Link>.</div>
        </div>
    )
}