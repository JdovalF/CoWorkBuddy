import { useCallback, useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { createUserApi, retrieveUserByIdApi, updateUserApi } from "../api/UserApiService"
import { Formik, Form, Field, ErrorMessage } from 'formik';

export default function UserComponent() {
    const { id } = useParams()
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [email, setEmail] = useState('')
    const navigate = useNavigate()

    const retrieveUserById = useCallback(() => {
        if(id !== '-1') {
            retrieveUserByIdApi(id)
                .then((response) => {
                    setUsername(response.data.username)
                    setPassword('')
                    setEmail(response.data.email)
                })
                .catch((error) => console.log(error))
        }
    }, [id])

    useEffect(() => retrieveUserById(), [id, retrieveUserById] )

    function validate(values) {
        const errors = {};
    
        const trimValue = (value) => (value === null || value === undefined) ? null : value.trim();
    
        const emailValue = trimValue(values.email);
        const usernameValue = trimValue(values.username);
        const passwordValue = trimValue(values.password);
    
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
        const passwordValue = values.password === null || values.password.trim() === '' ? null : values.password;
        const user = {
            id: id,
            username: values.username,
            email: values.email,
            password: passwordValue,
        }

        if(id !== '-1') {
            updateUserApi(user).then((response) => {
                navigate('/users')
            })
            .catch((error) => console.log(error) )
        } else {
            user.id = null
            createUserApi(user).then((response) => {
                navigate('/users')
            })
            .catch((error) => console.log(error) )
        }
    }

    return (
        <div className="container">
            <h1 className="fw-bold text-dark">Enter User Details</h1>
            <div>
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
                            <fieldset className="form-group" >
                                <label>Username:</label>
                                <ErrorMessage 
                                    name="username" 
                                    component='div' 
                                    className="alert alert-warning" 
                                />
                                <Field type='text' className='form-control' name='username' autocomplete="current-username"></Field>
                            </fieldset>
                            <fieldset className="form-group" >
                                <label>Email:</label>
                                <ErrorMessage 
                                    name="email" 
                                    component='div' 
                                    className="alert alert-warning" 
                                />
                                <Field type='email' className='form-control' name='email' autocomplete="current-email"></Field>
                            </fieldset>
                            <fieldset className="form-group" >
                                <label>Password:</label>
                                <ErrorMessage 
                                    name="password"
                                    component='div'
                                    className="alert alert-warning" 
                                />
                                <Field type='password' className='form-control' name='password' autocomplete="current-password"></Field>
                            </fieldset>
                            <div>
                                <button className="btn btn-outline-success m-5" type="submit">Save</button>
                            </div>
                        </Form>
                       )     
                    }
                </Formik>
            </div>
        </div>
    )
}