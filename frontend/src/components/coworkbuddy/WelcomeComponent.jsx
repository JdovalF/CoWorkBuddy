import { Link, useParams } from 'react-router-dom';

export default function WelcomeComponent() {

    const { username } = useParams()

    return (
        <div className="container  p-5">
            <h1 className='fs-2 fw-bold text-dark'>Welcome {username}!</h1>
            <div>Your rooms are <Link to='/rooms'>Here</Link>.</div>
        </div>
    )
}