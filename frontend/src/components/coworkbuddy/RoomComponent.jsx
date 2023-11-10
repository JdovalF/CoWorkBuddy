import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createRoom, retrieveRoomById, updateRoom } from "../api/RoomApiService";
import { ErrorMessage, Field, Form, Formik } from "formik";

export default function RoomComponent() {

    const { id, userId } = useParams()

    const [name, setName] = useState('')
    const navigate = useNavigate()

    useEffect(() => refreshRoom(id), [id])

    function refreshRoom(id) {
        if(id !== '-1') {
            retrieveRoomById(id).then((response) => {
                setName(response.data.name)
            } ).catch((error) => console.log(error))
        }
    }

    function onSubmit(values) {
        const room = {
            id: id,
            name: values.name,
            userId: userId
        }

        console.log(id)
        console.log(room.name)
        if(id !== '-1') {
            console.log(id)
            console.log(room.name)
            updateRoom(room).then(() => navigate('/rooms')).catch((error) => console.log(error))
        } else {
            room.id = null
            console.log(id)
            console.log(room.name)
            createRoom(room).then(() => navigate('/rooms')).catch((error) => console.log(error))
        }
    }

    function validate(values) {
        let errors = {}
        if(values.name.lenght < 4) {
            errors.name = 'Enter at least 4 characters for your room name'
        }
        return errors
    }

    return (
        <div className="container p-5">
            <h1 className="fs-2 fw-bold text-dark">Room</h1>
            <div className="d-flex flex-column">
                <Formik
                    initialValues={ { name } }
                    enableReinitialize = { true }
                    onSubmit={onSubmit}
                    validate={validate}
                    validateOnChange={false}
                    validateOnBlur={false}
                >
                    {
                        (props) => (
                            <Form>
                                <fieldset className="justify-content-center row align-items-baseline mt-1" >
                                    <label className="col-sm-1">Name:</label>
                                    <Field type='text' className="border border-primary col-sm-4" name='name' ></Field>
                                </fieldset>
                                <ErrorMessage name="name" component='div' className="alert alert-warning" />
                                <div className="mt-2">
                                    <button className='btn btn-outline-success' type='submit' >Save</button>
                                </div>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        </div>
    )

}