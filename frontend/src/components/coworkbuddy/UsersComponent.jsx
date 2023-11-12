import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { retrieveAllUsersApi } from "../api/UserApiService"
import ChipComponent from "./utils/ChipComponent"

export default function UsersComponent() {

    const [users, setUsers] = useState([])
    const navigate = useNavigate()
    useEffect(() => refreshUsers(), [])

    function refreshUsers() {
        retrieveAllUsersApi()
            .then((response) => setUsers(response.data))
            .catch((error) => console.log("error: " + error))
    }

    function updateUser(id) {
        navigate(`/users/${id}`)
    }

    function addNewUser() {
        navigate('/users/-1')
    }

    return (
        <div className="container  p-5">
            <h1 className="fs-2 fw-bold text-dark">Users</h1>
            {
            users.length > 0 &&
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <th></th>
                            <th></th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            users.sort((a, b) => new Date(a.creationDate) - new Date(b.creationDate))
                            .map(user => (
                                <tr key={user.id}>
                                    <td>{user.username}</td>
                                    <td>{user.email}</td>
                                    <td>{user.roles.map(role => <ChipComponent key={role.id} name={role.name}/> )}</td>
                                    <td><button className="btn btn-outline-secondary" onClick={() => updateUser(user.id)} >Update</button></td>
                                </tr>
                            ))
                        }
                    </tbody>
                </table>
            </div>
            }
            <button className="btn btn-outline-primary m-5" onClick={() => addNewUser()}>Add new User</button>
        </div>
    )
}