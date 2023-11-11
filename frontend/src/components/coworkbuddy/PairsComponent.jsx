import { useCallback, useEffect, useState } from "react"
import { recommendPairs, retrievePairsByRoomId } from "../api/PairApiService"
import { useParams } from "react-router"
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import DropTargetContainerComponent from "./utils/DropTargetContainerComponent"
import { createWorker } from "../api/WorkerApiService";
import { createTask } from "../api/TaskApiService";

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

    function handleNewWorkerChange(event) {
        setNewWorker(event.target.value)
    }

    function handleSubmitWorker() {
        createWorker({ id: null, name: newWorker, active: true, roomId: roomId })
            .then((response) => {
                console.log(response.data)
                setNewWorker('')
                refreshPairs()
            }).catch((error) => console.log(error))
    }

    function handleSubmitTask() {
        createTask({id: null, name: newTaskName, active: true, roomId: roomId})
        .then((response) => {
            setNewTaskName('')
            refreshPairs()
        })
       .catch((error) => console.log(error))
    }

    function handleRecommend() {
        recommendPairs(roomId).then((response) => {
            console.log(response.data)
            refreshPairs()
        })
        .catch((error) => console.log(error))
    }

    function handleSave() {

    }

    function handleNewTaskChange(event) {
        setNewTaskName(event.target.value)
    }

    const [newWorker, setNewWorker] = useState('')
    const [newTaskName, setNewTaskName] = useState('')

    return (
        <DndProvider backend={HTML5Backend}>
            <div className="container">
                <h1 className="fs-2 fw-bold text-dark">Pairs</h1>
                <div className="d-flex my-5 p-1 border border-success">
                    <div className="p-2 my-4 col-4 border border-primary">
                        {
                            loading ? ( <p>Loading...</p> ) 
                            : (
                                <div>
                                    <DropTargetContainerComponent
                                    data={workers.sort((a, b) => (a.active === b.active ? 0 : a.active ? -1 : 1))}
                                    onDrop={handleDrop}
                                    />
                                    <fieldset className="d-flex align-items-baseline mt-1 px-3">
                                        <input type="text" className="px-3 py-1 m-0 rounded-2 form-control focus-primary border border-outline-success" name="newworker" value={newWorker} onChange={handleNewWorkerChange} />
                                         <button type="button" name="add" onClick={handleSubmitWorker} className="btn btn-outline-success">Add</button>
                                    </fieldset>
                                </div>
                                
                            )
                        }
                    </div>
                    <div className="col-8 p-2  border border-primary">
                        <div className="container d-flex flex-wrap">
                        { 
                            loading ? ( <p>Loading tasks ...</p>) 
                            : ( 
                                tasks.sort((a, b) => (a.active === b.active ? 0 : a.active ? -1 : 1)))
                                .map((task) => (
                                    <div key={task.id}>
                                    <h3 className="chip-headers">{task.name}</h3>
                                        <DropTargetContainerComponent
                                            data={{ workers: task.workers, taskId: task.id }}
                                            onDrop={handleDrop}
                                            active ={task.active}
                                        />
                                    </div>

                                  
                                )
                            )
                        }
                        </div>
                        <div className="container">
                            <div className="row">
                                <div className="col-sm-7 d-flex">
                                    <fieldset className="d-flex align-items-baseline">
                                        <input type="text" className="col-sm-8 rounded-2 form-control focus-primary border border-outline-success" name="newworker" value={newTaskName} onChange={handleNewTaskChange} />
                                        <button type="button" name="addTask" onClick={handleSubmitTask} className="col-sm-4 btn btn-outline-success">Add Task</button>
                                    </fieldset>
                                </div>
                                <div className="col-sm-3">
                                    <button type="button" name="recommendTask" onClick={handleRecommend} className="btn btn-outline-success">Recommend Pairs</button>
                                </div>
                                <div className="col-sm-1">
                                    <button type="button" name="saveTask" onClick={handleSave} className="btn btn-outline-success">Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </DndProvider>
    )

}