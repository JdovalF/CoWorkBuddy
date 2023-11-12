import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { retrieveUserByUsernameApi } from "../api/UserApiService"
import { useAuth } from "../security/AuthContext"
import { deleteRoomById } from "../api/RoomApiService"

export default function RoomsComponent() {

    const authContext = useAuth()
    const username = authContext.username
    const [rooms, setRooms] = useState([])
    const [userId, setUserId] = useState()
    const [message, setMessage] = useState()
    const navigate = useNavigate()
    useEffect(() => refreshRooms(username), [username])

    function refreshRooms(username) {
        retrieveUserByUsernameApi(username)
            .then((response) =>{
                setRooms(response.data.rooms)
                setUserId(response.data.id)
            })
            .catch((error) => console.log("error: " + error))
    }

    function updateRoom(id) {
        navigate(`/rooms/${id}/users/${userId}`)
    }

    function selectRoom(id) {
        navigate(`/pairs/${id}`)
    }

    function addNewRoom() {
        navigate(`/rooms/-1/users/${userId}`)
    }

    function deleteRoom(id) {
        deleteRoomById(id).then(() => {
            setMessage(`Delete room by id ${id} was successful!`)
            refreshRooms(username)
        })
        .catch((error) => console.log(error))
    }

    return (
        <div className="container p-5">
            <h1 className="fs-2 fw-bold text-dark">Rooms</h1>
            {message && <div className="alert alert-warning" >{message}</div>}
            {
            rooms.length > 0 &&
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
                            rooms.sort((a, b) => new Date(a.creationDate) - new Date(b.creationDate))
                            .map(room => (
                                <tr key={room.id}>
                                    <td>{room.name}</td>
                                    <td>
                                        <button className="btn btn-outline-secondary" onClick={() => selectRoom(room.id)} >Pairs</button>
                                    </td>
                                    <td>
                                        <button className="btn btn-outline-secondary" onClick={() => updateRoom(room.id)} >Update</button>
                                    </td>
                                    <td>
                                        <button className="btn btn-outline-warning" onClick={() => deleteRoom(room.id)} >Delete</button>
                                    </td>
                                </tr>
                            ))
                        }
                    </tbody>
                </table>
            </div>
            }
            <button className="btn btn-outline-primary m-5" onClick={() => addNewRoom()}>Add new Room</button>
        </div>
    )
}