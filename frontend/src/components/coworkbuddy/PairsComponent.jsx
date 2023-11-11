import { useCallback, useEffect, useState } from "react"
import { retrievePairsByRoomId } from "../api/PairApiService"
import { useParams } from "react-router"
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import DropTargetContainerComponent from "./utils/DropTargetContainerComponent"

export default function PairsComponent() {
    const [pairs, setPairs] = useState([])
    const { roomId } = useParams()

    const [tasks, setTasks] = useState([])
    const [workers, setWorkers] = useState([])
    const [loading, setLoading] = useState(true)
    const [saved, setSaved] = useState(false)

    
    const refreshPairs = useCallback(() => { 
        retrievePairsByRoomId(roomId).then((response) => {
            setSaved(response.data.saved);
            setWorkers(response.data.tasks.filter(task => task.id === null).flatMap(task => task.workers));
            setTasks(response.data.tasks.filter(task => task.id !== null));
            setLoading(false);
        }).catch((error) => console.log(error))
    }, [roomId])

    useEffect(() => {
        refreshPairs()
    },[refreshPairs])

    const handleDrop = (worker, targetTaskId) => {
        const workerWithoutType = { ...worker };
        delete workerWithoutType.type;
    
        const updatedWorkers = targetTaskId === null || targetTaskId === undefined
            ? [...workers, workerWithoutType]
            : workers.filter((w) => w.id !== workerWithoutType.id);
    
        const updatedTasks = tasks.map((task) => {
            if (task.id === targetTaskId) {
                return {
                    ...task,
                    workers: [...task.workers, workerWithoutType],
                };
            } else if (task.workers.some((w) => w.id === workerWithoutType.id)) {
                return {
                    ...task,
                    workers: task.workers.filter((w) => w.id !== workerWithoutType.id),
                };
            } else {
                return task;
            }
        });
    
        const uniqueUpdatedTasks = updatedTasks.map(task => ({
            ...task,
            workers: Array.from(new Set(task.workers.map(w => w.id)))
                .map(id => task.workers.find(worker => worker.id === id)),
        }));
    
        const uniqueWorkers = Array.from(new Set(updatedWorkers.map(worker => worker.id)))
            .map(id => updatedWorkers.find(worker => worker.id === id));
    
        setTasks(uniqueUpdatedTasks);
        setWorkers(uniqueWorkers);
    };


    return (
        <DndProvider backend={HTML5Backend}>
            <div className="container">
                <h1>Pairs</h1>
                <div className="d-flex border border-primary">
                    <div className="p-2 col-4 borer border-success">
                        <h2>Workers</h2>
                        {
                            loading ? ( <p>Loading workers...</p> ) 
                            : (
                                <DropTargetContainerComponent
                                data={workers}
                                onDrop={handleDrop}
                                />
                            )
                        }
                    </div>
                    <div className="p-2 col-8 border border-warning">
                        <h2>Tasks</h2>
                        { 
                            loading ? ( <p>Loading tasks ...</p>) 
                            : (
                                tasks.map((task) => (
                                    <div key={task.id}>
                                    <h3>{task.name}</h3>
                                    <DropTargetContainerComponent
                                        data={{ workers: task.workers, taskId: task.id }}
                                        onDrop={handleDrop}
                                    />
                                    </div>
                                ))
                            )}
                    </div>
                </div>
            </div>
        </DndProvider>
    )

}