import { useEffect, useState } from "react"

import { retrieveAllUsersApi } from "../api/UserApiService"
import ChipComponent from "./utils/ChipComponent"
import { useNavigate } from "react-router-dom"



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
        navigate(`/user/${id}`)
    }

    function addNewUser() {
        navigate('/user/-1')
    }

    return (
        <div className="container">
            <h1 className="ms-2 fs-2 fw-bold text-dark">Users</h1>
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
                            users.map(user => (
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
            <button className="btn btn-outline-primary m-5" onClick={() => addNewUser()}>Add new User</button>
        </div>
    )
}