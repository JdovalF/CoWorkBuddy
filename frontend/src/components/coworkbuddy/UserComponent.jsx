import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { createUserApi, retrieveUserByIdApi, updateUserApi } from "../api/UserApiService"
import { Formik, Form, Field, ErrorMessage } from 'formik';

export default function UserComponent() {
    const { id } = useParams()
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [email, setEmail] = useState('')
    const navigate = useNavigate()
    useEffect(() => refreshUser(id) ,[id])

    function refreshUser(id) {
        if(id !== '-1') {
            retrieveUserByIdApi(id)
                .then((response) => {
                    setUsername(response.data.username)
                    setPassword('********')
                    setEmail(response.data.email)
                })
                .catch((error) => console.log(error))
        }
    }

    function validate(values) {
        const errors = {};
    
        const trimValue = (value) => (value === null || value === undefined) ? null : value.trim();
    
        const emailValue = trimValue(values.email);
        const usernameValue = trimValue(values.username);
        const passwordValue = values.password !== '********' ? trimValue(values.password) : null;
    
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passwordRegex = /^(?=.*[!@#$%^&.*])[a-zA-Z0-9.!@#$%^&*]{8,}$/;
    
        if (!emailRegex.test(emailValue)) {
            errors.email = 'Enter a valid email';
        }
    
        if (usernameValue === null || usernameValue.length < 4) {
            errors.username = 'Enter a valid username (at least 4 characters)';
        }
    
        if (id === '-1' && (passwordValue === null || passwordValue === '')) {
            errors.password = 'Enter a valid password (at least 8 characters, including at least one special character)';
        } else if (id !== '-1' && passwordValue && !passwordRegex.test(passwordValue)) {
            errors.password = 'Enter a valid password (at least 8 characters, including at least one special character)';
        }
    
        return errors;
    }

    function onSubmit(values) {
        const passwordValue = values.password === '********' || values.password === null 
            || values.password.trim() === '' ? null : values.password;
        const user = {
            id: id,
            username: values.username,
            email: values.email,
            password: passwordValue,
        }

        if(id !== '-1') {
            updateUserApi(user).then(() => {
                navigate('/users')
            })
            .catch((error) => console.log(error) )
        } else {
            user.id = null
            createUserApi(user).then(() => {
                navigate('/users')
            })
            .catch((error) => console.log(error) )
        }
    }

    return (
        <div className="container p-5">
            <h1 className="fs-2 fw-bold text-dark">User</h1>
            <div className="d-flex flex-column">
                <Formik
                    initialValues={  {username, email, password } }
                    enableReinitialize = {true}
                    onSubmit={onSubmit}
                    validate={validate}
                    validateOnChange= {false}
                    validateOnBlur= {false}
                >
                    {
                       (props) => (
                        <Form>
                            <fieldset className="justify-content-center row align-items-baseline mt-1" >
                                <label className="col-sm-1">Username:</label>
                                <Field type='text' className="border border-primary col-sm-4" name='username' ></Field>
                            </fieldset>
                            <ErrorMessage name="username" component='div' className="alert alert-warning" />

                            <fieldset className="justify-content-center row align-items-baseline">
                                <label className="col-sm-1">Email:</label>
                                <Field type='email' className="border border-primary col-sm-4" name='email' autocomplete="current-email"></Field>
                            </fieldset>
                            <ErrorMessage name="email" component='div' className="alert alert-warning" />

                            
                            <fieldset className="justify-content-center row align-items-baseline" >
                                <label className="col-sm-1">Password:</label>
                                <Field type='password' className="border border-primary col-sm-4" name='password'  autocomplete="current-password"></Field>
                            </fieldset>
                            <ErrorMessage name="password" component='div' className="alert alert-warning" />

                            <div className="mt-2">
                                <button className="btn btn-outline-success btn" type="submit">Save</button>
                            </div>
                        </Form>
                       )     
                    }
                </Formik>
            </div>
        </div>
    )
}