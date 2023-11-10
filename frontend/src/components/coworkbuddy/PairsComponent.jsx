import { useEffect, useState } from "react"
import { retrieveTasksByRoomId, retrieveWorkersByRoomId } from "../api/RoomApiService"
import { useParams } from "react-router"
import DraggItemComponent from "./utils/DraggItemComponent"
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import DropTargetContainerComponent from "./utils/DropTargetContainerComponent"

export default function PairsComponent() {
    
    const { roomId } = useParams()

    const [tasks, setTasks] = useState([])
    const [workers, setWorkers] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        refreshTasks(roomId)
        refreshWorkers(roomId)
    }, [roomId])

    function refreshTasks(roomId) {
        retrieveTasksByRoomId(roomId).then((response) => { 
            setTasks(response.data)
            setLoading(false)
            console.log(response.data)
        }).catch((error) => console.log(error)) 
    }

    function refreshWorkers(roomId) {
        retrieveWorkersByRoomId(roomId).then((response) => {
            setWorkers(response.data)
            setLoading(false)
            console.log(response.data)
        }).catch((error) => console.log(error)) 
    }

    const handleDrop = (draggedItem) => {
        
        console.log('Item dropped:', draggedItem);
      };

    return (
        <DndProvider backend={HTML5Backend}>
            <div className="container">
                <h1>Pairs</h1>
                <div className="d-flex border border-primary">
                    <div className="p-2 col-4 borer border-success">
                        <h2>Workers</h2>
                        {loading ? (
                            <p>Loading workers ...</p>
                        ) : 
                        ( workers.map(worker => (
                            <DraggItemComponent key={worker.id} worker={worker}/>
                        )))
                    }
                    </div>
                    <div className="p-2 col-8 border border-warning">
                        <h2>Tasks</h2>
                        {loading ? (
                            <p>Loading tasks ...</p>
                        ) : 
                        // <p>tasks loaded</p>
                        (tasks.map(task => (
                                <div key={task.id}>
                                    <h3>{task.name}</h3>
                                    <DropTargetContainerComponent data={task} onDrop={handleDrop}/>
                                </div>
                            )))
                        }
                    </div>
                </div>
            </div>
        </DndProvider>
    )

}